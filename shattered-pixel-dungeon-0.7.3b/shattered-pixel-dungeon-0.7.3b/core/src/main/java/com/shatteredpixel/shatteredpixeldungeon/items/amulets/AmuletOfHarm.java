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

package com.shatteredpixel.shatteredpixeldungeon.items.amulets;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import java.text.DecimalFormat;

public class AmuletOfHarm extends Amulet {
	
	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", 20f*soloBonus() + 10f, 10f*soloBonus()+10f);
		} else {
			return Messages.get(this, "stats", 20f*1 + 10f, 10f*1 +10f );
		}
	}

	@Override
	protected AmuletBuff buff( ) {
		return new Harm();
	}
	
	public static float damageGiveMultiplier( Char t ){
		return 0.2f*getBonus( t, Harm.class ) + 1.1f;
	}

	public static float damageTakeMultiplier( Char t ){
		return 0.1f*Math.abs( getBonus( t, Harm.class )) + 1.1f;
	}

	public int soloBonus(){
		if (cursed){
			return this.level() - 3;
		} else {
			return this.level();
		}
	}

	public static int drBonus (Char target) {return ( 2 + getBonus( target, Harm.class) ) / 2 ;}

	public class Harm extends AmuletBuff {
	}
}

