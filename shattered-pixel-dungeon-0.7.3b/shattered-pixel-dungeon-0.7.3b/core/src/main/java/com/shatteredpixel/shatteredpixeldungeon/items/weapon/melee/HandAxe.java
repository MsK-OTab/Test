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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EarthImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class HandAxe extends MeleeWeapon {

	{
		image = ItemSpriteSheet.HAND_AXE;

		tier = 2;
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //12 base, down from 15
				lvl*(tier+1);
	}

	@Override
	public int proc( Char attacker, Char defender, int damage ) {

		cleaved.clear();
		cleave(attacker, defender);

		if (enchantment != null && attacker.buff(MagicImmune.class) == null) {
			damage = enchantment.proc( this, attacker, defender, damage );
		} //target enemy, enchantment procss

		//start attacking process on cleaved enemy
		for(Char cleavedEnemy : cleaved) {

			boolean visibleFight = Dungeon.level.heroFOV[attacker.pos] || Dungeon.level.heroFOV[cleavedEnemy.pos];

			if(Char.hit(attacker, cleavedEnemy, false)) {

				int effectiveDamageForCleaved = cleavedEnemy.defenseProc(attacker, (int) Math.round(damage));
				effectiveDamageForCleaved = Math.max(effectiveDamageForCleaved - cleavedEnemy.damageRoll(), 0);

				if (enchantment != null && attacker.buff(MagicImmune.class) == null) {
					effectiveDamageForCleaved = enchantment.proc( this, attacker, cleavedEnemy, effectiveDamageForCleaved );
				} //enchantment process for cleaved enemy

				cleavedEnemy.damage(effectiveDamageForCleaved, attacker);

				if (attacker.buff(FireImbue.class) != null)
					attacker.buff(FireImbue.class).proc(cleavedEnemy);
				if (attacker.buff(EarthImbue.class) != null)
					attacker.buff(EarthImbue.class).proc(cleavedEnemy);
				if (attacker.buff(FrostImbue.class) != null)
					attacker.buff(FrostImbue.class).proc(cleavedEnemy);

				cleavedEnemy.sprite.bloodBurstA( attacker.sprite.center(), effectiveDamageForCleaved );
				cleavedEnemy.sprite.flash();

				if (!cleavedEnemy.isAlive() && visibleFight) {
					if (cleavedEnemy == Dungeon.hero) {

						if (attacker == Dungeon.hero) {
							continue;
						}

						Dungeon.fail( getClass() );
						GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name)) );

					} else if (attacker == Dungeon.hero) {
						GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", cleavedEnemy.name)) );
					}
				}
			}


		}   //cleaving
		//cleaving end

		if (!levelKnown && attacker == Dungeon.hero && availableUsesToID >= 1) {
			availableUsesToID--;
			usesLeftToID--;
			if (usesLeftToID <= 0) {
				identify();
				GLog.p( Messages.get(Weapon.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}

		return damage;
	}

	private ArrayList<Char> cleaved = new ArrayList<>();

	public void cleave( Char attacker, Char defender) {
		PathFinder.buildDistanceMap( attacker.pos, BArray.not( Dungeon.level.solid, null ), RCH );

		int[] cleavingPosition = new int[PathFinder.NEIGHBOURS4.length];
		for(int i = 0; i < PathFinder.NEIGHBOURS4.length; i++) {
			cleavingPosition[i] = defender.pos + PathFinder.NEIGHBOURS4[i];
		} //define cleavingPosition

		for(int i = 0; i < PathFinder.NEIGHBOURS4.length; i++) {
			Char n = Actor.findChar(cleavingPosition[i]);
			if(n != null && n != attacker &&
					Dungeon.level.distance(attacker.pos, cleavingPosition[i]) <= this.RCH) {
				if(attacker.alignment == Char.Alignment.ALLY && n.alignment == Char.Alignment.ENEMY ) { cleaved.add(n);}
				else if(attacker.alignment == Char.Alignment.ENEMY && n.alignment != Char.Alignment.ENEMY) {cleaved.add(n);}
			}
		} //add cleaved enemy if exist & is in cleaving position and in reach of weapon

		//maybe can check attacker's buff(vertigo, berserk), and then add friendly mob so that can attack friendly mob
	}

}
