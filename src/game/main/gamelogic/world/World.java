package game.main.gamelogic.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgor on 31.12.13.
 * игровой мир - карта, игроки
 */
public class World {

    public final Map map;

    private ArrayList<Player> players = new ArrayList<Player>();
    private int currentPlayer = -1;

    public World(int width, int height, List<LandType> types) {
        map = new Map(Map.getTestConstructor(width, height, types));
        Action.init(this);
    }

    public Player getNextPlayer() {
        currentPlayer++;
        if (currentPlayer >= players.size()) {
            currentPlayer = 0;
        }
        lastAction = null;    //негоже отменять ходы предыдущего игрока
        return players.get(currentPlayer);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Action само себя заносит в это поле после того, как будет применено
     */
    protected Action lastAction = null;

    protected void cancelPreviousAction() {
        if (lastAction == null) {
            return;
        }
        lastAction.cancel();
        lastAction = null;
    }
}
