package game.main.utils.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * параметры рисования, необходимые для рисования спрайта
 * Created by lgor on 07.04.14.
 */
public class RenderParams {

    public final Paint paint;

    public Canvas canvas;
    public int width, height;
    public int x, y;
    protected final Rect rect;

    public RenderParams(Paint paint) {
        this.paint = paint;
        this.rect = new Rect();
    }

    public void setCellSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
