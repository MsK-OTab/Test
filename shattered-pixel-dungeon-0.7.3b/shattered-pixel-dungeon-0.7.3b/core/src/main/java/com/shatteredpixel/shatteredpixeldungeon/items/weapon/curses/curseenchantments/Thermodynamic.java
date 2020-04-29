package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.curseenchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Thermodynamic extends Weapon.Enchantment {

    private static ItemSprite.Glowing TORANGE = new ItemSprite.Glowing( 0xBB8844 );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {

        int level = Math.max( 0, weapon.level() );

        if ( Random.Int( level + 3 ) >= 2  ) {

            if( defender.buff(Chill.class) != null || defender.buff(Frost.class) != null ) {
                Buff.prolong(attacker, FrostImbue.class, 3);
                attacker.sprite.emitter().burst( SnowParticle.FACTORY, 5 );

            } else if ( defender.buff(Burning.class) != null ) {
                Buff.append(attacker, FireImbue.class ).set( 3 );
                attacker.sprite.emitter().burst( FlameParticle.FACTORY, 5 );

            } else {
                if( Random.Int(2) == 0 ) {
                    Buff.prolong(attacker, FrostImbue.class, 1 );
                    attacker.sprite.emitter().burst( SnowParticle.FACTORY, 3 );

                    Buff.prolong(defender, Frost.class, Frost.duration(defender) * Random.Float(0.5f, 1f));
                    CellEmitter.get(defender.pos).start(SnowParticle.FACTORY, 0.2f, 6);
                } else {
                    Buff.append(attacker, FireImbue.class ).set( 1 );
                    attacker.sprite.emitter().burst( FlameParticle.FACTORY, 5 );

                    Buff.affect( defender, Burning.class ).reignite( attacker );
                    defender.sprite.emitter().burst( FlameParticle.FACTORY, 5 );
                }
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return TORANGE;
    }
}
