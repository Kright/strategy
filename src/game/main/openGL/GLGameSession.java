package game.main.openGL;

import android.opengl.GLSurfaceView;
import game.main.gamelogic.userinput.Gamer;
import game.main.gamelogic.world.*;
import game.main.utils.Touch;
import game.main.utils.TouchBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Здесь весь игровой контекст, а заодно есть реализация Runnable
 * Created by lgor on 29.04.14.
 */
public class GLGameSession implements Runnable {

    /**
     * координаты нажатия - в пикселях
     */
    public final TouchBuffer touches = new TouchBuffer();

    protected volatile boolean running = false;
    protected volatile boolean finished = false;

    protected volatile World world;

    protected final GLSurfaceView view;
    protected final DrawingContext context;

    protected Player currentPlayer;

    public GLGameSession(GLSurfaceView view, DrawingContext drawingContext) {
        this.view = view;
        this.context = drawingContext;
        view.setOnTouchListener(touches);

    }

    @Override
    public void run() {
        int i = 0;
        loadWorld();
        while (!finished) {
            while (running) {
                repaint();
                List<Touch> tt = touches.getTouches();
                while (!tt.isEmpty()) {
                    GLActivity.myLog(tt.remove(0));
                }
                if (i++ % 100 == 0) {
                    GLActivity.myLog("I'm working : " + i / 100);
                }
                sleep(10);
            }
            sleep(50);
        }
        GLActivity.myLog("I'm finished");
    }

    public void setRunning(boolean running) {
        this.running = running;
        GLActivity.myLog("set Running");
    }

    public void finish() {
        finished = true;
        running = false;
    }

    /**
     * фактически, блокирующий вызов
     */
    public void repaint() {
        view.queueEvent(new Runnable() {
            public void run() {
                context.repainted = false;
                view.requestRender();
            }
        });
        while (running && !context.repainted) {
            Thread.yield();
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            //nothing
        }
    }

    void loadWorld() {
        final int worldH = 200, worldW = 240;

        ArrayList<LandType> landscape = new ArrayList<LandType>();
        landscape.add(new LandType(context.getSprite("grass"), 2, "Поле"));
        landscape.add(new LandType(context.getSprite("grass"), 4, "Лес", context.getSprite("forest")));
        landscape.add(new LandType(context.getSprite("hill"), 4, "Холм"));

        landscape.get(2).landUpgrades.add(new LandUpgrade(context.getSprite("windmill"), "windmill"));
        landscape.get(0).landUpgrades.add(new LandUpgrade(context.getSprite("field"), "field"));

        Settlement.initGL(context);

        world = new World(worldW, worldH, landscape);

        Country country = new Country(world, 1);

        UnitType crusader = new UnitType(4, 2, 0, context.getSprite("crusader"));
        country.createUnit(crusader, 2, 2);
        country.createUnit(crusader, 4, 4);

        world.map.getCell(2, 2).getUnit().buildCastle().apply();
        new Village(country, 4, 4);

        world.addPlayer(new Gamer(null , country));

        currentPlayer = world.getNextPlayer();
        currentPlayer = world.getNextPlayer();
    }
}
