package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.curseglyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Dodge extends Armor.Glyph {

    private static ItemSprite.Glowing MSBLUE = new ItemSprite.Glowing( 0x6666FF );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage ) {

        final int level = Math.max( 0, armor.level() );

        if ( ( damage >= defender.HT/3 || damage >= defender.HP ) &&  Random.Int( level/2 + 5) >= 4 ) {
            Buff.append(defender, Invisibility.class, 3);
            ScrollOfTeleportation.teleportHero(Dungeon.hero);
            return 0;
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return MSBLUE;
    }

}
