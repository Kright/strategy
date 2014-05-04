package game.main.gamelogic.userinput;

/**
 * состояние, которое возвращается, если игрок закончит ход
 * Created by lgor on 04.05.14.
 */
class EndOfTurn extends State{

    EndOfTurn(Gamer gamer) {
        super(gamer);
    }

    @Override
    State getNext() {
        throw new UnsupportedOperationException("userinput: end of turn without end");
    }
}
