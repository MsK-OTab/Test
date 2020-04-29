package com.shatteredpixel.shatteredpixeldungeon.items.amulets;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

import static com.shatteredpixel.shatteredpixeldungeon.items.amulets.Amulet.getBonus;

public class AmuletOfBreathing extends Amulet {

    public String statsInfo() {
        if (isIdentified()){
            return Messages.get(this, "stats",soloBonus());
        } else {
            return Messages.get(this, "stats",1 );
        }
    }

    @Override
    protected Amulet.AmuletBuff buff( ) {
        return new Breathing();
    }

    public static int getBreathing ( Char target ) {
        return getBonus( target, Breathing.class );
    }

    public class Breathing extends Amulet.AmuletBuff {
    }
}
