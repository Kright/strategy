package game.main.gamelogic;

import android.content.res.Resources;
import android.graphics.Canvas;
import game.main.GUI.ActiveArea;
import game.main.GUI.AdvancedLandType;
import game.main.GUI.GamePanel;
import game.main.R;
import game.main.gamelogic.world.*;
import game.main.utils.CustomRandom;
import game.main.utils.LinearCongruentialGenerator;
import game.main.utils.Sprite;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.Collections;
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

        Sprite[] spritesL = Sprite.loadHorisontalN(resources, R.drawable.land4, 5);
        Sprite[] sprites = Sprite.loadHorisontalSpecial(resources, R.drawable.land4, 5, 32);

        ArrayList<LandType> landscape = new ArrayList<LandType>();
        landscape.add(new LandType(sprites[0], 2, "Поле"));
        landscape.add(new AdvancedLandType(spritesL[1], 4, 0, -0.25f, "Лес"));
        landscape.add(new LandType(sprites[2], 4, "Холм"));

        render = new MapRender(128);
        Settlement.init(new Sprite[]{sprites[3], sprites[4]});

        world = new World(120,120, landscape);

        Country country = new Country(world, 1);
        Gamer gamer = new Gamer(world, country);

        UnitType crusader = new UnitType(4, 2, 0, Sprite.loadHorisontalN(resources, R.drawable.xz2, 1)[0]);
        country.createUnit(crusader, 2, 2);
        country.createUnit(crusader, 4, 4);
        world.map.getCell(2, 2).getUnit().buildCastle().apply();

        world.addPlayer(gamer);

        currentPlayer = world.getNextPlayer();

        panel = GamePanel.getGamePanel(gamer, 60);
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
        render.render(this, canv, panel);
        for (ActiveArea area : gui) {
            area.render(render, canv);
        }
    }

    public boolean maySkipRender() {
        return (properties.powerSaving && !newTouches);
    }
}
