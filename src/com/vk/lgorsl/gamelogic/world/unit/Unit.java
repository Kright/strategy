package com.vk.lgorsl.gamelogic.world.unit;

import com.vk.lgorsl.gamelogic.world.*;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.iRender;

/**
 * Created by lgor on 08.01.14.
 * игровой юнит. Его координаты можно узнать как координаты клетки, на которой он стоит
 */
public class Unit implements iRender {

    public final UnitType type;
    public final Country country;   //страна
    int movementPoints;     //очки перемещения
    int hitPoints;
    Cell cell;      //при перемещении надо обновлять
    UnitTask task = UnitTask.emptyTask;  //задача, которую делает юнит

    public Unit(Country country, UnitType type) {
        this.type = type;
        this.country = country;
        this.cell = Cell.getEmpty();
        hitPoints = type.hitPoints;
        movementPoints = type.movementPoints;
    }

    private Unit(Unit u) {
        this.type = u.type;
        this.country = u.country;
        this.movementPoints = u.movementPoints;
        this.hitPoints = u.hitPoints;
        this.cell = u.cell;
        this.task = u.task;
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
    }

    /**
     * called when new turn started
     */
    public void startNextTurn() {
        task = task.process(this);
    }

    public void setTask(UnitTask task) {
        this.task = task.process(this);
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
    public int upkeep() {
        return type.pay;
    }

    public boolean isFree() {
        return (task == UnitTask.emptyTask && hasMovementPoints());
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
     *
     * @param count - отнимаемое количество очков
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
    public void render(RenderParams params) {
        //flags[id].sprite.render(params);
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

    @Override
    public String toString() {
        return "type = " + type.name + ", position " + cell + ", movementPoints: " + getMovementPoints();
    }
}
