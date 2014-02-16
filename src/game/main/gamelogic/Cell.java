package game.main.gamelogic;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.gamelogic.meta.LandType;
import game.main.gamelogic.meta.Settlement;

/**
 * Created by lgor on 31.12.13.
 */
public class Cell implements iRender {
    /*
    клетка карты, содержит всякую информацию - тип ландшафта, юнита, если он есть, улучшения и т.п.
     */
    public LandType land;

    //если никого нет - null
    Unit unit=null;
    Settlement settlement=null;
    //boolean road = false;

    @Override
    public void render(Canvas canv, Rect cell) {
        render(canv, cell, null);
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint paint) {
        canv.drawBitmap(land.sprite.bmp,land.sprite.rect, cell, paint);
        if (settlement!=null)
            settlement.render(canv, cell);
        if (unit!=null){
            unit.render(canv, cell);
        }
    }

    /**
     * отнимаемые очки перемещения
     */
    public int getMovindCost(){
        //TODO
        return 1;
    }

    /**
     * можно ди переместиться на эту клетку прямо сейчас (На ней нет юнитов и по ней можно ходить)
     */
    public boolean canMove(){
        //TODO
        return accessible() && true;
    }

    /**
     * проходима ли клетка в принципе (т.е, не гора и не река)
     */
    public boolean accessible(){
        //TODO
        return true;
    }
}