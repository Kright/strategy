package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.utils.Sprite;

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
    public final int movingCost;  //заглушка
    public final boolean accessable;
    protected final Sprite sprite;
    private iRender nextLayer;

    public LandType(Sprite sprite, int movingCost, String name) {
        this.sprite = sprite;
        this.name = name;
        this.movingCost = movingCost;
        this.accessable = (movingCost>0);
        this.nextLayer = iRender.NullRender.get();
    }

    public LandType(Sprite sprite, int movingCost, String name, final Sprite second, final float xC, final float yC){
        this(sprite, movingCost, name);
        this.nextLayer = new iRender() {
            private Rect rect=new Rect();

            @Override
            public void render(Canvas canv, Rect cell, Paint p) {
                rect.set(cell.left, cell.top + (int) (yC * cell.height()), cell.right + (int) (xC * cell.width()), cell.bottom);
                canv.drawBitmap(second.bmp, second.rect, rect, p);
            }
        };
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint p) {
        canv.drawBitmap(sprite.bmp, sprite.rect, cell, p);
    }

    public iRender nextLayer(){
        return nextLayer;
    }

    @Override
    public String toString() {
        return "name : "+name;
    }
}
