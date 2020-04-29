package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.curseglyphs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Roaring extends Armor.Glyph {
    private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xFF0000 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.level());

        if (Random.Int( level/2 + 5) >= 4) {
            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                if (Dungeon.level.heroFOV[mob.pos]) {
                    Buff.prolong(mob, Amok.class, 3f );
                }
            }

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                Char ch = Actor.findChar( defender.pos + PathFinder.NEIGHBOURS8[i] );
                if (ch != null && ch.isAlive()) {
                    Buff.prolong(ch, Paralysis.class, 1f );
                }
            }

            attacker.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
            Sample.INSTANCE.play(Assets.SND_CHALLENGE);
            Invisibility.dispel();
            GLog.n(Messages.get(this, "msg_" + (Random.Int(3)+1)));

        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }
}
