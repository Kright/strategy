package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;
import game.main.utils.sprites.iRender;

/**
 * Created by Michael-PC on 10.04.14.
 */
public class LandUpgrade implements iRender, ResourcesCounter.ResourcesProducer {

    public final String name;

    protected final Sprite sprite;
    //private final int[] resources;

    public LandUpgrade(Sprite sprite, String name) {
        this.sprite = sprite;
        this.name = name;
    }

    public void produce(ResourcesCounter counter) {
        //counter.food += resources[0];
        //counter.gold += resources[1];
    }

    public void render(RenderParams params) {
        sprite.render(params);
    }
}
