package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.curseenchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Precise extends Weapon.Enchantment {

    private static ItemSprite.Glowing PPINK = new ItemSprite.Glowing( 0xFF55CC );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        return damage;
    }

    //called from attackSkill in Hero, Statue, (and GhostHero)
    public static boolean rollToGuaranteeHit( Weapon weapon ){
        // lvl 0 - 13%
        // lvl 1 - 22%
        // lvl 2 - 30%
        int level = Math.max( 0, weapon.level() );

        if (Random.Int( level + 7 ) >= 6) {
            return true;
        }

        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PPINK;
    }
}

