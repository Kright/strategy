package game.main.gamelogic.world;

import android.util.Log;
import game.main.utils.CustomRandom;
import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Деревушка
 * Created by lgor on 14.03.14.
 */
public class Village extends Settlement {

    public static final int[] maxWealth = new int[]{100, 200, 300, 400};    //small numbers only for test
    private static final int productivity = 10;     //доход на жителя

    protected int population; // may be {1,2,3,4}
    protected int wealth; // благосостояние
    private int maxPopulation;
    protected ArrayList<Cell> fields = new ArrayList<Cell>(4);

    public Village(Country country, int x, int y) {
        super(getCountry(country.world, x, y), country.map.getTrueMap().getCell(x, y));
        population = 1;
        wealth = 1; // условно
        maxPopulation = 4;
        country.map.getTrueMap().addSettlement(this, x, y);
    }

    protected static Country getCountry(World world, int x, int y) {
        Castle c = world.map.getControllingCastle(x, y);
        if (c != null) {
            return c.country;
        }
        return world.international;
    }

    @Override
    public void nextTurn() {
        wealth += getProfit();
        if (wealth < 0) {
            decreasePopulation();
        }
        if (wealth > maxWealth[population - 1]) {
            increasePopulation();
        }
    }

    /**
     * @return прибыль с учётом налогов
     */
    private int getProfit() {
        Castle c = cell.controlledByCastle();
        double k = 1;
        if (c != null) {
            k -= c.getLevelOfTaxes();
        }
        return (int) (k * getYield());
    }

    /**
     * @return поступления в казну (они меньше собираемых налогов)
     */
    @Override
    public int getTaxes() {
        Castle castle = cell.controlledByCastle();
        if (castle == null) return 0;
        return (int) (castle.getLevelOfTaxes() * castle.getEfficiency() * getYield());
    }

    /**
     * @return чистый доход
     */
    private int getYield() {
        return population * productivity;
    }

    private void decreasePopulation() {
        if (population == 1) {
            removeSettlement();
        } else {
            population--;
            wealth = maxWealth[population - 1];
            if (fields.size() >= population) {
                fields.remove(0).setLandUpgrade(null);
            }
        }
    }

    private void increasePopulation() {
        wealth = 0;
        if (foundNewSettlementNear()) {
            return;
        }
        if (population < maxPopulation) {
            population++;
            wealth = 0;
            addLandUpgrade();
            return;
        }
        becomeTown();
    }

    private void addLandUpgrade() {
        ArrayList<Cell> near = new ArrayList<Cell>(7);
        country.map.getTrueMap().addCellsNear(near, cell.x, cell.y);
        Iterator<Cell> iter = near.iterator();
        while (iter.hasNext()) {
            Cell c = iter.next();
            if (c.hasLandUpgrade() || c.hasSettlement() || !c.accessible() || c.land.landUpgrades.isEmpty()) {
                iter.remove();
            }
        }
        if (near.size() == 0) {    //hasn't place near, fail
            return;
        }
        CustomRandom random = country.world.getRandom();
        Cell c = near.get(random.get(near.size()));
        LandUpgrade upgrade = c.land.landUpgrades.get(random.get(c.land.landUpgrades.size()));
        c.setLandUpgrade(upgrade);
        fields.add(c);
    }

    /**
     * set up Town
     */
    public void becomeTown() {
        country.settlements.remove(this);
        //TODO превращение в город, now town hasn't picture and won't builded
        //new Town(this);
    }

    /**
     * @return true if this village can and create new village.
     */
    private boolean foundNewSettlementNear() {
        if (population < maxPopulation) return false; // если население меньше критического, то ничего не основываем

        int x = cell.x + getRandomXY();
        int y = cell.y + getRandomXY();
        Map trueMap = country.map.getTrueMap();
        Cell target = trueMap.getCell(x, y);
        //Map must be trueMap because country.map may has wrong information about cells
        if (target.controlledByCastle() != null) {
            if (target.controlledByCastle().country != country) {
                return false;
            }
        }
        if (target.hasSettlement() || target.hasLandUpgrade() || !target.accessible() || target.isNull()
                || trueMap.getCell(x + 1, y + 1).hasSettlement()
                || trueMap.getCell(x, y + 1).hasSettlement()
                || trueMap.getCell(x + 1, y).hasSettlement()
                || trueMap.getCell(x - 1, y).hasSettlement()
                || trueMap.getCell(x, y - 1).hasSettlement()
                || trueMap.getCell(x - 1, y - 1).hasSettlement()) {
            return false;
        }
        new Village(country, target.x, target.y);
        return true;
    }

    private int getRandomXY() {
        return country.world.getRandom().get(11) - 5;
    }

    @Override
    public void removeSettlement() {
        for (Cell c : fields) {
            c.setLandUpgrade(null);
        }
        country.map.addSettlement(null, cell.x, cell.y);
    }

    protected static Sprite sprite;

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }
}
