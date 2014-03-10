package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import game.main.gamelogic.Gamer;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * Вертикальная панель слева или справа экрана
 * Содержит кнопочки "next turn", "cancel", "help" и панельку с доп информацией;
 * "меню" вызывается стандартной кнопкой андроида "назад", её тут не будет
 * Created by lgor on 10.03.14.
 */
public class GamePanel extends ActiveArea {

    private int x = 0, width = 0;
    private List<Button> buttons;
    private boolean rightSide;
    private Paint paint = new Paint();

    {
        paint.setColor(0xFFAAAAAA);
    }

    private GamePanel(List<Button> buttons, boolean isRightSide) {
        this.buttons = buttons;
        this.rightSide = isRightSide;
        for (Button b : buttons) {
            width = Math.max(width, b.getWidth());
        }
    }

    @Override
    public boolean interestedInTouch(Touch touch) {
        return touch.x >= x && touch.x <= x + width;
    }

    private Button activeButton = null;

    @Override
    public void update(Touch touch) {
        if (!interestedInTouch(touch)) {
            activeButton = null;
            return;
        }
        int dy = 0;
        if (touch.firstTouch())
            Log.d("action", "Game panel touched");
        for (Button b : buttons) {
            if (b.isInto(touch.x - x, touch.y - dy)) {
                activeButton = b;
                if (touch.firstTouch()) {
                    b.click();
                }
            }
            dy += b.getHeight();
        }
        if (touch.lastTouch()) {
            activeButton = null;
        }
    }

    /**
     * @param isRightSide false == left side, right == right side of view;
     */
    public void setSide(boolean isRightSide) {
        this.rightSide = isRightSide;
    }

    /*
     * для того, чтобы карта рисовалась не навсё экране, т.к., поверх будут рисоваться кнопочки
     */
    public int getFreeLeft() {
        return rightSide ? 0 : width;
    }

    public int getFreeRight(int canvasWidth) {
        return rightSide ? canvasWidth - width : canvasWidth;
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        if (rightSide) {
            x = canvas.getWidth() - width;
        }
        int dy = 0;
        canvas.drawRect(x, 0, x + width, camera.getScreenHeight(), paint);
        for (Button b : buttons) {
            b.render(canvas, x, dy, activeButton == b);
            dy += b.getHeight();
        }
    }

    public static GamePanel getGamePanel(Gamer gamer, float textSize) {
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(TextButton.getNextTurnButton(textSize, gamer));
        buttons.add(TextButton.getCancelButton(textSize, gamer));
        return new GamePanel(buttons, true);
    }
}
