package game.main.gamelogic.meta;

import game.main.GUI.MapCamera;
import game.main.gamelogic.World;
import game.main.input.Touch;

/**
 * Created by lgor on 09.02.14.
 */
public abstract class Player {

    protected final World world;

    public final int id;

    public Player(World world, int id){
        this.world=world;
        this.id=id;
    }

    /*
    обновить состояние городов, казны и т.п.
    вызывается до того, как будет вызван update
     */
    public void nextStep(){
        //TODO
    }

    /*
    вызывается после update==false
    подлечить юнитов, восполнить очки ходов и т.п.
     */
    public void theEnd(){
        //TODO
    }

    /*
    вызывается до тех пор, пока не вернёт fasle.
    между вызовами мир рисуется на экран
     */
    public abstract boolean update(MapCamera camera, Touch[] touches);
}
