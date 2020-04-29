package com.shatteredpixel.shatteredpixeldungeon.items.amulets;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.text.DecimalFormat;

public class AmuletOfDetection extends Amulet {

    public String statsInfo() {
        if (isIdentified()){
            return Messages.get(this, "stats", 5*soloBonus(), soloBonus()<0 ? 0 : (int)soloBonus()/2 );
        } else {
            return Messages.get(this, "stats", 5*1 );
        }
    }

    @Override
    protected AmuletBuff buff( ) {
        return new Detection();
    }

    public static int detectionBonus ( Char target ) {
        return getBonus( target, Detection.class );
    }

    public class Detection extends AmuletBuff{
    }
}
