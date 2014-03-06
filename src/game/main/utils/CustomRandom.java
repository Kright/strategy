package game.main.utils;

import java.io.Serializable;

/**
 * для собственного генератора случайных чисел, так как в стандартном отсутствует возможность узнать его внутреннее
 * состояние, а оно пригодится - чтобы была возможность сохранять или откатывать назад.
 * внутреннее состояние целиком и полностью должно описываться одним числом long
 * Created by lgor on 04.03.14.
 */
public interface CustomRandom extends Serializable {

    /**
     * @return число, описывающее внутреннее состояние в данный момент
     */
    public int getSeed();

    /**
     * @param seed - установить внутреннее состояние
     */
    public void setSeed(int seed);

    /**
     * @param n
     * @return возвращает целое число в диапазоне [0..(n-1)]
     */
    public int get(int n);
}
