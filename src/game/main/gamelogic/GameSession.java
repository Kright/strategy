package game.main.gamelogic;

import android.content.res.Resources;
import android.graphics.Canvas;
import game.main.GUI.ActiveArea;
import game.main.GUI.GamePanel;
import game.main.gamelogic.world.*;
import game.main.utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgor on 16.01.14.
 * Такой большой Singleton. Имеет статическую ссылку на себя, чтобы при сворачивании приложения ничего не пропадало
 * Cостояние игры:
 * 1. Игровой мир
 * 2. Всё, что связано с GUI (картинки, положение экрана, последнее активное нажатие и т.п.)
 * 3. настройки
 * методы, связанные с загрузкой и сохранением этого всего
 */
public class GameSession {

    public static GameSession now;

    public boolean notFinished = true;
    public CustomRandom rnd;         //потом тут будет свой, особый генератор случайных чисел
    World world;
    MapRender render;
    GameProperties properties;
    Player currentPlayer;
    GamePanel panel;
    List<ActiveArea> gui = new ArrayList<ActiveArea>();
    private boolean newTouches = true;
    private ActiveArea currentActive;

    private GameSession() {
    }

    public static GameSession initGameSession(Resources resources) {
        if (now == null) {
            now = new GameSession();
            now.init(resources);
        }
        return now;
    }

    public void init(Resources resources) {
        properties = new GameProperties();
        rnd = LinearCongruentialGenerator.getLikeNativeRandom();

        ArrayList<LandType> landscape = new ArrayList<LandType>();

        SpriteBank sprites = new SpriteBank(resources);
        landscape.add(new LandType(sprites.get("grass"), 2, "Поле"));
        landscape.add(new LandType(sprites.get("grass"), 4, "Лес", sprites.get("forest"), 0.25f, -0.25f));
        landscape.add(new LandType(sprites.get("hill"), 4, "Холм"));
        Settlement.init(sprites);

        render = new MapRender(128);
        world = new World(60, 60, landscape);

        Country country = new Country(world, 1);
        Gamer gamer = new Gamer(world, country);

        UnitType crusader = new UnitType(4, 2, 0, sprites.get("crusader"));
        country.createUnit(crusader, 2, 2);
        country.createUnit(crusader, 4, 4);

        world.map.getCell(2, 2).getUnit().buildCastle().apply();

        world.addPlayer(gamer);

        currentPlayer = world.getNextPlayer();

        panel = GamePanel.getGamePanel2(gamer, sprites);
        gui.add(panel);
    }

    public void doLogic(List<Touch> touches) {
        newTouches = (!touches.isEmpty());
        List<Touch> tt = new ArrayList<Touch>();
        while (!touches.isEmpty()) {
            Touch t = touches.remove(0);
            if (t.firstTouch()) {
                currentActive = null;
                for (ActiveArea area : gui) {
                    if (area.interestedInTouch(t)) {
                        currentActive = area;
                        break;
                    }
                }
            }
            if (currentActive != null) {
                currentActive.update(t);
            } else {
                tt.add(t);
            }
        }
        if (!currentPlayer.update(render, tt)) {
            setNextPlayer();
        }
    }

    private void setNextPlayer() {
        currentPlayer.beforeEndTurn();
        currentPlayer = world.getNextPlayer();
        currentPlayer.startNextTurn();
    }

    public void render(Canvas canv) {
        render.render(this, currentPlayer.getCountry().map, canv, panel);
        for (ActiveArea area : gui) {
            area.render(render, canv);
        }
    }

    public boolean maySkipRender() {
        return (properties.powerSaving && !newTouches);
    }
}
