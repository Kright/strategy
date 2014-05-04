package com.vk.lgorsl.gamelogic.world;

import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.iRender;

/**
 * улучшение местности (мельница, поле и т.п.)
 * Created by Michael-PC on 10.04.14.
 */
public class LandUpgrade implements iRender{

    public final String name;

    protected final iRender sprite;

    public LandUpgrade(iRender sprite, String name) {
        this.sprite = sprite;
        this.name = name;
    }

    public void render(RenderParams params) {
        sprite.render(params);
    }
}
