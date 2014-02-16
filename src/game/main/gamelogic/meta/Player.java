package game.main.gamelogic.meta;

import game.main.GUI.MapCamera;
import game.main.gamelogic.Unit;
import game.main.gamelogic.World;
import game.main.input.Touch;

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

    protected List<Unit> units = new ArrayList<Unit>();
    protected List<Settlement> settlements = new ArrayList<Settlement>();

    public Player(World world, int id){
        this.world=world;
        this.id=id;
    }

    /*
    обновить состояние городов, казны и т.п.
    вызывается до того, как будет вызван update
     */
    public void nextStep(){
        world.map.listsUnitsSettlements(this, units, settlements);
        for (Unit unit : units) {
            unit.nextTurn();
        }
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
    public void theEnd(){
        //TODO
    }

    /*
    вызывается до тех пор, пока не вернёт fasle.
    между вызовами мир рисуется на экран
     */
    public abstract boolean update(MapCamera camera, Touch[] touches);
}
