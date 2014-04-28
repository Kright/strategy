package game.main.utils;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * сохраняет все нажатия на экран
 * Created by lgor on 29.04.14.
 */
public class TouchBuffer implements View.OnTouchListener{

    private final List<Touch> touches = new ArrayList<Touch>();
    private Touch prev;

    public final List<Touch> getTouches() {
        List<Touch> copy;
        synchronized (touches) {
            copy = new ArrayList<Touch>(touches);
            touches.clear();
        }
        return copy;
    }

    @Override
    public final boolean onTouch(View v, MotionEvent event) {
        prev = Touch.getTouches(event, prev);
        synchronized (touches) {
            touches.add(prev);
        }
        return true;
    }
}
