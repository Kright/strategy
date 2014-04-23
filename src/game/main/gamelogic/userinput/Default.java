package game.main.gamelogic.userinput;

import game.main.gamelogic.world.Cell;
import game.main.utils.Touch;

/**
 * дефолтное состояние интерфейса - ничего не нажато, никаких активных юнитов и прочего
 * Created by lgor on 18.04.14.
 */
class Default extends Gamer.State{

    public Default(Gamer gamer){
        gamer.super();
    }

    public Default mustUpdate(){
        needRepaint();
        return this;
    }

    @Override
    public Gamer.State getNext() {
        while (noTouches()){    //TODO убрать 4 стрчоки, они для теста
            repaint();
            checkPause();
        }
        mayBeRepaint();
        Touch t = waitTouch();
        Cell c = getTrueCell(t);
        if (c.hasUnit()){
            return gamer().unitActivation.setUnit(c.getUnit());
        }
        return gamer().mapMoving;
    }
}


