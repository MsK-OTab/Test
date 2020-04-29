package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Adaptation extends Armor.Glyph {
    private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x99BB33 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        //no proc effect, see HighGrass.trample, armor.speedfactor for effect.

        final int level = Math.max( 0, armor.level() );

        if (Random.Int( 4 ) == 0) {
            if (defender.flying) {
            } else if (Dungeon.level.map[defender.pos] == Terrain.EMPTY) {
                Buff.affect(defender, Earthroot.Armor.class).level(5 + 2 * level);
                CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY, 0.05f, 8);
                Camera.main.shake(1, 0.4f);
            } else if (Dungeon.level.map[defender.pos] == Terrain.GRASS
                    || Dungeon.level.map[defender.pos] == Terrain.FURROWED_GRASS
                    || Dungeon.level.map[defender.pos] == Terrain.HIGH_GRASS
            ) {
                Buff.affect(defender, Earthroot.Armor.class).level(5 + 4 * level);
                CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY, 0.05f, 8);
                Camera.main.shake(1, 0.4f);
            }
        }
        return damage;
    }


    @Override
    public ItemSprite.Glowing glowing() {
        return GREEN;
    }

    public static class Camo extends Invisibility {

        {
            announced = false;
        }

        private int pos;
        private int left;

        @Override
        public boolean act() {
            left--;
            if (left == 0 || target.pos != pos) {
                detach();
            } else {
                spend(TICK);
            }
            return true;
        }

        public void set(int time){
            left = time;
            pos = target.pos;
            Sample.INSTANCE.play( Assets.SND_MELD );
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns(left));
        }

        private static final String POS     = "pos";
        private static final String LEFT	= "left";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( POS, pos );
            bundle.put( LEFT, left );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            pos = bundle.getInt( POS );
            left = bundle.getInt( LEFT );
        }
    }

}
