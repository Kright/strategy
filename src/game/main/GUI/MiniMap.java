package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.utils.Touch;

import java.io.Serializable;

/**
 * Реализация миникарты.
 * В данный момент не используется, но раньше работало исправно и удалять жаль - думаю, код отсуюда может пригодиться
 * Created by lgor on 20.02.14.
 */
public class MiniMap extends ActiveArea implements Serializable{

    private final MapCamera camera;
    transient private boolean activated = false;
    private final float small = 48, big = 128;
    transient private final Paint paint;

    public MiniMap(MapCamera camera) {
        this.camera = camera;
        paint = new Paint();
        paint.setColor(0xFF00FF00);
    }

    private void setScale(float goal) {
        float scale = goal / camera.getCellHeight();
        camera.scale(scale, camera.getScreenWidth() / 2, camera.getScreenHeight() / 2);
    }

    @Override
    public boolean interestedInTouch(Touch touch) {
        if (activated)
            return true;
        if (camera.getScreenWidth() - touch.x < 128 && touch.y < 256) {
            setScale(small);
            return true;
        }
        return false;
    }

    transient private Touch t;

    @Override
    public void update(Touch touch) {

        if (activated) {
            t = touch;
        }
        if (touch.lastTouch() ) {
            if (activated)
            {
                camera.move(t.x - camera.getScreenWidth() / 2, t.y - camera.getScreenHeight() / 2);
                setScale(big);
                t = null;
            }
            activated = !activated;
            }

    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        float scale = small / big / 2;
        float dx = scale * camera.getScreenWidth();
        float dy = scale * camera.getScreenHeight();
        if (activated && t != null) {
            //canvas.drawRect(t.x-dx, t.y-dy, t.x+dx, t.y+dy, paint);
            canvas.drawLine(t.x - dx, t.y - dy, t.x + dx, t.y - dy, paint);
            canvas.drawLine(t.x - dx, t.y - dy, t.x - dx, t.y + dy, paint);
            canvas.drawLine(t.x + dx, t.y + dy, t.x + dx, t.y - dy, paint);
            canvas.drawLine(t.x + dx, t.y + dy, t.x - dx, t.y + dy, paint);
            if (t.x + dx > canvas.getWidth()) {
                camera.move(t.x + dx - canvas.getWidth(), 0);
            }
            if (t.x - dx < 0) {
                camera.move(t.x - dx, 0);
            }
            if (t.y + dy > canvas.getHeight()) {
                camera.move(0, t.y + dy - canvas.getHeight());
            }
            if (t.y - dy < 0) {
                camera.move(0, t.y - dy);
            }
        }
    }
}
