package game.main.gamelogic.world;

import game.main.utils.CustomRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgor on 31.12.13.
 * игровой мир - карта, игроки
 */
public class World implements Serializable{
     public final Map map;
    /**
     * мирные поселения, которые могут существовать без игрока, "принадлежат" этой "стране"
     */
    public final Country international;

    private CustomRandom rnd;
    private ArrayList<Player> players = new ArrayList<Player>();
    private int currentPlayer = -1;

    public World(int width, int height, List<LandType> types, CustomRandom random) {
        map = new Map(Map.getTestConstructor(width, height, types, random));
        Action.init(this);
        international = new Country(this, 0);
        rnd = random;
    }

    public CustomRandom getRandom(){
        return rnd;
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
