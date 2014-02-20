package game.main.gamelogic.world;

import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgor on 09.02.14.
 */
public abstract class Player {

    protected final World world;

    /**
     * игрок с id==0 - тестовый, которому доступно всё - видит всю карту и т.п.
     */
    public final int id;

    protected final List<Unit> units = new ArrayList<Unit>();
    protected final List<Settlement> settlements = new ArrayList<Settlement>();

    public Player(World world, int id) {
        this.world = world;
        this.id = id;
    }

    /*
    обновить состояние городов, казны и т.п.
    вызывается до того, как будет вызван update
     */
    public void nextStep() {
        world.map.listsUnitsSettlements(this, units, settlements);
        for (Settlement settlement : settlements) {
            settlement.nextTurn();
        }
        //may be gold -= units.size();
        //TODO
    }

    /*
    вызывается после update==false
    подлечить юнитов, восполнить очки ходов и т.п.
     */
    public void theEnd() {
        for (Unit unit : units) {
            unit.endTurn();
        }
    }

    /*
    вызывается до тех пор, пока не вернёт fasle.
    между вызовами мир рисуется на экран
     */
    public abstract boolean update(MapCamera camera, Touch[] touches, List<iRenderFeature> features);
}
