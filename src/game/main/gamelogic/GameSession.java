package game.main.gamelogic;

import android.content.res.Resources;
import android.graphics.Canvas;
import game.main.GUI.ActiveArea;
import game.main.GUI.GamePanel;
import game.main.GameThread2;
import game.main.gamelogic.world.*;
import game.main.utils.LinearCongruentialGenerator;
import game.main.utils.Sprite;
import game.main.utils.SpriteBank;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * main() - главная часть программы, repaint() - вызов, после которого, если возможно, будет вызван paint()
 * Created by lgor on 02.04.14.
 */
public class GameSession extends GameThread2 {

    public boolean notFinished = true;
    World world;
    MapRender render;
    public GameProperties properties;
    public Player currentPlayer;
    GamePanel panel;
    List<ActiveArea> gui = new ArrayList<ActiveArea>();
    private boolean newTouches = true;
    private ActiveArea currentActive;

    private final Resources resources;
    private SpriteBank sprites;

    public GameSession(Object monitor, Resources resources) {
        super(monitor);
        this.resources = resources;
    }

    @Override
    public void main() {
        createNewWorld(120, 120);
        repaint();
        while (notFinished) {
            doLogic();
            if (newTouches) {
                repaint();
            }
            checkPause();
        }
    }

    @Override
    public void paint(Canvas canvas) {
        render.render(this, world.map, canvas, panel);
        for (ActiveArea area : gui) {
            area.render(render, canvas);
        }
    }

    private void setNextPlayer() {
        currentPlayer.beforeEndTurn();
        currentPlayer = world.getNextPlayer();
        currentPlayer.startNextTurn();
    }

    public void doLogic() {
        List<Touch> touches = getTouches();
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

    protected void createNewWorld(int width, int height) {
        properties = new GameProperties();

        sprites = new SpriteBank(resources);

        ArrayList<LandType> landscape = new ArrayList<LandType>();
        landscape.add(new LandType(sprites.getSprite("grass"), 2, "Поле"));
        landscape.add(new LandType(sprites.getSprite("grass"), 4, "Лес", sprites.getSprite("forest"), 0.25f, -0.25f));
        landscape.add(new LandType(sprites.getSprite("hill"), 4, "Холм"));
        Settlement.init(sprites);

        render = new MapRender(128, new Sprite[]{sprites.getSprite("road100"), sprites.getSprite("road010"),
                sprites.getSprite("road110"), sprites.getSprite("road001"), sprites.getSprite("road101"),
                sprites.getSprite("road011"), sprites.getSprite("road111")});

        world = new World(width, height, landscape, LinearCongruentialGenerator.getLikeNativeRandom());

        Country country = new Country(world, 1);
        Gamer gamer = new Gamer(world, country);

        UnitType crusader = new UnitType(2, 2, 0, sprites.getSprite("crusader"));
        country.createUnit(crusader, 2, 2);
        country.createUnit(crusader, 4, 4);

        world.map.getCell(2, 2).getUnit().buildCastle().apply();

        world.addPlayer(gamer);

        currentPlayer = world.getNextPlayer();

        panel = GamePanel.getGamePanel2(gamer, sprites);
        gui.add(panel);
    }
}
