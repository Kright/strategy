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
        return state;
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
    private int state = (int)SystemClock.elapsedRealtime();
    private int previous = state;
    private int a=17221;
    private int b=107839;
    private int m=510300;

}
