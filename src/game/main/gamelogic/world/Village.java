package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Деревушка
 * Created by lgor on 14.03.14.
 */
public class Village extends Settlement {

    protected int population; // население
    protected int wealth; // благосостояние
    protected int wellBeing; // благополучие
    protected float levelOfTaxes; // уровень налогов, который нужно задавать через Region
    protected ResourcesCounter rCounter;
    //protected int growthOfPopulation;
    //protected int growthOfWealth;

    protected ArrayList<Cell> fields;



    public Village(Country country, Cell cell) {
        super(country, cell);
        population = 1;// условно
        wealth = 1;// условно
        wellBeing=1;
    }

    @Override
    public int getTaxes() {
        return (int)(levelOfTaxes*population);
    }

    @Override
    public List<Upgrade> getUpgrades() {
        return new ArrayList<Upgrade>();                    //заглушка
    }

    @Override
    public void nextTurn() {
        rCounter.clear();
        produce();
        int growthOfPopulation=spawn();
        changeWellBeing();
        population+=growthOfPopulation;
        wealth+=rCounter.gold;
    }

    private int spawn(){
            return population+wellBeing-getTaxes();
    }



    private void produce(){
        int profitf;
        for(Cell c:fields){
            c.produce(rCounter);
        }
    }

    private int changeWellBeing(){
        return wellBeing;
    }


    protected static Sprite sprite;

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }
}
