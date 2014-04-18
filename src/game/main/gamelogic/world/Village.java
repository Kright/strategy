package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;
import java.util.ArrayList;

/**
 * Деревушка
 * Created by lgor on 14.03.14.
 */
public class Village extends Settlement {

    public static final int[] maxWealth = new int[]{100, 200, 300, 400};
    private static final int productivity = 10;     //доход на жителя

    protected int population; // may be {1,2,3,4}
    protected int wealth; // благосостояние
    private int maxPopulation;
    protected ArrayList<Cell> fields;

    public Village(Country country, Cell cell) {
        super(country, cell);
        population = 1;
        wealth = 1; // условно
        maxPopulation=4;
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
        return (int) (castle.getLevelOfTaxes() * castle.getEfficiency() * getYield());
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
        if (population<maxPopulation){
            population++;
            wealth = 0;
            return;
        }
        //TODO превращение в город
    }

    /**
      *  @return set up Town
     **/
    public void becomeTown(){
        new Town(this);
    }
    /**
     * @return true if this village can and create new village.
     */
    private boolean foundNewSettlementNear(){

        if(population<maxPopulation) return false; // если население меньше критического, то ничего не основываем

        int x=cell.x+getRandomXY();
        int y=cell.y+getRandomXY();

        Cell target=country.map.getCell(x,y);

        if (target.hasSettlement() || target.hasLandUpgrade() || !target.accessible() || target.isNull()
                || country.map.getCell(x+1,y+1).hasSettlement()
                || country.map.getCell(x,y+1).hasSettlement()
                || country.map.getCell(x+1,y).hasSettlement()
                || country.map.getCell(x-1,y).hasSettlement()
                || country.map.getCell(x,y-1).hasSettlement()
                || country.map.getCell(x-1,y-1).hasSettlement())
        return false;

        if(target.controlledByCastle()!=null){
            if(target.controlledByCastle().country!=country)
                return false;
        }

        country.map.addSettlement(new Village(country, target),x,y);

        return true;    //TODO поиск новых мест для поселения
    }

    private int getRandomXY(){
        return country.world.getRandom().get(11)-5;
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
