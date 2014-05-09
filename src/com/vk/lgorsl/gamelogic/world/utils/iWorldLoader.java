package com.vk.lgorsl.gamelogic.world.utils;

import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.world.World;

/**
 * factory for creating or loading world
 * Created by lgor on 03.05.14.
 */
public interface iWorldLoader {

    public World load(GameSession session);

    /**
     * @return success of saving
     */
    public boolean save(GameSession session);
}
