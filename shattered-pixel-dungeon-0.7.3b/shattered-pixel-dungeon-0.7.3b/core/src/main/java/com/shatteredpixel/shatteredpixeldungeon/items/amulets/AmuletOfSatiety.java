package com.shatteredpixel.shatteredpixeldungeon.items.amulets;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

import java.text.DecimalFormat;

public class AmuletOfSatiety extends Amulet {

    public String statsInfo() {
        if (isIdentified()){
            return Messages.get(this, "stats",soloBonus());
        } else {
            return Messages.get(this, "stats",1 );
        }
    }

    @Override
    public Item random() {
        //+1 100%
        int n = 1;
        level(n);

        if (Random.Float() < 0.3f) {
            cursed = true;
        }

        return this;
    }

    @Override
    protected AmuletBuff buff ( ) { return new Satiety(); }

    public static float digestionBonus (Char target) {
        return getBonus(target, Satiety.class);
    }

    public class Satiety extends Amulet.AmuletBuff{
    }
}
