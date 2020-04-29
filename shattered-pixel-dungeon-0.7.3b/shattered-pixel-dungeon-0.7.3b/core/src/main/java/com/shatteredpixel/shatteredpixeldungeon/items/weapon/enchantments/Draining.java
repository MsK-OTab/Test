/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.King;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yog;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Draining extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing DARK_VIOLET = new ItemSprite.Glowing( 0x773377 );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {

		// lvl 0 - 10%
		// lvl 1 ~ 13%
		// lvl 2 ~ 16%
		float drainingPower = 2 + weapon.level();

		float rWeak;

		if (defender instanceof Mob) {

			rWeak = 1 + ((Mob) defender).EXP;

			if (defender instanceof Mimic || defender instanceof Statue) {
				rWeak = 1 + Dungeon.depth;
			} else if (defender instanceof Piranha || defender instanceof Bee) {
				rWeak = 1 + Dungeon.depth / 2f;
			} else if (defender instanceof Wraith) {
				//this is so low because wraiths are always at max hp
				rWeak = 0.5f + Dungeon.depth / 8f;
			} else if (defender instanceof Yog.BurningFist || defender instanceof Yog.RottingFist) {
				rWeak = 1 + 30;
			} else if (defender instanceof Yog.Larva || defender instanceof King.Undead) {
				rWeak = 1 + 5;
			} else if (defender instanceof Swarm) {
				//child swarms don't give exp, so we force this here.
				rWeak = 1 + 3;
			}
			rWeak *= 1 + 2 * Math.pow(defender.HP / (float) defender.HT, 2);
			if (drainingPower > rWeak) {
			Buff.prolong(defender, Weakness.class, 5f);
			Buff.prolong(attacker, Adrenaline.class, 3f);
			} else {
				float debuffChance = drainingPower / rWeak;
				if (Random.Float() < debuffChance) {
					Buff.prolong(defender, Weakness.class, 3f);
					Buff.prolong(attacker, Adrenaline.class, 1f);
				}
			}

		}

		if (defender == Dungeon.hero && Random.Int(2) == 0) {
			Buff.prolong(defender, Weakness.class, 5f);
			Buff.prolong(attacker, Adrenaline.class, 3f);
		}

		return damage;
	}


	@Override
	public ItemSprite.Glowing glowing() { return DARK_VIOLET; }
}
