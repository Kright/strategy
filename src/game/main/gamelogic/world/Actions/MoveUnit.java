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
        if (way.size() <= 1) {
            throw new IllegalArgumentException("way have only " + way.size() + " cells, there mast be >1 !");
        }
        this.way = way;
        this.unit = unit;
        this.savedUnit = unit.getClone();
        finish = way.get(way.size() - 1);
        assert unit.getCell() == way.get(0) && way.size() > 1 && !finish.hasUnit();
    }

    @Override
    protected boolean doAction() {
        for (int i = 1; i < way.size(); i++) {
            if (unit.hasMovementPoints()) {
                unit.decreaseMovementPoints(way.get(i).getMovindCost());
            } else {    //если у юнита нет очков движения, нам подсунули какое-то левое действие, и мы его не произведём
                cancel();
                throw new RuntimeException("Way is wrong!");
                //return false;
            }
        }
        world.map.setUnit(unit, finish);
        return true;
    }

    @Override
    protected void cancel() {
        finish.setUnit(null);
        world.map.setUnit(savedUnit, way.get(0));
    }

    @Override
    public String toString() {
        return "moving unit from (" + way.get(0).x + "," + way.get(0).y + ") to (" + finish.x + "," + finish.y + ")";
    }
}
