package com.vk.lgorsl.gamelogic.userinput;

import android.graphics.Canvas;
import com.vk.lgorsl.gamelogic.MapRender;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.utils.Touch;

/**
 * абстрактный класс для состояния.
 * Created by lgor on 04.05.14.
 */
abstract class State{

    final Gamer gamer;

    State(Gamer gamer){
        this.gamer = gamer;
    }

    abstract State getNext();

    void paint(Canvas canvas, MapRender render){
        render.render(canvas, gamer.country.map);
    }

    Cell getTrueCell(Touch t){
        return gamer.camera.getCell(gamer.country.map.getTrueMap(), t.x, t.y);
    }
}