package com.vk.lgorsl.gamelogic.world;

import com.vk.lgorsl.openGL.DrawingContext;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.SpriteBank;
import com.vk.lgorsl.utils.sprites.iRender;

/**
 * поселение
 * Наследуются - замок, деревушка, город
 * Created by lgor on 25.01.14.
 */
public abstract class Settlement implements iRender {

    public static iRender shadow;

    protected Country country;
    public final Cell cell;

    public Settlement(Country country, Cell cell) {
        this.country = country;
        this.cell = cell;
    }

    /**
     * удаление поселения
     */
    abstract public void removeSettlement();

    /**
     * called once per turn
     *
     * @return taxes from this settlement, may be positive or 0 or negative (if unprofitable)
     */
    public abstract int getTaxes();

    /**
     * called after start of new turn
     */
    public abstract void nextTurn();

    public abstract void render(RenderParams params);

    public static void init(SpriteBank sprites) {
        Village.sprite = sprites.getSprite("village");
        Castle.sprite = sprites.getSprite("castle");
        shadow = sprites.getSprite("shadow");
    }

    public static void initGL(DrawingContext context){
        Village.sprite = context.getSprite("village");
        Castle.sprite = context.getSprite("castle");
        shadow = context.getSprite("shadow");
    }
}
