package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Kinesis extends Armor.Glyph {

    private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF00 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max( 0, armor.level() );
        float effectiveness =  (level + 1)/(level + 5);
        int conservedDamage = 0;

        if (defender.buff(ArmorConservedDamage.class) != null) {
            conservedDamage = defender.buff(ArmorConservedDamage.class).damageBonus();
            defender.buff(ArmorConservedDamage.class).detach();
            attacker.damage(conservedDamage, defender);
        }

        if (Random.Int( level + 5 ) >= 4){
            int oppositeHero = attacker.pos + (attacker.pos - defender.pos);
            Ballistica trajectory = new Ballistica(attacker.pos, oppositeHero, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(attacker, trajectory, 2);
        }

        conservedDamage = (int) (damage * effectiveness);
        if ( conservedDamage >0 ) {
            Buff.affect(defender, ArmorConservedDamage.class).setBonus( conservedDamage );
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return YELLOW;
    }

    public static class ArmorConservedDamage extends Buff {

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            if (preservedDamage >= 20){
                icon.hardlight(1f, 0f, 0f);
            } else if (preservedDamage >= 10) {
                icon.hardlight(1f, 1f - (preservedDamage - 5f)*.2f, 0f);
            } else {
                icon.hardlight(1f, 1f, 1f - preservedDamage*.2f);
            }
        }

        private float preservedDamage;

        public void setBonus(int bonus){
            preservedDamage = bonus;
        }

        public int damageBonus(){
            return (int)Math.ceil(preservedDamage);
        }


        // reduce preserved damage
        @Override
        public boolean act() {
            preservedDamage -= Math.max(preservedDamage*.025f, 0.1f);
            if (preservedDamage <= 0) detach();
            else if (preservedDamage <= 10) BuffIndicator.refreshHero();

            spend(TICK);
            return true;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", damageBonus());
        }

        private static final String PRESERVED_DAMAGE = "preserve_damage";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(PRESERVED_DAMAGE, preservedDamage);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            if (bundle.contains(PRESERVED_DAMAGE)){
                preservedDamage = bundle.getFloat(PRESERVED_DAMAGE);
            } else {
                preservedDamage = cooldown()/10;
                spend(cooldown());
            }
        }
    }

}
