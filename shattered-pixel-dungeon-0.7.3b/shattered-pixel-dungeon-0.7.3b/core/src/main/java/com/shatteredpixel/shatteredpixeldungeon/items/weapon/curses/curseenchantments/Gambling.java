package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.curseenchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Gambling extends Weapon.Enchantment {

    private static ItemSprite.Glowing GGREEN = new ItemSprite.Glowing( 0x33AA44 );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {

        int level = Math.max( 0, weapon.level() );
        float Chance = ( 3*level + 20f )/( 2*level + 40f );

        if( Random.Int( 2*level + 40 ) >= ( 20 - level ) ) {
            damage = weapon.max();
        } else {
            damage = weapon.min();
        }

        return damage - defender.drRoll();
    }


    @Override
    public ItemSprite.Glowing glowing() {
        return GGREEN;
    }
}
