package game.main.gamelogic.world.Actions;

import game.main.gamelogic.world.Action;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;

/**
 * добавление юнитана карту
 * Created by lgor on 17.03.14.
 */
public class AddUnit extends Action{

    private final Unit unit;
    private final Cell cell;

    public AddUnit(Unit unit, Cell cell){
        this.unit=unit;
        this.cell=cell;
    }

    @Override
    protected boolean doAction() {
        if (!cell.canMove(unit))
            return false;
        world.map.setUnit(unit, cell);
        return true;
    }

    @Override
    protected void cancel() {
        unit.getCell().setUnit(null);
    }
}
