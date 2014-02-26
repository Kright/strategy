package game.main.gamelogic.world.Actions;

import game.main.gamelogic.world.Action;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;

import java.util.List;

/**
 * перемещение юнита по пути из клеток за один ход
 * Created by lgor on 26.02.14.
 */
public class MoveUnit extends Action {

    private final List<Cell> way;
    private final Unit unit, savedUnit;
    private final Cell finish;

    public MoveUnit(Unit unit, List<Cell> way) {
        this.way = way;
        this.unit = unit;
        this.savedUnit = unit.getClone();
        finish = way.get(way.size() - 1);
        assert unit.getCell() == way.get(0) && way.size() > 1 && !finish.hasUnit();
    }

    @Override
    protected boolean doAction() {
        for (int i = 1; i < way.size(); i++) {
            assert unit.hasMovementPoints();    //если нет, то это какое-то левое перемещение.
            // Но assert почему-то не работает, и это печально
            unit.decreaseMovementPoints(way.get(i).getMovindCost());
        }
        world.map.setUnit(unit, finish);
        return true;
    }

    @Override
    protected void cancel() {
        finish.setUnit(null);
        world.map.setUnit(savedUnit, way.get(0));
    }
}