package game.main.gamelogic.world.utils;

/**
 * игровые настройки
 * Created by lgor on 25.01.14.
 */
public class GameProperties {

    /**
     * рисовать ли на карте границы ячеек.
     */
    public boolean renderBorders = false;

    /**
     * показывать ли количество кадров в секунду
     */
    public boolean showFPS = true;

    /**
     * game sleeps this time if nothing was changed instead of screen redrawing;
     */
    public long sleepingInsteadRender = 20L;
}
