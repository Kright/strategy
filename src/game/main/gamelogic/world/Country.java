package game.main.gamelogic.world;

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

    protected List<Unit> units = new ArrayList<Unit>();
    protected List<Settlement> settlements = new ArrayList<Settlement>();

    public Country(Map map, int id) {
        this.id = id;
        this.map = map;
    }

    /**
     * вызывать вначале хода каждого госудаства
     * города что-нибудь производят, пополняется казна и т.п.
     */
    public void startNextTurn() {
        map.listsUnitsSettlements(this.id, units, settlements);
        for (Settlement settlement : settlements) {
            settlement.nextTurn();
        }
    }

    /**
     * вызывать перед концом хода, тут лечатся юниты и т.п.
     */
    public void beforeEndTurn() {
        for (Unit u : units) {
            u.endTurn();
        }
    }

    /**
     * добавляет в список всех юнитов своей страны с карты
     */
    public void addAllUnits(List<Unit> units) {
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
     *
     * @param settlements
     */
    public void addSettlements(List<Settlement> settlements) {
        for (Cell c : map) {
            if (c.settlement != null) {
                settlements.add(c.settlement);
            }
        }
    }
}
