package com.vk.lgorsl.gamelogic.world;

import android.util.Log;
import com.vk.lgorsl.gamelogic.world.unit.Unit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Страна.
 * Понятие страны отделено от игрока - игрок будет отвечать за действия над государством, а этот класс - за его
 * состояние (юниты, развитие, производство, казна и т.п.)
 * Created by lgor on 12.03.14.
 */
public class Country {

    public final int id;
    public final Map map;
    protected final World world;

    protected List<Unit> units = new ArrayList<Unit>();
    protected List<Castle> castles = new ArrayList<Castle>();
    protected List<Unit> freeUnits = new ArrayList<Unit>();

    protected int gold; //казна

    public Country(World world, int id) {
        this.id = id;
        this.world = world;
        this.map = world.map.createPlayerMap();
        map.listsUnitsCastles(this.id, units, castles);
        Log.d("action", "Country constructor : " + castles.size() + " settlements, " + units.size() + " units");
    }

    /**
     * вызывать вначале хода каждого госудаства
     * города что-нибудь производят, пополняется казна и т.п.
     */
    public void startNextTurn() {
        map.listsUnitsCastles(this.id, units, castles);
        for (Castle castle : castles) {
            if (castle.country == this) {
                castle.nextTurn();
                gold += castle.getTaxes();
            }
        }
        for (Unit unit : units) {
            gold -= unit.upkeep();
            unit.startNextTurn();
            if (unit.isFree()) {
                freeUnits.add(unit);
            }
        }
    }

    public List<Unit> getFreeUnits() {
        Iterator<Unit> units = freeUnits.iterator();
        while (units.hasNext()) {
            if (!units.next().isFree()) {
                units.remove();
            }
        }
        if (freeUnits.isEmpty()) {
            for (Cell c : map) {
                if (c.hasUnit()) {
                    Unit u = c.getUnit();
                    if (u.country == this && u.isFree()) {
                        freeUnits.add(u);
                    }
                }
            }
        }
        return freeUnits;
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
                if (u.country == this) {
                    u.endTurn();
                }
            }
        }
    }

    public void createUnit(UnitType unitType, int x, int y) {
        Action.addUnit(new Unit(this, unitType), x, y).apply();
    }
}
