package com.vk.lgorsl.gamelogic;

import android.graphics.Canvas;
import com.vk.lgorsl.gamelogic.world.Country;

/**
 * абстрактный класс игрока
 * Created by lgor on 09.02.14.
 */
public abstract class Player {

    public final Country country;
    public GameSession session;

    public Player(GameSession session, Country country) {
        this.session = session;
        this.country = country;
    }

    public final void run(MapRender render) {
        startNextTurn();
        doTurn(render);
        beforeEndTurn();
    }

    protected abstract void doTurn(MapRender render);

    public abstract void paint(Canvas canvas, MapRender render);

    /*
    обновить состояние городов, казны и т.п.
    вызывается до того, как будет вызван update
     */
    private void startNextTurn() {
        country.startNextTurn();
    }

    /*
    вызывается после update==false
    подлечить юнитов, восполнить очки ходов и т.п.
     */
    private void beforeEndTurn() {
        country.beforeEndTurn();
    }
}
