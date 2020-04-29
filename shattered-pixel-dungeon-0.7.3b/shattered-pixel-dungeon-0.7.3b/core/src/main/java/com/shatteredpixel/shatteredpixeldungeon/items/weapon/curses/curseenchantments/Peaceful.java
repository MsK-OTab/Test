package com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.curseenchantments;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Peaceful extends Weapon.Enchantment {

    private static ItemSprite.Glowing PBLUE = new ItemSprite.Glowing( 0xAACCEE );;

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        if (defender.buff(Corruption.class) != null || !(defender instanceof Mob)) return damage;

        int level = Math.max( 0, weapon.level() );

        int wepDamRange = Math.max( 0, weapon.max() - weapon.min() );
        int dam = damage - weapon.min();

        // lvl 0 - 10%
        // lvl 1 ~ 13%
        // lvl 2 ~ 16%
        if ( Random.Int(weapon.max() - weapon.min() ) >= (damage - weapon.min()) ){

            Mob enemy = (Mob) defender;
            Hero hero = (attacker instanceof Hero) ? (Hero) attacker : Dungeon.hero;

            enemy.HP = enemy.HT;
            for (Buff buff : enemy.buffs()) {
                if (buff.type == Buff.buffType.NEGATIVE
                        && !(buff instanceof SoulMark)) {
                    buff.detach();
                } else if (buff instanceof PinCushion){
                    buff.detach();
                }
            }
            if (enemy.alignment == Char.Alignment.ENEMY){
                enemy.rollToDropLoot();
            }

            Buff.affect(enemy, Corruption.class);

            Statistics.enemiesSlain++;
            Badges.validateMonstersSlain();
            Statistics.qualifiedForNoKilling = false;
            if (enemy.EXP > 0 && hero.lvl <= enemy.maxLvl) {
                hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(enemy, "exp", enemy.EXP));
                hero.earnExp(enemy.EXP, enemy.getClass());
            } else {
                hero.earnExp(0, enemy.getClass());
            }

            return 0;
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PBLUE;
    }


}
