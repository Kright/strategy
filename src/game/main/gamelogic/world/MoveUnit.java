package game.main.gamelogic.world;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * перемещение юнита по пути из клеток за один ход
 * Created by lgor on 26.02.14.
 */
public class MoveUnit extends Action implements Serializable{

    private final List<Cell> way;
    private final Unit unit, savedUnit;
    private final Cell finish;
    private Set<Cell> nearest = new HashSet<Cell>();

    public MoveUnit(Unit unit, List<Cell> way) {
        this.way = way;
        this.unit = unit;
        this.savedUnit = unit.getClone();
        if (way.size() <= 1) {
            throw new IllegalArgumentException("way have cells,length == " + way.size() + ", cells == {" + getSequence() +
                    "} , there must be >1 !");
        }
        finish = way.get(way.size() - 1);
        assert unit.getCell() == way.get(0) && way.size() > 1 && !finish.hasUnit();
    }

    @Override
    protected boolean doAction() {
        unit.country.map.addCellsNear(nearest, unit.getCell().x, unit.getCell().y);
        for (int i = 1; i < way.size(); i++) {
            if (unit.hasMovementPoints()) {
                Cell c = way.get(i);
                unit.decreaseMovementPoints(c.getMovindCost());
                unit.country.map.openСellsNear(c.x, c.y);
                unit.country.map.addCellsNear(nearest, c.x, c.y);
            } else {
                /* если у юнита нет очков движения, нам подсунули какое-то левое действие, и мы его не произведём
                 *
                 * но, в принципе, такая ситуация возможна - например, мы идём на "затенённую" клетку, а потом
                 * оказывается, что что-то изменилось - например, пропала дорога или стоит мешающий юнит другой страны.
                 * в идеале надо будет делать действие до того момента, как мы обнаружим подвох, а потом остановиться
                 * и пусть игрок делает новое действие, исходя из новой информации.
                 *
                 * В будущем надо будет переделать реализацию и не кидаться исключениями.
                 */
                cancel();
                throw new RuntimeException("way is wrong, " + getSequence());
            }
        }
        world.map.setUnit(unit, finish.x, finish.y);
        checkShadows();
        return true;
    }

    void checkShadows() {
        for (Cell c : nearest) {
            unit.country.map.checkShadows(c.x, c.y);
        }
    }

    @Override
    protected void cancel() {
        finish.setUnit(null);
        world.map.setUnit(savedUnit, way.get(0).x, way.get(0).y);
        checkShadows();
    }

    private String getSequence() {
        String s = "";
        for (Cell c : way) {
            s += "(" + c.x + "," + c.y + "), ";
        }
        return s;
    }

    @Override
    public String toString() {
        return "moving unit from (" + way.get(0).x + "," + way.get(0).y + ") to (" + finish.x + "," + finish.y + ")";
    }
}
