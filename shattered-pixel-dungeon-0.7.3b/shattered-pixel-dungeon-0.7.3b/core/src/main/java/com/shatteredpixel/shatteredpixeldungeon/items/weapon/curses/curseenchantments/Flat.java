package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.curseenchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;

public class Flat extends Weapon.Enchantment {

    private static ItemSprite.Glowing FGREY = new ItemSprite.Glowing( 0xCCCCCC );
    private boolean weakhit = false;

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {

        int weakdamage = (weapon.max() + weapon.min())/2 - defender.drRoll();
        if ( weakhit && damage <= weakdamage) {
            damage = weapon.max() - damage + defender.drRoll();
            weakhit = false;
        } else if ( !weakhit && damage <= weakdamage ) {
            weakhit = true;
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return FGREY;
    }

    private static final String WEAKHIT = "weakhit";

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        weakhit = bundle.getBoolean(WEAKHIT);
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        bundle.put(WEAKHIT, weakhit);
    }

}
