package com.vk.lgorsl.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.vk.lgorsl.utils.Touch;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.SpriteBank;
import com.vk.lgorsl.utils.sprites.iRender;

import java.util.ArrayList;
import java.util.List;

/**
 * панелька
 * "прижимается" к правому краю экрана, занимает всё пространство сверху донизу
 * Created by lgor on 18.05.14.
 */
public class RightButtonsPanel implements iRenderFeature {

    private final static float picRatio = 320.0f / 1080;

    private int screenW, screenH, panelW;

    private iRender pic;
    private RenderParams params;

    private List<iClickable> buttons = new ArrayList<iClickable>();

    public RightButtonsPanel(SpriteBank spriteBank) {
        params = new RenderParams(new Paint());
        pic = spriteBank.getSprite("rightButtonsPanel");
    }

    private RenderParams getPicParams(Canvas canvas) {
        params.y = 0;
        params.x = screenW - panelW;
        params.setCellSize(panelW, screenH);
        params.canvas = canvas;
        return params;
    }

    public int getWidth() {
        return panelW;
    }

    public boolean interestedInTouch(Touch touch) {
        return touch.x > screenW - panelW;
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        screenH = canvas.getHeight();
        screenW = canvas.getWidth();
        panelW = Math.round(screenH * picRatio);

        pic.render(getPicParams(canvas));
    }
}
