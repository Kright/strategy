package com.vk.lgorsl.GUI.panels;

import android.graphics.Canvas;
import com.vk.lgorsl.GUI.MapCamera;
import com.vk.lgorsl.GUI.iRenderFeature;
import com.vk.lgorsl.utils.Touch;
import com.vk.lgorsl.utils.sprites.SpriteBank;

/**
 * контейнер для GUI элементов
 * Created by lgor on 18.05.14.
 */
public class PanelsGUI implements iRenderFeature {

    public final RightButtonsPanel rightButtonsPanel;

    public PanelsGUI(SpriteBank spriteBank) {
        rightButtonsPanel = new RightButtonsPanel(spriteBank);
    }

    public boolean onTouch(Touch t) {
        return rightButtonsPanel.onTouch(t);
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        rightButtonsPanel.render(camera, canvas);
    }
}
