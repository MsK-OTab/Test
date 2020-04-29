package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Clanging extends Armor.Glyph {
    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        if (Random.Int(20) == 0) {

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                mob.beckon(attacker.pos);
            }
            defender.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
            Sample.INSTANCE.play(Assets.SND_DESCEND);
            Invisibility.dispel();
            GLog.n(Messages.get(this, "msg_" + (Random.Int(5)+1)));
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public boolean curse() {
        return true;
    }
}
