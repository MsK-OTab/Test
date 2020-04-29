//package com.shatteredpixel.shatteredpixeldungeon.items.armor;
//
//import com.shatteredpixel.shatteredpixeldungeon.Badges;
//import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
//import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
//import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
//import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
//import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
//import com.shatteredpixel.shatteredpixeldungeon.items.Item;
//import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
//import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
//import com.watabou.noosa.particles.Emitter;
//import com.watabou.utils.Bundle;
//
//import java.util.ArrayList;
//
//public class BodyArmor extends Armor {
//
//    public BodyArmor ( int tier ){
//        super(tier);
//    }
//
//    private BrokenSeal seal;
//
//    private static final String SEAL            = "seal";
//
//    @Override
//    public void storeInBundle( Bundle bundle ) {
//        super.storeInBundle( bundle );
//        bundle.put( SEAL, seal);
//    }
//
//    @Override
//    public void restoreFromBundle( Bundle bundle ) {
//        super.restoreFromBundle(bundle);
//        seal = (BrokenSeal) bundle.get(SEAL);
//    }
//
//    @Override
//    public void reset() {
//        super.reset();
//        //armor can be kept in bones between runs, the seal cannot.
//        seal = null;
//    }
//
//    @Override
//    public ArrayList<String> actions(Hero hero) {
//        ArrayList<String> actions = super.actions(hero);
//        if (seal != null) actions.add(AC_DETACH);
//        return actions;
//    }
//
//    @Override
//    public void execute(Hero hero, String action) {
//
//        super.execute(hero, action);
//
//        if (action.equals(AC_DETACH) && seal != null){
//            BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
//            if (sealBuff != null) sealBuff.setArmor(null);
//
//            if (seal.level() > 0){
//                degrade();
//            }
//            GLog.i( Messages.get(Armor.class, "detach_seal") );
//            hero.sprite.operate(hero.pos);
//            if (!seal.collect()){
//                Dungeon.level.drop(seal, hero.pos);
//            }
//            seal = null;
//        }
//    }
//
//    @Override
//    public boolean doEquip( Hero hero ) {
//
//        detach(hero.belongings.backpack);
//
//        if (hero.belongings.bodyArmor == null || hero.belongings.bodyArmor.doUnequip( hero, true, false )) {
//
//            hero.belongings.bodyArmor = this;
//
//            cursedKnown = true;
//            if (cursed) {
//                equipCursed( hero );
//                GLog.n( Messages.get(Armor.class, "equip_cursed") );
//            }
//
//            ((HeroSprite)hero.sprite).updateArmor();
//            activate(hero);
//
//            hero.spendAndNext( time2equip( hero ) );
//            return true;
//
//        } else {
//
//            collect( hero.belongings.backpack );
//            return false;
//
//        }
//    }
//
//    @Override
//    public void activate(Char ch) {
//        if (seal != null) Buff.affect(ch, BrokenSeal.WarriorShield.class).setArmor(this);
//    }
//
//    @Override
//    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
//        if (super.doUnequip( hero, collect, single )) {
//
//            hero.belongings.bodyArmor = null;
//            ((HeroSprite)hero.sprite).updateArmor();
//
//			BrokenSeal.WarriorShield sealBuff = hero.buff(BrokenSeal.WarriorShield.class);
//			if (sealBuff != null) sealBuff.setArmor(null);
//
//            return true;
//
//        } else {
//
//            return false;
//
//        }
//    }
//
//    @Override
//    public boolean isEquipped( Hero hero ) {
//        return hero.belongings.bodyArmor == this;
//    }
//
//    public void affixSeal(BrokenSeal seal){
//        this.seal = seal;
//        if (seal.level() > 0){
//            //doesn't trigger upgrading logic such as affecting curses/glyphs
//            level(level()+1);
//            Badges.validateItemLevelAquired(this);
//        }
//        if (isEquipped(Dungeon.hero)){
//            Buff.affect(Dungeon.hero, BrokenSeal.WarriorShield.class).setArmor(this);
//        }
//    }
//
//    public BrokenSeal checkSeal(){
//        return seal;
//    }
//
//    @Override
//    public Item upgrade(boolean inscribe ) {
//
//        if (seal != null && seal.level() == 0)
//            seal.upgrade();
//
//        return super.upgrade();
//    }
//
//    @Override
//    public String info() {
//        String info = desc();
//
//        info += super.info();
//
//        if (cursed && isEquipped( Dungeon.hero )) {
//        } else if (cursedKnown && cursed) {
//        } else if (seal != null) {
//            info += "\n\n" + Messages.get(Armor.class, "seal_attached");
//        } else if (!isIdentified() && cursedKnown){
//        }
//
//        return info;
//    }
//
//    @Override
//    public Emitter emitter() {
//        if (seal == null) return super.emitter();
//        Emitter emitter = new Emitter();
//        emitter.pos(ItemSpriteSheet.film.width(image)/2f + 2f, ItemSpriteSheet.film.height(image)/3f);
//        emitter.fillTarget = false;
//        emitter.pour(Speck.factory( Speck.RED_LIGHT ), 0.6f);
//        return emitter;
//    }
//
//    @Override
//    public int price() {
//        if (seal != null) return 0;
//
//        return super.price();
//    }
//
//
//
//
//}
