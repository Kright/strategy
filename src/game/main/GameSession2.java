package game.main;

import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.utils.FPS;

/**
 * main() - главная часть программы, repaint() - вызов, после которого, если возможно, будет вызван paint()
 * Created by lgor on 02.04.14.
 */
public class GameSession2 extends GameThread2{

    protected GameSession2(Object monitor) {
        super(monitor);
    }

    //for example
    int counter=0;
    Paint p = new Paint();
    private FPS fps= new FPS();
    {
        p.setTextSize(80);
    }

    @Override
    public void main() {
        while(true){
            repaint();
            checkPause();
        }
    }

    @Override
    public void paint(Canvas canvas) {
        canvas.drawColor(0xFFAAAAAA);
        canvas.drawText((counter++)+",  fps = "+fps.get(), 100,100, p);
    }
}
