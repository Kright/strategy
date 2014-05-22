package com.vk.lgorsl.gamelogic.AI;

import android.graphics.Canvas;
import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.MapRender;
import com.vk.lgorsl.gamelogic.Player;
import com.vk.lgorsl.gamelogic.world.Country;

/**
 * игрок - искусственный интеллект
 * Created by lgor on 04.05.14.
 */
public class PlayerAI extends Player {

    public PlayerAI(GameSession session, Country country) {
        super(session, country);
    }

    @Override
    protected void doTurn(MapRender render) {
        //GameSession.sleep(100);
        //session.repaint();
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        //render.render(canvas, country.map.getTrueMap());
    }
}
