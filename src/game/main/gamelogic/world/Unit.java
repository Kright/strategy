package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.iRender;

import java.io.Serializable;

/**
 * Created by lgor on 08.01.14.
 * игровой юнит. Его координаты можно узнать как координаты клетки, на которой он стоит
 */
public class Unit implements iRender , Serializable{

    public final UnitType type;
    public final Country country;   //страна
    private int movementPoints;     //очки перемещения
    private int hitPoints;
    private Settlement home;
    private Cell cell;      //при перемещении надо обновлять

    Unit(Country country, UnitType type) {
        this.type = type;
        this.country = country;
        this.cell = Cell.getEmpty();
        hitPoints = type.hitPoints;
        movementPoints = type.movementPoints;
    }

    private Unit(Unit u) {
        this.type = u.type;
        this.country = u.country;
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
        if (hitPoints < getMaxHitPoints() && movementPoints == type.movementPoints) {
            hitPoints++;
        }
        movementPoints = type.movementPoints;
        /*
        восполнение запасов хп, если ничего не сделал за прошлый ход.
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
     * @return unitType.pay: стоимость содержания юнита
     */
    public int upkeep(){
        return type.pay;
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
     * @return оставшиеся очки перемещения
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
     * @param count - отнимаемое количество очков
     */
    public void decreaseMovementPoints(int count) {
        if (movementPoints > count) {
            movementPoints -= count;
        }else{
        movementPoints = 0;
        }
     }


    public int getMaxMovementPoints() {
        return type.movementPoints;
    }

    @Override
    public void render(RenderParams params) {
        type.sprite.render(params);
    }

    /**
     * @return Action which adds Castle on map
     */
    public Action buildCastle() {
        return new Action() {
            @Override
            protected boolean doAction() {
                if (cell.hasSettlement())
                    return false;
                cell.setSettlement(new Castle(country, cell));
                return true;
            }

            @Override
            protected void cancel() {
                cell.setSettlement(null);
            }
        };
    }
}
