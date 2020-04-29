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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Shortsword extends MeleeWeapon {

	{
		image = ItemSpriteSheet.SHORTSWORD;

		tier = 2;
	}

	@Override
	public int min(int lvl) {
		if(Dungeon.hero.belongings.weapon == this) {
			return tier + lvl + (Dungeon.hero.lvl)/3 - 1;
		}
		return  tier +  //base
				lvl-1;  //level scaling

	}

	@Override
	public int max(int lvl) {
		if(Dungeon.hero.belongings.weapon == this) {
			return (lvl+1)*(tier+1) +
					Dungeon.hero.lvl/3;
		}
		return  (tier+1) +    //base
				lvl*(tier+1);   //level scaling
	}
}