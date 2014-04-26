package game.main.utils.sprites;

import java.io.Serializable;

/**
 * Обычно это используется для тех элементов, которые рисуются в контексте клетки карты.
 * Таким образом, из параметров меняются только x,y
 * Created by lgor on 09.02.14.
 */
public interface iRender  extends Serializable{

    /**
     * рисует себя на canvas, считая, что принадлежит клетке на карте, которая вписывается в Rect
     */
    public void render(RenderParams params);

    /**
     * для тех неловких моментов, когда рисовать нечего, а проверять на null неохота
     */
    public class NullRender implements iRender, Serializable{
        @Override
        public void render(RenderParams params) {}

        private final static iRender nullRender = new NullRender();

        public static iRender get(){
            return nullRender;
        }
    }
}
