package game.main.utils;

/**
 * Created by lgor on 07.03.14.
 * Внаглую копировать исходный код Java.util.Random как-то совестно, потому что тот выложен под GPL, а мой - не пойми
 * под чем.
 * Так что тут просто абстрактная реализация линейного конгруэнтного метода.
 */
public abstract class LinearCongruentialGenerator implements CustomRandom {

    protected final long a, c, m;
    protected long seed;

    private LinearCongruentialGenerator(long a, long c, long m) {
        this.a = a;
        this.c = c;
        this.m = m;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed % m;
    }

    /**
     * фактически, самая важная часть генератора чисел - здесь вычисляются следующие значения
     *
     * @param bits
     * @return число от 0 до 2^bits-1
     */
    abstract protected int next(int bits);

    @Override
    public int get(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");
        if ((n & -n) == n) // if n power of 2. Офигенный способ)
            return (int) ((n * (long) next(31)) >> 31);
        int rand;
        int max = (int) ((0x80000000L / n) * n - 1); //вероятность выпадения всех значений должна быть одинаковой
        do {
            rand = next(31);
        } while (rand > max);
        return rand % n;
    }

    /**
     * @return версия генератора с чиселками из util.Random
     * местами сделано по-своему, за идентичность не ручаюсь.
     */
    public static LinearCongruentialGenerator getLikeNativeRandom() {
        return new LinearCongruentialGenerator(25214903917L, 11, 1L << 48) {
            {
                seed = (long) (Math.random() * 1000000d);
            }

            @Override
            protected int next(int bits) {
                seed = (a * seed + c) & 0xFfffFfffFfffL;  //числозависимая штука, но при (...) % m появляются отрицительные числа
                return (int) (seed >>> (48 - bits));
            }
        };
    }
}
