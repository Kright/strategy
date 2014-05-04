package com.vk.lgorsl.gamelogic.world;

import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.Sprite;

import java.util.ArrayList;

/**
 * town. Has a walls, have profit from trading and agronomy.
 * Max size depends on cultureLevel
 * Created by lgor on 12.04.14.
 */
public class Town extends Settlement{

    protected ArrayList<Cell> fields;

    /**
     * Town is an upgrade of village
     */
    protected Town(Village village){
        super(village.country, village.cell);
        country.map.addSettlement(this, cell.x, cell.y);
        fields = village.fields;
    }

    @Override
    public int getTaxes() {
        return 0;
    }

    @Override
    public void nextTurn() {

    }

    @Override
    public void removeSettlement() {

    }

    static Sprite sprite;

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }
}
