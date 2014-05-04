package game.main.utils;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * сохраняет все нажатия на экран
 * Created by lgor on 29.04.14.
 */
public class TouchBuffer implements View.OnTouchListener {

    private final List<Touch> touches = new ArrayList<Touch>();
    private Touch prev;

    public synchronized List<Touch> getTouches() {
        List<Touch> copy;
        copy = new ArrayList<Touch>(touches);
        touches.clear();
        return copy;
    }

    public synchronized Touch getTouch(){
        if (touches.isEmpty()) return prev;
        return touches.remove(0);
    }

    public synchronized Touch getTouchWithoutRemove(){
        if (touches.isEmpty()) return prev;
        return touches.get(0);
    }

    @Override
    public synchronized boolean onTouch(View v, MotionEvent event) {
        prev = Touch.getTouches(event, prev);
        touches.add(prev);
        return true;
    }

    public synchronized boolean isEmpty() {
        return touches.isEmpty();
    }
}
