package game.main.utils;

import android.os.SystemClock;

/**
 * Created by user on 03.03.14.
 */
final public class Randomiser {
    public int rnd ()
    {
        previous = state;
        state = (a*state+b) % m;
        return state % 32768;
    }
    public void back()
    {
        state = previous;
    }


    public void changeParam(int aa,int bb, int mm)
    {
        this.a=aa;
        this.b=bb;
        this.m=mm;
    }
    private int state = (int)SystemClock.elapsedRealtime() % 100000;

    private int previous = state;
    private int a=4081;
    private int b=25673;
    private int m=121500;

}
