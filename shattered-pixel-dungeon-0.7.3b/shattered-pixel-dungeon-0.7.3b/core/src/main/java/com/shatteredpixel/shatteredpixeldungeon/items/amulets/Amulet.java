/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.amulets;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.ItemStatusHandler;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Amulet extends EquipableItem {

    @Override
    public boolean doEquip( Hero hero ) {

        detach(hero.belongings.backpack);

        if (hero.belongings.amulet == null || hero.belongings.amulet.doUnequip( hero, true, false )) {

            hero.belongings.amulet = this;

            cursedKnown = true;
            if (cursed) {
                equipCursed( hero );
                GLog.n( Messages.get(Amulet.class, "equip_cursed") );
            }

            activate(hero);

            hero.spendAndNext( time2equip( hero ) );
            return true;

        } else {

            collect( hero.belongings.backpack );
            return false;

        }
    }

    protected Buff buff;

    private static final Class<?>[] amulets = {
            AmuletOfRegeneration.class,
            AmuletOfHaste.class,
            //AmuletOfClarity.class,
            AmuletOfDetection.class,
            AmuletOfWealth.class,
            AmuletOfHarm.class,
            AmuletOfSatiety.class,
            //AmuletOfConflict,
//            AmuletOfBreathing.class
    };

    private static final HashMap<String, Integer> gems = new HashMap<String, Integer>() {
        {
            put("beryl", ItemSpriteSheet.AMULET_BERYL);
            put("citrine", ItemSpriteSheet.AMULET_CITRINE);
            //put("lapis lazuli", ItemSpriteSheet.AMULET_AQUAMARINE);
            put("platinum", ItemSpriteSheet.AMULET_PLATINUM);
            put("silver", ItemSpriteSheet.AMULET_SILVER);
            put("jasper", ItemSpriteSheet.AMULET_JASPER);
            put("brass", ItemSpriteSheet.AMULET_BRASS);
            //put("zirconium", ItemSpriteSheet.AMULET_ZIRCONIUM);
//            put("crastal", ItemSpriteSheet.AMULET_CRYSTAL);
        }
    };


    private static ItemStatusHandler<Amulet> handler;

    private String gem;

    //amulets cannot be 'used' like other equipment, so they ID purely based on exp
    private float levelsToID = 1;

    @SuppressWarnings("unchecked")
    public static void initGems() {
        handler = new ItemStatusHandler<>( (Class<? extends Amulet>[])amulets, gems );
    }

    public static void save( Bundle bundle ) {
        handler.save( bundle );
    }

    public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
        handler.saveSelectively( bundle, items );
    }

    @SuppressWarnings("unchecked")
    public static void restore( Bundle bundle ) {
        handler = new ItemStatusHandler<>( (Class<? extends Amulet>[])amulets, gems, bundle );
    }

    public Amulet() {
        super();
        reset();
    }

    //anonymous amulets are always IDed, do not affect ID status,
    //and their sprite is replaced by a placeholder if they are not known,
    //useful for items that appear in UIs, or which are only spawned for their effects
    protected boolean anonymous = false;
    public void anonymize(){
        if (!isKnown()) image = ItemSpriteSheet.AMULET_HOLDER;
        anonymous = true;
    }

    public void reset() {
        super.reset();
        levelsToID = 1;
        if (handler != null && handler.contains(this)){
            image = handler.image(this);
            gem = handler.label(this);
        }
    }

    public void activate( Char ch ) {
        buff = buff();
        buff.attachTo( ch );
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

            hero.belongings.amulet = null;
            hero.remove( buff );
            buff = null;

            return true;

        } else {

            return false;

        }
    }

    public boolean isKnown() {
        return anonymous || (handler != null && handler.isKnown( this ));
    }

    public void setKnown() {
        if (!anonymous) {
            if (!isKnown()) {
                handler.know(this);
            }

            if (Dungeon.hero.isAlive()) {
                Catalog.setSeen(getClass());
            }
        }
    }

    @Override
    public String name() {
        return isKnown() ? super.name() : Messages.get(Amulet.class, gem);
    }

    @Override
    public String info(){

        String desc = isKnown() ? super.desc() : Messages.get(this, "unknown_desc");

        if (cursed && isEquipped( Dungeon.hero )) {
            desc += "\n\n" + Messages.get(Amulet.class, "cursed_worn");

        } else if (cursed && cursedKnown) {
            desc += "\n\n" + Messages.get(Amulet.class, "curse_known");

        } else if (!isIdentified() && cursedKnown){
            desc += "\n\n" + Messages.get(Amulet.class, "not_cursed");

        }

        if (isKnown()) {
            desc += "\n\n" + statsInfo();
        }

        return desc;
    }

    protected String statsInfo(){
        return "";
    }

    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && isKnown();
    }

    @Override
    public Item identify() {
        setKnown();
        levelsToID = 0;
        return super.identify();
    }

    @Override
    public Item random() {
        //+1: 66.67% (2/3)
        //+2: 33.33% (1/3)
        int n = 1;
        if (Random.Int(3) == 0) {
            n++;
        }
        level(n);

        //30% chance to be cursed
        if (Random.Float() < 0.3f) {
            cursed = true;
        }

        return this;
    }

    public static HashSet<Class<? extends Amulet>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Amulet>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == amulets.length - 2;
    }

    @Override
    public int price() {
        int price = 100;
        if (cursed && cursedKnown) {
            price /= 2;
        }
        if (levelKnown) {
            if (level() > 0) {
                price *= (level() + 1);
            } else if (level() < 0) {
                price /= (1 - level());
            }
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }


    protected AmuletBuff buff() {
        return null;
    }

    private static final String LEVELS_TO_ID    = "levels_to_ID";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEVELS_TO_ID, levelsToID );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        levelsToID = bundle.getFloat( LEVELS_TO_ID );

        //pre-0.7.2 saves
        if (bundle.contains( "unfamiliarity" )){
            levelsToID = bundle.getInt( "unfamiliarity" ) / 200f;
        }
    }

    public void onHeroGainExp( float levelPercent, Hero hero ){
        if (isIdentified() || !isEquipped(hero)) return;
        //becomes IDed after 1 level
        levelsToID -= levelPercent;
        if (levelsToID <= 0){
            identify();
            GLog.p( Messages.get(Amulet.class, "identify", toString()) );
            Badges.validateItemLevelAquired( this );
        }
    }

    public static int getBonus(Char target, Class<?extends AmuletBuff> type){
        int bonus = 0;
        for (AmuletBuff buff : target.buffs(type)) {
            bonus += buff.level();
        }
        return bonus;
    }

    public int soloBonus(){
        if (cursed){
            return Math.min( 0, Amulet.this.level()-2 );
        } else {
            return Amulet.this.level();
        }
    }

    public boolean isEquipped( Hero hero ) {
        return hero.belongings.amulet == this;
    }


    public class AmuletBuff extends Buff {

        @Override
        public boolean act() {

            spend( TICK );

            return true;
        }

        public int level(){
            return Amulet.this.soloBonus();
        }

    }

}
