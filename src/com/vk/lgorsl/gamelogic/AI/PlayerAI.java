package com.vk.lgorsl.gamelogic.AI;

import android.graphics.Canvas;
import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.MapRender;
import com.vk.lgorsl.gamelogic.world.Country;
import com.vk.lgorsl.gamelogic.Player;

/**
 * игрок - искусственный интеллект
 * Created by lgor on 04.05.14.
 */
public class PlayerAI extends Player{

    public PlayerAI(GameSession session, Country country) {
        super(session, country);
    }

    @Override
    protected void doTurn(MapRender render) {

    }

    @Override
    public void paint(Canvas canvas, MapRender render) {

    }
}
