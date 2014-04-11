package game.main.gamelogic.world;

import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.iRender;
import game.main.utils.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * тип ландшафта
 * Created by lgor on 31.12.13.
 */
public class LandType implements iRender, ResourcesCounter.ResourcesProducer {
    /*
    тип ландшафта
        его параметры, возможные улучшения, возможно картинка, но хочется несколько для разнообразия
    */

    String name;
    public final int movingCost;
    public final boolean accessable;
    //  Список возможных для данного типа местности улучшений. Может быть пустым.
    public final List<LandUpgrade> landUpgrades = new ArrayList<LandUpgrade>();
    protected final Sprite sprite;
    private iRender nextLayer;

    public LandType(Sprite sprite, int movingCost, String name) {
        this.sprite = sprite;
        this.name = name;
        this.movingCost = movingCost;
        this.accessable = (movingCost > 0);
        this.nextLayer = iRender.NullRender.get();
    }

    public LandType(Sprite sprite, int movingCost, String name, final Sprite second) {
        this(sprite, movingCost, name);
        this.nextLayer = new iRender() {
            @Override
            public void render(RenderParams params) {
                second.render(params);
            }
        };
    }

    @Override
    public void render(RenderParams params) {
        sprite.render(params);
    }

    @Override
    public void produce(ResourcesCounter counter) {
        //TODO производство ресурсов в зависимости от типа поверхности
        counter.food++;
    }

    /**
     * @return nextLayer - например, для клетки с лесом трава - sprite, nextLayer - деревья.
     */
    public iRender nextLayer() {
        return nextLayer;
    }

    @Override
    public String toString() {
        return "name : " + name;
    }
}
