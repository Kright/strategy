package com.vk.lgorsl.GUI;

import android.graphics.Canvas;
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

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        rightButtonsPanel.render(camera, canvas);
    }
}
