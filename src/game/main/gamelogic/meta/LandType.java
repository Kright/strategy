package game.main.gamelogic.meta;

import game.main.GUI.Sprite;

/**
 * Created by lgor on 31.12.13.
 */
public class LandType {
    /*
    тип ландшафта
        его параметры, возможные улучшения, возможно картинка, но хочется несколько для разнообразия
    */

    public final Sprite sprite;

    public LandType(Sprite sprite){
        this.sprite=sprite;
    }
}
