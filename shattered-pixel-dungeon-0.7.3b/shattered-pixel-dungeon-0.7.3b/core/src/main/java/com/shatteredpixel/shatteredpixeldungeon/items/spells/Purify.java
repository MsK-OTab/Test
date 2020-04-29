package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class Purify extends InventorySpell {

    {
        image = ItemSpriteSheet.CLEANSE;
        mode = WndBag.Mode.UNCURSABLE;
    }

    @Override
    protected void onItemSelected(Item item) {

        new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;
        boolean procced = uncurse( curUser, item );

        Weakness.detach( curUser, Weakness.class );

        if (procced) {
            GLog.p( Messages.get(this, "repaired") );
        } else {
            GLog.i( Messages.get(this, "not_repaired") );
        }
    }

    public static boolean uncurse( Hero hero, Item item ) {

        boolean procced = false;

        if (item != null) {
            item.cursedKnown = true;
            if (item.cursed) {
                item.cursed = false;
                procced = true;
            }
        }
        if (item instanceof MeleeWeapon || item instanceof SpiritBow) {
            Weapon w = (Weapon) item;
            if (w.enchantment != null) {
                Weapon.Enchantment ench = w.enchantment;
                w.enchant( null );
                w.enchant(Weapon.Enchantment.toUncursed.get(ench.getClass()));
                procced = true;
                if (w instanceof MagesStaff){
                    ((MagesStaff) w).updateWand(true);
                }
            }
        } else if (item instanceof Armor){
            Armor a = (Armor) item;
            if (a.hasCurseGlyph()){
                Armor.Glyph curse = a.glyph;
                a.inscribe(null);
                a.inscribe(Armor.Glyph.toUncursed.get(curse.getClass()));
                procced = true;
            }
        } else if (item instanceof Wand){
            ((Wand) item).updateLevel();
        }

        if (procced) {
            hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
            hero.updateHT( false ); //for ring of might
            updateQuickslot();
        }

        updateQuickslot();

            return procced;
    }



    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ScrollOfRemoveCurse.class, StoneOfEnchantment.class};
            inQuantity = new int[]{1, 1};

            cost = 6;

            output = Purify.class;
            outQuantity = 2;
        }

    }

}
