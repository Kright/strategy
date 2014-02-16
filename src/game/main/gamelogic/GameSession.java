package game.main.gamelogic;

import android.content.res.Resources;
import android.graphics.Canvas;
import game.main.GUI.MapCamera;
import game.main.GUI.Sprite;
import game.main.R;
import game.main.gamelogic.meta.LandType;
import game.main.gamelogic.meta.Player;
import game.main.gamelogic.meta.Settlement;
import game.main.gamelogic.meta.UnitType;
import game.main.input.Touch;

import java.util.Random;

/**
 * Created by lgor on 16.01.14.
 */
public class GameSession {

    //статическая ссылка на себя, чтобы при сворачивании приложения ничего не пропадало
    public static GameSession now;

    /**
     * Cостояние игры:
     * 1. Игровой мир
     * 2. Всё, что связано с GUI (картинки, положение экрана, последнее активное нажатие и т.п.)
     * 3. настройки
     * методы, связанные с загрузкой и сохранением этого всего
     */

    public boolean notFinished = true;
    public Random rnd = new Random(); //потом тут будет свой генератор случайных чисел, с возможностью "откатиться"
    // назад, чтобы при отмене дейсвия со случайным исходом (боя, например) и повторении его результат был такой же

    World world;
    MapCamera camera;
    GameProperties properties;
    Player currentPlayer;

    private GameSession() {
    }

    public void init(Resources resources) {
        Sprite[] sprites = Sprite.loadHorisontalN(resources, R.drawable.land2, 5);
        LandType[] landscape = new LandType[3];
        for (int i = 0; i < 3; i++) {
            landscape[i] = new LandType(sprites[i]);
        }
        camera = new MapCamera(192, 128);
        Settlement.init(new Sprite[]{sprites[3], sprites[4]});

        UnitType crusader=new UnitType(2, 2, Sprite.loadHorisontalN(resources, R.drawable.xz2, 1)[0]);

        world = new World(landscape);
        world.map.table[1][1].unit=new Unit(crusader);
        world.addPlayer(new Gamer(world, 1));

        currentPlayer = world.getNextPlayer();
        properties = new GameProperties();
    }

    public void doLogic(Touch[] touches) {
        newTouches = touches != null;
        if (!currentPlayer.update(camera, touches)) {
            currentPlayer.theEnd();
            currentPlayer = world.getNextPlayer();
            currentPlayer.nextStep();
        }
    }

    public void render(Canvas canv) {
        camera.render(world, canv, properties);
    }

    private boolean newTouches;

    public boolean maySkipRender() {
        return (properties.powerSaving && !newTouches);
    }

    public static GameSession initGameSession(Resources resources) {
        if (now == null) {
            now = new GameSession();
            now.init(resources);
        }
        return now;
    }
}
