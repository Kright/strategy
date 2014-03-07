package game.main.utils;

/**
 * Created by user on 03.03.14.
 */
final public class Randomiser implements CustomRandom {
    public Randomiser()
    {
        seed = (int)(Math.random()* m);
    }
    public Randomiser(int _seed)
    {
        seed = _seed;
    }

    public float getFloat() {
        previous = seed;
        seed = (a*seed+b) % m;
        return (float)seed / (float)m;
    }
    public int get(){
        previous = seed;
        seed = (a*seed+b) % m;
        return seed ;
    }
    public int get(int n)
    {
        previous = seed;
        seed = (a*seed+b) % m;
        return (seed) % n;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long x) {
        seed = (int) x;
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
