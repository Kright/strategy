package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lgor on 09.02.14.
 */
public interface iRender {

    /**
     *рисует себя на canvas, считая, что принадлежит клетке на карте, которая вписывается в Rect
     */
    public void render(Canvas canv, Rect cell);

    public void render(Canvas canv, Rect cell, Paint p);
}
