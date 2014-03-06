package game.main.utils;

import android.os.SystemClock;

import java.util.Random;

/**
 * Created by user on 03.03.14.
 */
final public class Randomiser {
    public Randomiser()
    {
        seed = (int)SystemClock.elapsedRealtime();
        seed %= m;
    }
    public Randomiser(int _seed)
    {
        seed = _seed;
    }

    public  float getFloat()
    {
        previous = seed;
        seed = (a*seed+b) % m;
        return (float)seed / (float)m;
    }
    public int getInt(){
        previous = seed;
        seed = (a*seed+b) % m;
        return seed ;
    }
    public int getInt(int n)
    {
        previous = seed;
        seed = (a*seed+b) % m;
        return (seed) % n;
    }

    public int getSeed(){
        return seed;
    }
    public void setSeed(int x){
        seed = x;
    }
    public void back(){
        seed = previous;
    }


    private int seed =  1;
    private int previous = seed;
    private int a=4081;
    private int b=25673;
    private int m=121500;

}
