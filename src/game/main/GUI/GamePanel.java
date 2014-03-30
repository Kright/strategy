package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import game.main.gamelogic.Gamer;
import game.main.gamelogic.world.Action;
import game.main.utils.Sprite;
import game.main.utils.SpriteBank;
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

    protected int x = 0, width = 0;
    protected List<Button> buttons;
    private boolean rightSide;
    protected Button activeButton = null;
    protected Paint paint = new Paint();

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
     * для того, чтобы карта рисовалась не на всём экране, т.к., поверх будут рисоваться кнопочки
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

    //панель-картинка
    public static GamePanel getGamePanel2(final Gamer gamer, final SpriteBank sprites) {
        final Paint paint = new Paint();
        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(new Button() {
            @Override
            public void click() {
                Log.d("action", "button \"next turn\" is clicked");
                gamer.setNextTurnReady();
            }

            @Override
            public void render(Canvas canv, int x, int y, boolean isPressed) {
                if (isPressed) {
                    paint.setColor(0x44000000);
                    canv.drawRect(x, y, x + width, y + height, paint);
                    paint.setColor(0xFF000000);
                }
            }
        });
        buttons.add(new Button() {
            @Override
            public void click() {
                Log.d("action", "button \"cancel\" is clicked");
                Action.getCancelAction().apply();
                gamer.cancelAction();
            }

            @Override
            public void render(Canvas canv, int x, int y, boolean isPressed) {
                if (isPressed) {
                    paint.setColor(0x44000000);
                    canv.drawRect(x, y, x + width, y + height, paint);
                    paint.setColor(0xFF000000);
                }
            }
        });

        return new GamePanel(buttons, true) {
            private Rect result = new Rect();
            Sprite panel = sprites.get("game panel");

            @Override
            public void render(MapCamera camera, Canvas canvas) {
                float scale = camera.getScreenHeight() / panel.rect.height();
                width = (int) (panel.rect.width() * scale);
                x = canvas.getWidth() - width;
                result.set(x, 0, x + width, camera.getScreenHeight());
                for (Button b : buttons) {
                    b.setSize(width, (int) (scale * 240));
                }
                canvas.drawBitmap(panel.bmp, panel.rect, result, paint);
                int dy = 0;
                for (Button b : buttons) {
                    b.render(canvas, x, dy, activeButton == b);
                    dy += b.getHeight();
                }
            }
        };
    }
}
