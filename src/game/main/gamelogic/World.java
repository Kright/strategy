package game.main.gamelogic;

import game.main.gamelogic.meta.LandType;
import game.main.gamelogic.meta.Player;

import java.util.ArrayList;

/**
 * Created by lgor on 31.12.13.
 */
public class World {

    public final Map map;

    private ArrayList<Player> players=new ArrayList<Player>();
    private int currentPlayer=-1;

    public World(LandType[] types){
        map=new Map(100,70);
        map.fillRandom(types);
    }

    public Player getNextPlayer(){
        currentPlayer++;
        if (currentPlayer>=players.size()){
            currentPlayer=0;
        }
        lastAction=null;    //негоже отменять ходы предыдущего игрока
        return players.get(currentPlayer);
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * Action само себя заносит в это поле после того, как будет применено
     */
    protected Action lastAction=null;

    protected void cancelPreviousAction(){
        if (lastAction==null) {
            return;
        }
        lastAction.cancel();
        lastAction=null;
    }
}
