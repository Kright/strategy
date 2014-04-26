package game.main.gamelogic.world;

import android.graphics.Rect;
import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.iRender;
import game.main.utils.sprites.Sprite;

import java.io.Serializable;

/**
 * тип ландшафта
 * Created by lgor on 31.12.13.
 */
public class LandType implements iRender, Serializable {
    /*
    тип ландшафта
        его параметры, возможные улучшения, возможно картинка, но хочется несколько для разнообразия
    */

    String name;
    public final int movingCost;  //заглушка
    public final boolean accessable;
    transient protected final Sprite sprite;
    transient private iRender nextLayer;

    public LandType(Sprite sprite, int movingCost, String name) {
        this.sprite = sprite;
        this.name = name;
        this.movingCost = movingCost;
        this.accessable = (movingCost>0);
        this.nextLayer = iRender.NullRender.get();
    }

    public LandType(Sprite sprite, int movingCost, String name, final Sprite second){

        this(sprite, movingCost, name);
        this.nextLayer = new iRender() {
            @Override
            public void render(RenderParams params) {
                Rect r = new Rect();
                second.render(params);
            }
        };
    }

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }

    public iRender nextLayer(){
        return nextLayer;
    }

    @Override
    public String toString() {
        return "name : "+name;
    }
}
