package com.shatteredpixel.shatteredpixeldungeon.items.armor.curses.curseglyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Dreamfoil;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Thriving extends Armor.Glyph {

    private static ItemSprite.Glowing FGREEN = new ItemSprite.Glowing( 0x228822 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max( 0, armor.level() );

        if ( Random.Int( level + 5 ) >= 4) {

            if ( Random.Int( level + 23 ) >= 18) {

                Plant.Seed s;
                do{
                    s = (Plant.Seed) Generator.random(Generator.Category.SEED);
                } while (s instanceof BlandfruitBush.Seed || s instanceof Starflower.Seed);

                Plant p = s.couch(defender.pos, null);

                HeroSubClass sc = ((Hero) defender).subClass;
                ((Hero) defender).subClass = HeroSubClass.WARDEN;
                p.activate( defender );
                ((Hero) defender).subClass = sc;

            } else {

                Plant.Seed s;
                do{
                    s = (Plant.Seed) Generator.random(Generator.Category.SEED);
                } while ( !( s instanceof Dreamfoil.Seed || s instanceof Earthroot.Seed ||
                        s instanceof Sungrass.Seed || s instanceof Swiftthistle.Seed ));

                Plant p = s.couch(defender.pos, null);

                p.activate( defender );

                CellEmitter.get( defender.pos ).burst( LeafParticle.LEVEL_SPECIFIC, 10 );
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return FGREEN;
    }
}
