package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * То, что может рисоваться на карте в контексте игровой клетки (сама клетка, юнит, поселение и т.п.)
 * Created by lgor on 09.02.14.
 */
public interface iRender {

    /**
     * рисует себя на canvas, считая, что принадлежит клетке на карте, которая вписывается в Rect
     */
    public void render(Canvas canv, Rect cell, Paint p);

    /**
     * для тех неловких моментов, когда рисовать нечего, а проверять на null неохота
     */
    public class NullRender implements iRender{
        @Override
        public void render(Canvas canv, Rect cell, Paint p) {}

        private final static iRender nullRender = new NullRender();

        public static iRender get(){
            return nullRender;
        }
    }
}
