package game.main.gamelogic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.gamelogic.meta.Settlement;
import game.main.gamelogic.meta.UnitType;

/**
 * Created by lgor on 08.01.14.
 */
public class Unit implements iRender{

    public final UnitType type;

    //Point position;
    private int movementPoints;     //очки перемещения
    private int hitPoints;
    private Settlement home;

    private int x,y;

    Unit(UnitType type){
        this.type=type;
    }

    /**
     * вызывать вначале каждого хода
     */
    public void nextTurn(){
        movementPoints=type.movementPoints;
        if (hitPoints<type.hitPoints){
            hitPoints++;
        }
        /*
        восполнение запасов хп, если на союзной территории
        перемещение если есть задание на несколько ходов вперёд
        строительство улучшений, если занят постройкой (рабочий например)
         */
    }

    @Override
    public void render(Canvas canv, Rect cell) {
        canv.drawBitmap(type.sprite.bmp,type.sprite.rect,cell,null);
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint paint) {
        canv.drawBitmap(type.sprite.bmp,type.sprite.rect,cell,null);
    }
}
