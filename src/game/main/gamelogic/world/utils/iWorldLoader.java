package game.main.gamelogic.world.utils;

import game.main.gamelogic.GameSession;
import game.main.gamelogic.world.World;

/**
 * factory for creating or loading world
 * Created by lgor on 03.05.14.
 */
public interface iWorldLoader {

    public World load(GameSession session);

    /**
     * @param world which would be saved
     * @return success of saving
     */
    public boolean save(World world);
}
