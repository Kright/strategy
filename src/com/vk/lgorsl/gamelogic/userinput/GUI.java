package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.utils.Touch;

/**
 * та самая штука, которая происходит, если нажать на какую нибудь кнопочку.
 * Created by lgor on 09.05.14.
 */
class GUI extends State {

    public boolean interestedInTouch(Touch t){
        //если вернуть true, то у нас вызовут getNext(), где можно будет обработать нажатие на кнопочку.
        return false;
    }

    GUI(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        return gamer.endOfTurn;
    }
}
