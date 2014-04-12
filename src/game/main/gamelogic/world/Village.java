package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;
import java.util.ArrayList;

/**
 * Деревушка
 * Created by lgor on 14.03.14.
 */
public class Village extends Settlement {

    public static final int[] maxWealth = new int[]{10, 20, 30, 40};
    private static final int productivity = 10;     //доход на жителя

    protected int population; // may be {1,2,3,4}
    protected int wealth; // благосостояние

    protected ArrayList<Cell> fields;

    public Village(Country country, Cell cell) {
        super(country, cell);
        population = 1;
        wealth = 1; // условно
    }

    @Override
    public void nextTurn() {
        wealth += getProfit();
        if (wealth < 0) {
            decreasePopulation();
        }
        if (wealth > maxWealth[population + 1]) {
            increasePopulation();
        }
    }

    /**
     * @return прибыль с учётом налогов
     */
    private int getProfit(){
        Castle c=cell.controlledByCastle();
        double k=1;
        if (c!=null){
            k-=c.getLevelOfTaxes();
        }
        return (int)(k*getYield());
    }

    /**
     * @return поступления в казну (они меньше собираемых налогов)
     */
    @Override
    public int getTaxes() {
        Castle castle = cell.controlledByCastle();
        if (castle == null) return 0;
        return (int) (castle.getLevelOfTaxes() * castle.getEfficiency() * population);
    }

    /**
     * @return чистый доход
     */
    private int getYield() {
        return population*productivity;
    }

    private void decreasePopulation() {
        if (population == 1) {
            removeSettlement();
        } else {
            population--;
            wealth = maxWealth[population + 1];
        }
    }

    private void increasePopulation() {
        wealth = 0;
        if (foundNewSettlementNear()){
            return;
        }
        if (population<4){
            population++;
            wealth = 0;
            return;
        }
        //TODO превращение в город
    }

    /**
     * @return true if this village can and create new village.
     */
    private boolean foundNewSettlementNear(){
        return false;    //TODO поиск новых мест для поселения
    }

    @Override
    public void removeSettlement() {
        for(Cell c:fields){
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
