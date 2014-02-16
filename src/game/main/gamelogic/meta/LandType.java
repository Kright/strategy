package game.main.gamelogic.meta;

import game.main.GUI.Sprite;

/**
 * Created by lgor on 31.12.13.
 * тип ландшафта
 * его параметры, возможные улучшения, возможно картинка или несколько (для разнообразия)
 */
public class LandType {

    public final Sprite sprite;
    public final int movingCost = 2;  //заглушка

    public LandType(Sprite sprite) {
        this.sprite = sprite;
    }
}
