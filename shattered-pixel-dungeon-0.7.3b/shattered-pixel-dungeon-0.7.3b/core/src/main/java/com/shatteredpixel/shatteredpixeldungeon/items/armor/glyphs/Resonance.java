package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class Resonance extends Armor.Glyph {
    ItemSprite.Glowing ROSEPINK = new ItemSprite.Glowing( 0xCC88CC );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.level());

        float defHpRate = (float)defender.HP/(float)defender.HT;
        float rangeRate = GameMath.gate(0, (float)(2/(15-Math.sqrt(1+8*level))),1);
        float downRange = Math.max( 0, defHpRate - rangeRate );
        float upRange = Math.min( defHpRate + rangeRate, 1 );

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Dungeon.level.heroFOV[mob.pos]) {
                float mobHpRate = (float)mob.HP/(float)mob.HT;
                if( mobHpRate < upRange && mobHpRate > downRange ) {
                    mob.damage( damage, attacker );
                }

            }
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ROSEPINK;
    }
}
