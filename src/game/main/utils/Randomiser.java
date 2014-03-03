package game.main.utils;

import android.os.SystemClock;

/**
 * Created by user on 03.03.14.
 */
final public class Randomiser {

    public  float getFloat()
    {
        float f = (float)rnd() / (float)m;
        return f;
    }
    public int getInt(int n){
        return rnd() % n;
    }
    private int rnd ()
    {
        previous = seed;
        seed = (a*seed+b) % m;
        return seed ;
    }
    public void back()
    {
        seed = previous;
    }

    public int getSeed(){
        return seed;
    }
    public void setSeed(int x){
        seed = x;
    }

    private int seed = (int)SystemClock.elapsedRealtime() % 100000;

    private int previous = seed;
    private int a=4081;
    private int b=25673;
    private int m=121500;

}
