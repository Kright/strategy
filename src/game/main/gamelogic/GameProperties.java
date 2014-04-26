package game.main.gamelogic;

import java.io.Serializable;

/**
 * игровые настройки
 * Created by lgor on 25.01.14.
 */
public class GameProperties implements Serializable{

    /**
     * рисовать ли на карте границы ячеек.
     */
    public boolean renderBorders = false;

    /**
     * если ничего не произошло, картинка на экране не перерисовывается
     */
    public boolean powerSaving = false;

    /**
     * показывать ли количество кадров в секунду
     */
    public boolean showFPS = true;
}
