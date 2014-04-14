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

    protected ArrayList<Cell> fields;


    public Village(Country country, Cell cell) {
        super(country, cell);
        population = 1;// условно
        wealth = 1;// условно
    }

    @Override
    public int getTaxes() {
        return population;
    }

    @Override
    public List<Upgrade> getUpgrades() {
        return new ArrayList<Upgrade>();                    //заглушка
    }

    @Override
    public void nextTurn() {

    }

    protected static Sprite sprite;

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }
}
