package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import game.main.gamelogic.Gamer;
import game.main.gamelogic.world.Action;

/**
 * кнопочки с текстом внутри, которые можно одноразово нажимать
 * Created by lgor on 10.03.14.
 */
public abstract class TextButton extends Button {

    private Paint p = new Paint();

    protected String text;
    private Paint paint;

    private int dx, dy;

    private TextButton(String text, float textSize) {
        this.text = text;
        paint = new Paint();
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int delta = bounds.height() / 2;
        width = bounds.width() + 2 * delta;
        height = bounds.height() + 2 * delta;
        dx = -bounds.left + delta;
        dy = -bounds.top + delta;
    }

    @Override
    public abstract void click();

    @Override
    public void render(Canvas canv, int x, int y, boolean isPressed) {
        canv.drawText(text, x + dx, y + dy, paint);
        if (isPressed) {
            paint.setColor(0x44000000);
            canv.drawRect(x, y, x + width, y + height, paint);
            paint.setColor(0xFF000000);
        }
    }

    public static Button getCancelButton(float textSize, final Gamer gamer) {
        return new TextButton("cancel", textSize) {
            @Override
            public void click() {
                Log.d("action", "button \"" + text + "\" is clicked");
                Action.getCancelAction().apply();
                gamer.cancelAction();
            }
        };
    }

    public static Button getNextTurnButton(float textSize, final Gamer gamer) {
        return new TextButton("next turn", textSize) {
            @Override
            public void click() {
                Log.d("action", "button \"" + text + "\" is clicked");
                gamer.setNextTurnReady();
            }
        };
    }
}
