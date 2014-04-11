package game.main.gamelogic.world;

/**
 * Created by Michael-PC on 10.04.14.
 */
public class ResourcesCounter {

    /**
     * тот, кто производит ресурсы.
     * Подразумевается, что он увеличивает счётчик на нужные значения +=
     */
    interface ResourcesProducer{
        void produce(ResourcesCounter counter);
    }

    public int food;
    public int gold;

    public void clear(){
        food=0;
        gold=0;
    }
}
