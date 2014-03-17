package game.main.gamelogic.world;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Страна.
 * Понятие страны отделено от игрока - игрок будет отвечать за действия над государством, а этот класс - за его
 * состояние (юниты, развитие, производство, казна и т.п.)
 * Created by lgor on 12.03.14.
 */
public class Country {

    private final int id;
    protected final Map map;
    protected final World world;

    protected List<Unit> units = new ArrayList<Unit>();
    protected List<Settlement> settlements = new ArrayList<Settlement>();

    protected int gold; //казна

    public Country(World world, int id) {
        this.id = id;
        this.world = world;
        this.map = world.map;
        map.listsUnitsSettlements(this.id, units, settlements);
        Log.d("action", "Country constructor : " + settlements.size() + " settlements, " + units.size() + " units");
    }

    /**
     * вызывать вначале хода каждого госудаства
     * города что-нибудь производят, пополняется казна и т.п.
     */
    public void startNextTurn() {
        map.listsUnitsSettlements(this.id, units, settlements);
        for (Settlement settlement : settlements) {
            settlement.nextTurn();
            if (settlement.playerID == id){
                gold+=settlement.getTaxes();
            }
        }
    }

    /**
     * вызывать перед концом хода, тут лечатся юниты и т.п.
     * надо принудительно пробежать по карте, потому что во время хода могли появиться новые юниты,
     * например, при отмене игроком действия
     */
    public void beforeEndTurn() {
        for (Cell c : map) {
            if (c.hasUnit()) {
                Unit u = c.getUnit();
                if (u.countryID == this.id) {
                    u.endTurn();
                }
            }
        }
    }

    /**
     * добавляет в список всех юнитов своей страны с карты
     */
    protected void addAllUnits(List<Unit> units) {
        for (Cell c : map) {
            if (c.hasUnit()) {
                Unit u = c.getUnit();
                if (u.countryID == id) {
                    units.add(u);
                }
            }
        }
    }

    /**
     * добавляет в список все поселения
     */
    protected void addSettlements(List<Settlement> settlements) {
        for (Cell c : map) {
            if (c.settlement != null) {
                settlements.add(c.settlement);
            }
        }
    }

    public void createUnit(UnitType unitType, int x, int y){
        Action.addUnit(new Unit(unitType, id), map.getCell(x,y)).apply();
    }
}
