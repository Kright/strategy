package game.main.gamelogic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.gamelogic.meta.Player;
import game.main.gamelogic.meta.Settlement;
import game.main.gamelogic.meta.UnitType;

/**
 * Created by lgor on 08.01.14.
 * игровой юнит. Его координаты можно узнать как координаты клетки, на которой он стоит
 */
public class Unit implements iRender {

    public final UnitType type;
    public final int playerID;

    private int movementPoints;     //очки перемещения
    private int hitPoints;          //
    private Settlement home;

    private Cell cell;      //при перемещении надо обновлять

    Unit(UnitType type, Player player) {
        this.type = type;
        playerID = player.id;
    }

    /**
     * Вызывается в конце каждого хода
     */
    public void endTurn() {
        if (hitPoints < getMaxHitPoints() && movementPoints == type.movementPoints && cell.getPlayerID() == playerID) {
            hitPoints++;
        }
        movementPoints = type.movementPoints;
        /*
        восполнение запасов хп, если на союзной территории и ничего не сделал за прошлый ход.
        строительство улучшений, если занят постройкой (рабочий например)
         */
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    /**
     * Этот метод используется вместо type.hitPoints, потому что юнит может быть улучшен - тогда хп будет больше
     */
    public int getMaxHitPoints() {
        return type.hitPoints;
    }

    /**
     * количество здоровья
     */
    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public void render(Canvas canv, Rect cell) {
        canv.drawBitmap(type.sprite.bmp, type.sprite.rect, cell, null);
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint paint) {
        canv.drawBitmap(type.sprite.bmp, type.sprite.rect, cell, null);
    }
}
