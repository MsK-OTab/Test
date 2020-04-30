package com.shatteredpixel.shatteredpixeldungeon.items.amulets;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.text.DecimalFormat;

public class AmuletOfDetection extends Amulet {

    public String statsInfo() {
        if (isIdentified()){
            return Messages.get(this, "stats", 5f*soloBonus(), Math.max( 0, (soloBonus()/2 )));
        } else {
            return Messages.get(this, "typical_stats", 5f*1, Math.max( 0, (1/2 )) );
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
