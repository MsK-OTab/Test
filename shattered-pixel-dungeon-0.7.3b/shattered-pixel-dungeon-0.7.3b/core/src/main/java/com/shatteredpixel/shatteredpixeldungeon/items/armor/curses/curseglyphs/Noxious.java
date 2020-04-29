package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.curseglyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Noxious extends Armor.Glyph {

    private static ItemSprite.Glowing STENCH = new ItemSprite.Glowing( 0x004411 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max( 0, armor.level() );

        if ( Random.Int( level + 5 ) >= 4 ) {

            int pos = defender.pos;
            for (int i : PathFinder.NEIGHBOURS8){
                Splash.at(pos+i, 0x000000, 5);
                if (Actor.findChar(pos+i) != null)
                    Buff.affect(Actor.findChar(pos+i), Ooze.class).set( 20f );
            }
        }

        if( Random.Int( level + 20 ) >= 17 ) {
            Buff.affect( defender, NoxiousImmunity.class ).set( NoxiousBuff.DURATION + 10 );
            Buff.affect( defender, NoxiousBuff.class ).set( NoxiousBuff.DURATION );
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return STENCH;
    }

    public static class NoxiousBuff extends Buff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        protected float left;

        private static final String LEFT	= "left";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( LEFT, left );

        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            left = bundle.getFloat( LEFT );
        }

        public void set( float duration ) {
            this.left = duration;
        }


        @Override
        public boolean act() {
            GameScene.add(Blob.seed(target.pos, 20, StenchGas.class));

            spend(TICK);
            left -= TICK;
            if (left <= 0){

                detach();
            } else if (left < 5){
                BuffIndicator.refreshHero();
            }

            return true;
        }




    }
    public static class NoxiousImmunity extends Buff {

        protected float left;
        public void set( float duration ) {
            this.left = duration;
        }

        @Override
        public boolean act() {
            spend(TICK);
            left -= TICK;
            if (left <= 0){

                detach();
            } else if (left < 5){
                BuffIndicator.refreshHero();
            }

            return true;
        }

        {
            immunities.add( StenchGas.class );
            immunities.add( ToxicGas.class );
        }
    }

}
