package game.main.utils;

import android.os.SystemClock;

import java.util.Random;

/**
 * Created by user on 03.03.14.
 */
final public class Randomiser {

    Random rand = new Random();
    Random rSeed = new Random();

    public Randomiser(long _seed, long _tmpS, long _previous, boolean _isBack )
    {
        seed = _seed;
        tmpS =_tmpS;
        previous = _previous;
        isBack = _isBack;
    }
    public Randomiser()
    {
        rSeed.setSeed(SystemClock.elapsedRealtime());
        seed = rSeed.nextLong();
        rnd();
    }
    public  float getFloat()
    {
        float x = rand.nextFloat();
        rnd();
        return x;
    }
    public int getInt(){

        int x = rand.nextInt();
        rnd();
        return x;
    }
    public int getInt(int n)
    {
        int x =  rand.nextInt(n);
        rnd();
        return x;
    }

    private void rnd ()
    {
        previous = seed;
        if (isBack)
        {
            seed = tmpS;
            isBack=false;
        }
        else
            seed = rSeed.nextLong();

        rand.setSeed(seed);
    }
    public void back()
    {
        tmpS = seed;
        seed = previous;
        isBack = true;
    }

    public long getSeed(){
        return seed;
    }
    public void setSeed(long x){
        seed = x;
    }

    private long seed;
    private long tmpS ;
    private long previous;
    private boolean isBack = false;

}
