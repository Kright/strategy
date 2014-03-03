package game.main.utils;

import android.os.SystemClock;

import java.util.Random;

/**
 * Created by user on 03.03.14.
 */
final public class Randomiser {
    Random rand = new Random();
    Random rSeed = new Random();
    Randomiser()
    {
        rSeed.setSeed(SystemClock.elapsedRealtime());
        rnd();
    }
    public  float getFloat()
    {
        return rand.nextFloat();
    }
    public int getInt(){

        return rand.nextInt();
    }
    public int getInt(int n)
    {
        return rand.nextInt(n);
    }

    private void rnd ()
    {
        if (isBack)
        {
            seed = tmpS;
            isBack=false;
        }
        else
            seed = rSeed.nextLong();

        previous = seed;
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
