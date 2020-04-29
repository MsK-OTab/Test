package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.curseglyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Cloning extends Armor.Glyph {
    private static ItemSprite.Glowing BRED = new ItemSprite.Glowing( 0xDD2222 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.level());

        if ( Random.Int( level + 20 ) >= 17 ){
            ArrayList<Integer> spawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = attacker.pos + PathFinder.NEIGHBOURS8[i];
                if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                    spawnPoints.add( p );
                }
            }

            if (spawnPoints.size() > 0) {

                Mob m = null;
                if (Random.Int(2) == 0 && defender instanceof Hero){
                    m = new MirrorImage();
                    ((MirrorImage)m).duplicate( (Hero)defender );

                }

                if (m != null) {
                    GameScene.add(m);
                    ScrollOfTeleportation.appear(m, Random.element(spawnPoints));
                }

                int duration = Random.IntRange( 1, 2 );
                Buff.affect( attacker, Charm.class, duration ).object = defender.id();
                attacker.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 1 );

            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BRED;
    }

}
