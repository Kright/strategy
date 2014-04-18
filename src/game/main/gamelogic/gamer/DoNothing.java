package game.main.gamelogic.gamer;

import game.main.gamelogic.GameSession;
import game.main.gamelogic.world.Cell;
import game.main.utils.Touch;

/**
 * Created by lgor on 18.04.14.
 */
public class DoNothing extends State {

    public DoNothing(Gamer gamer, GameSession session) {
        super(gamer, session);
    }

    @Override
    public State getNext() {
        session.safeRepaint();
        if (g.touches.isEmpty()) {
            g.waitAndUpdate();
            session.safeRepaint();
            return this;
        }
        Touch t = g.touches.get(0);
        Cell c = g.camera.getCell(g.country.map, t.x, t.y);
        if (c.hasUnit()) {
            //g.activeUnit.setUnit(c.getUnit());
            //return g.activeUnit;
        }
        //return g.mapMoving;
        return null;
    }
};

