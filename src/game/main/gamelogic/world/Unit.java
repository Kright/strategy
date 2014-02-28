package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;

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

    public Unit(UnitType type, Player player) {
        this.type = type;
        playerID = player.id;
        this.cell = Cell.getEmpty();
        hitPoints = type.hitPoints;
        movementPoints = type.movementPoints;
    }

    private Unit(Unit u) {
        this.type = u.type;
        this.playerID = u.playerID;
        this.movementPoints = u.getMovementPoints();
        this.hitPoints = u.hitPoints;
        this.home = u.home;
        this.cell = u.cell;
    }

    public Unit getClone() {
        return new Unit(this);
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

    public Cell getCell() {
        assert cell != null;
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
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

    /**
     * оставшиеся очки перемещения
     *
     * @return
     */
    public int getMovementPoints() {
        return movementPoints;
    }

    /**
     * @return movementPoints>0
     */
    public boolean hasMovementPoints() {
        return movementPoints > 0;
    }

    /**
     * отнимаются очки перемещения, если это возможно
     *
     * @param count
     */
    public void decreaseMovementPoints(int count) {
        if (movementPoints > count) {
            movementPoints -= count;
        } else {
            movementPoints = 0;
        }
    }

    public int getMaxMovementPoints() {
        return type.movementPoints;
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
