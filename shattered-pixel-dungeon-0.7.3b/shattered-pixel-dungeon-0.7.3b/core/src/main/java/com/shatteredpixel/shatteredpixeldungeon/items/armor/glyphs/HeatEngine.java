package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;

public class HeatEngine extends Armor.Glyph {
    private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF4400 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        //no proc effect, see Burning.act
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ORANGE;
    }

    //FIXME doesn't work with sad ghost
    public static class EngineOperate extends Buff {

        {
            type = buffType.POSITIVE;
        }

        private static int output = 0;

        public boolean needOutputUpdate = true;

        public static float speedBonus() {
            return (float)(Math.pow(1.15, output()));
        }

        public static int output() {
            return output;
        }

        public void incOutput() {
            incOutput(1);
        }

        public void decOutput() {
            incOutput( -1 );
        }

        public void incOutput(int i) {
            output += i;
            needOutputUpdate = true;
        }

        @Override
        public boolean act() {
            Hero hero = (Hero)target;

            if (hero.belongings.armor == null || !hero.belongings.armor.hasGlyph(HeatEngine.class, hero)) {
                detach();
                return true;
            }

            int level = hero.belongings.armor.level();

            if (hero.buff(Burning.class) != null){
                //max shielding equal to the armors level (this does mean no shield at lvl 0)
                if ((output()) < level) {
                    incOutput();

                    //generates 0.2 + 0.1*lvl shield per turn
                    spend( 10f / (2f + level));
                } else {

                    //if shield is maxed, don't wait longer than 1 turn to try again
                    spend( Math.min( TICK, 10f / (2f + level)));
                }

            } else if (hero.buff(Burning.class) == null){
                if (output() > 0){
                    decOutput();

                    //shield decays at a rate of 1 per turn.
                    spend(TICK);
                } else {
                    detach();
                }
            }

            return true;
        }

        public void startDecay(){
            //sets the buff to start acting next turn. Invoked by Burning when it expires.
            spend(-cooldown()+2);
        }

//        @Override
//        public void restoreFromBundle(Bundle bundle) {
//            super.restoreFromBundle(bundle);
//            //pre-0.7.0
//            if (bundle.contains("added")){
//                setShield(bundle.getInt("added"));
//            }
//        }
    }

}
