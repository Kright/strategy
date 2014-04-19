package game.main.gamelogic;

import android.content.res.Resources;
import android.graphics.Canvas;
import game.main.GameThread;
import game.main.gamelogic.world.*;
import game.main.utils.CustomRandom;
import game.main.utils.LinearCongruentialGenerator;
import game.main.utils.Touch;
import game.main.utils.sprites.SpriteBank;

import java.util.ArrayList;
import java.util.List;

/**
 * main() - главная часть программы, repaint() - вызов, после которого, если возможно, будет вызван paint()
 * Created by lgor on 02.04.14.
 */
public class GameSession {

    public final CustomRandom random = LinearCongruentialGenerator.getLikeNativeRandom();

    private volatile GameThread thread;
    private final Resources resources;
    private SpriteBank sprites;

    private boolean notFinished = true;
    public GameProperties properties;

    World world;
    MapRender render;

    public Player currentPlayer;

    public GameSession(Resources resources) {
        this.resources = resources;
    }

    public void setCallback(GameThread thread) {
        this.thread = thread;
    }

    public void run() {
        while (notFinished) {
            currentPlayer.run(render);
            currentPlayer = world.getNextPlayer();
        }
    }

    public void paint(Canvas canvas) {
        currentPlayer.paint(canvas, render);
    }

    /**
     * приложение было свёрнуто и снова открыто.
     * обновляем картинку на экране
     */
    public void resume() {
        sprites.load();
        while (!repaint()) {
            sleep(20);
            Thread.yield();
        }
    }

    /**
     * вызывается перед тем, как приложение будет поставлено на паузу
     */
    public void pause() {
        save();
    }

    /**
     * сохранение gameSession в долговременную память
     */
    public void save() {

    }

    /**
     * @param need - нуждается ли, по мнению вызывающего, экран приложения в перерисовке
     */
    public void needUpdate(boolean need) {
        if (need) {
            screenUpdated = false;
        }
    }

    /**
     * @return успешность последнего обновления экрана
     */
    public boolean screenNotUpdated() {
        return !screenUpdated;
    }

    private boolean screenUpdated = false;

    /**
     * если ничего не изменилось и режим энергосбережения - пропускаем обновление экрана
     * на этом вызове приложение может приостановиться, если его свернули
     */
    public void safeRepaint() {
        checkPause();
        if (!screenUpdated || !properties.powerSaving) {
            screenUpdated = thread.repaint();
        } else {
            if (properties.sleepingInsteadRender > 0) {
                sleep(properties.sleepingInsteadRender);
            }
        }
    }

    /**
     * особое обновление экрана, нельзя прервать.
     * Надо вручную использовать проверку на паузу в приложении.
     *
     * @return success of repaint() call;
     */
    public boolean repaint() {
        return screenUpdated = thread.repaint();
    }

    public boolean checkPause() {
        return thread.checkPause();
    }

    /**
     * @return список нажатий на экран, произощедших после последнего вызова этой функции
     */
    public List<Touch> getTouches() {
        List<Touch> touches = thread.getTouches();
        needUpdate(!touches.isEmpty());
        return touches;
    }

    public final void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

    public void createNewWorld(int width, int height) {
        properties = new GameProperties();

        sprites = new SpriteBank(resources);

        ArrayList<LandType> landscape = new ArrayList<LandType>();
        landscape.add(new LandType(sprites.getSprite("grass"), 2, "Поле"));
        landscape.add(new LandType(sprites.getSprite("grass"), 4, "Лес", sprites.getSprite("forest")));
        landscape.add(new LandType(sprites.getSprite("hill"), 4, "Холм"));
        landscape.get(2).landUpgrades.add(new LandUpgrade(sprites.getSprite("windmill"), "windmill"));
        landscape.get(0).landUpgrades.add(new LandUpgrade(sprites.getSprite("field"), "field"));
        Settlement.init(sprites);

        //render = new MapRender(128, sprites, properties);
        render = new BufferedRender(128, sprites, properties);

        world = new World(width, height, landscape, this);

        Country country = new Country(world, 1);

        UnitType crusader = new UnitType(4, 2, 0, sprites.getSprite("crusader"));
        country.createUnit(crusader, 2, 2);
        country.createUnit(crusader, 4, 4);

        world.map.getCell(2, 2).getUnit().buildCastle().apply();

        world.addPlayer(new game.main.gamelogic.userinput.Gamer(this, country));
        currentPlayer = world.getNextPlayer();

        //panel = GamePanel.getGamePanel2(gamer, sprites);
        //gui.add(panel);
    }
}
