package game.main.gamelogic.world;

import game.main.gamelogic.GameSession;
import game.main.utils.CustomRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgor on 31.12.13.
 * игровой мир - карта, игроки
 */
public class World {

    public final Map map;
    /**
     * мирные поселения, которые могут существовать без игрока, "принадлежат" этой "стране"
     */
    public final Country international;

    private ArrayList<Player> players = new ArrayList<Player>();
    private int currentPlayer = -1;
    private CustomRandom random;

    public World(int width, int height, List<LandType> types, GameSession session) {
        random = session.random;
        map = new Map(Map.getTestConstructor(width, height, types, random));
        Action.init(this);
        international = new Country(this, 0);
    }

    public CustomRandom getRandom(){
        return random;
    }

    public Player getNextPlayer() {
        currentPlayer++;
        if (currentPlayer >= players.size()) {
            currentPlayer = 0;
            endOfTurns();
        }
        lastAction = null;    //негоже отменять ходы предыдущего игрока
        return players.get(currentPlayer);
    }

    /**
     * called when all players end his turns
     * there may be increasing of settlements, which don't controlled by players
     */
    public void endOfTurns(){
        for(Cell c:map){
            if (!c.hasSettlement()) continue;
            Settlement s = c.getSettlement();
            if (s.country == international){
                s.nextTurn();
            }
        }
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
