package game.main.gamelogic;

import android.util.Log;
import game.main.GUI.MapCamera;
import game.main.gamelogic.meta.Player;
import game.main.input.Touch;

/**
 * Created by lgor on 09.02.14.
 */
public class Gamer extends Player {

    public Gamer(World world, int id) {
        super(world, id);
    }

    /**
     * для оценки производительности
     * Sony Xperia SL - 16мс. Т.е., одиночный обход всей карты за 0.00016сек, что круто
     */
    private void test(){
        Cell[][] t=world.map.table;
        long time=System.currentTimeMillis();
        int counter=0;
        for(int k=0;k<100;k++){
            for(int i=0;i<t.length;i++){
                for(int j=0;j<t[i].length;j++){
                    Cell c=t[i][j];
                    if (c.unit!=null){
                        counter++;
                    }
                }
            }
        }
        time = System.currentTimeMillis()-time;
        Log.d("mylog",time+"ms"+counter);
    }

    @Override
    public boolean update(MapCamera camera, Touch[] touches) {
        if (touches != null) {
            for (Touch t : touches) {
                update(camera, t);
            }
        }
        return true;
    }

    private void update(MapCamera camera, Touch touch) {
        if (touch.count()==1){
            camera.move(-touch.dx(), -touch.dy());
        }
        else{
            camera.move(-(touch.dx()+touch.next.dx())/2,-(touch.dy()+touch.next.dy())/2);
            Touch t2=touch.next;
            float scale=(float)Math.sqrt(len2(touch.x-t2.x,touch.y-t2.y)/len2(touch.oldX()-t2.oldX(),touch.oldY()-t2.oldY()));
            camera.scale(scale,(touch.x+t2.x)/2,(touch.y+t2.y)/2);
        }
        /*
        if (touch.count()==3){
            test();
        }
        */
    }

    private final float len2(float x,float y){
        return (x*x+y*y);
    }
}
