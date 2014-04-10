package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;
import game.main.utils.sprites.iRender;

import java.util.ArrayList;

/**
 * Created by Michael-PC on 10.04.14.
 */
public class LandUpgrade implements iRender, ResourcesCounter.ResourcesProducer {

    public String name;

    protected final Sprite sprite;
    private iRender nextLayer;
    private int[] resources;

    public LandUpgrade(Sprite sprite, String name, int[] resources) {
        this.sprite = sprite;
        this.name = name;
        this.resources=resources;
        this.nextLayer = iRender.NullRender.get();
    }

    public void produce(ResourcesCounter counter){
        counter.food+=resources[0];
        counter.gold+=resources[1];

    }

    public iRender nextLayer(){
        return nextLayer;
    }



    public void render(RenderParams params){

    }
}
