package com.vk.lgorsl.gamelogic.world;

import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.iRender;

import java.util.ArrayList;
import java.util.List;

/**
 * тип ландшафта
 * Created by lgor on 31.12.13.
 */
public class LandType implements iRender {
    /*
    тип ландшафта
        его параметры, возможные улучшения, возможно картинка, но хочется несколько для разнообразия
    */

    String name;
    public final int movingCost;
    public final boolean accessable;
    //  Список возможных для данного типа местности улучшений. Может быть пустым.
    public final List<LandUpgrade> landUpgrades = new ArrayList<LandUpgrade>();
    public final iRender sprite;
    private iRender nextLayer;

    public LandType(iRender sprite, int movingCost, String name) {
        this.sprite = sprite;
        this.name = name;
        this.movingCost = movingCost;
        this.accessable = (movingCost > 0);
        this.nextLayer = iRender.NullRender.get();
    }

    public LandType(iRender sprite, int movingCost, String name, final iRender second) {
        this(sprite, movingCost, name);
        this.nextLayer = second;
    }

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }

    /**
     * @return nextLayer - например, для клетки с лесом трава - sprite, nextLayer - деревья.
     */
    public iRender nextLayer() {
        return nextLayer;
    }

    @Override
    public String toString() {
        return "name : " + name;
    }
}
