package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Refrigeration extends Armor.Glyph {

    private static ItemSprite.Glowing SKYBLUE = new ItemSprite.Glowing( 0x88CCEE );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        //no proc effect, see Hero.isImmune, Char.spend, Chill.act

        int level = Math.max( 0, armor.level() );
        if( Random.Int( level/2 + 5) >= 4 ) {
            Splash.at(defender.pos, 0xFFB2D6FF, 5);
            Sample.INSTANCE.play(Assets.SND_SHATTER);

            PathFinder.buildDistanceMap(defender.pos, BArray.not(Dungeon.level.solid, null), 2);
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                    GameScene.add(Blob.seed(i, 3, Freezing.class));
                }
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return SKYBLUE;
    }

    //FIXME doesn't work with sad ghost
    public static class FrozenShield extends ShieldBuff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {
            Hero hero = (Hero)target;

            if (hero.belongings.armor == null || !hero.belongings.armor.hasGlyph(Refrigeration.class, hero)) {
                detach();
                return true;
            }

            int level = hero.belongings.armor.level();

            if (hero.buff(Chill.class) != null){
                //max shielding equal to the armors level (this does mean no shield at lvl 0)
                if (shielding() < level) {
                    incShield();

                    //generates 0.2 + 0.1*lvl shield per turn
                    spend( 10f / (2f + level));
                } else {

                    //if shield is maxed, don't wait longer than 1 turn to try again
                    spend( Math.min( TICK, 10f / (2f + level)));
                }

            } else if (hero.buff(Chill.class) == null){
                if (shielding() > 0){
                    decShield();

                    //shield decays at a rate of 1 per turn.
                    spend(TICK);
                } else {
                    detach();
                }
            }

            return true;
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            //pre-0.7.0
            if (bundle.contains("added")){
                setShield(bundle.getInt("added"));
            }
        }
    }
}
