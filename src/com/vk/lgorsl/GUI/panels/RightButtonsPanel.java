package com.vk.lgorsl.GUI.panels;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.vk.lgorsl.GUI.MapCamera;
import com.vk.lgorsl.GUI.iRenderFeature;
import com.vk.lgorsl.utils.Touch;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.SpriteBank;
import com.vk.lgorsl.utils.sprites.iRender;

/**
 * панелька
 * "прижимается" к правому краю экрана, занимает всё пространство сверху донизу
 * Created by lgor on 18.05.14.
 */
public class RightButtonsPanel implements iRenderFeature {

    public enum ButtonState {
        empty,
        nextTurn,
        cancel,
        selectUnit
    }

    private final static int picW = 320, picH = 1080, btnH = 240;
    private final static float picRatio = (float) picW / picH;

    private int screenW, screenH, panelW;

    private final iRender pic;
    private final RenderParams params;
    private final Paint paint;

    private ButtonState[] buttonState = new ButtonState[]{
            ButtonState.nextTurn, ButtonState.cancel, ButtonState.selectUnit
    };
    private ButtonState lastPressed = ButtonState.empty;
    private int activeNumber = -1;

    private final Rect selection = new Rect();
    private final int selectionColor = 0x55000000;

    public RightButtonsPanel(SpriteBank spriteBank) {
        paint = new Paint();
        params = new RenderParams(paint);
        pic = spriteBank.getSprite("rightButtonsPanel");
    }

    public int getWidth() {
        return panelW;
    }

    /**
     * @return последнюю нажатую кнопку или ButtonState.empty в случае некорректного нажатия
     */
    public ButtonState getLastPressedButton() {
        return lastPressed;
    }

    public boolean onTouch(Touch touch) {
        if (touch.x < screenW - panelW) {
            activeNumber = -1;
            return false;
        }
        activeNumber = (int) (touch.y / screenH * picH) / btnH;
        if (activeNumber >= buttonState.length) {
            lastPressed = ButtonState.empty;
            activeNumber = -1;
            return true;
        }
        if (touch.lastTouch()) {
            lastPressed = buttonState[activeNumber];
            activeNumber = -1;
        }
        return true;
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        screenH = canvas.getHeight();
        screenW = canvas.getWidth();
        panelW = Math.round(screenH * picRatio);
        pic.render(getPicParams(canvas));
        if (activeNumber != -1) {
            int buttonH = (int) ((float) btnH / picH * screenH);
            selection.set(screenW - panelW, activeNumber * buttonH, screenW, (activeNumber + 1) * buttonH);
            paint.setColor(selectionColor);
            canvas.drawRect(selection, paint);
            paint.setColor(0xFF000000);
        }
    }

    private RenderParams getPicParams(Canvas canvas) {
        params.y = 0;
        params.x = screenW - panelW;
        params.setCellSize(panelW, screenH);
        params.canvas = canvas;
        return params;
    }
}
