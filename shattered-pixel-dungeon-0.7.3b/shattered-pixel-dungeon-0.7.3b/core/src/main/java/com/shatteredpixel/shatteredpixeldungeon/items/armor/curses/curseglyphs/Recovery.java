package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.curseglyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Recovery extends Armor.Glyph {

    private static ItemSprite.Glowing RGREEN = new ItemSprite.Glowing( 0x55CC55 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max( 0, armor.level() );

        if ( Random.Int( level + 5 ) >= 4 ) {

            //assumes using up 10% of starving, and healing of 1 hp per 10 turns;
            int healing = Math.min((int) Hunger.STARVING/100, defender.HT - defender.HP);

            if (healing > 0) {

                Hunger hunger = Buff.affect(defender, Hunger.class);

                if (hunger != null && !hunger.isStarving()) {
                    if(  Random.Int( level/2 + 5) >= 4 ) {
                        hunger.reduceHunger( healing * -0.5f );
                    } else {

                        hunger.reduceHunger( -(healing / 3) );
                    }
                    BuffIndicator.refreshHero();

                    defender.HP += healing;
                    defender.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                    defender.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( healing ) );
                }
            }

        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RGREEN;
    }
}
