package game.main.gamelogic.world;

import game.main.utils.Sprite;

/**
 * Created by lgor on 31.12.13.
 */
public class LandType {
    /*
    тип ландшафта
        его параметры, возможные улучшения, возможно картинка, но хочется несколько для разнообразия
    */

    public final int movingCost = 2;  //заглушка
    public final Sprite sprite;

    public LandType(Sprite sprite) {
        this.sprite = sprite;
    }
}
