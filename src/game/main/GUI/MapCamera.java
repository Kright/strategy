package game.main.GUI;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;
import game.main.utils.FPS;

import java.util.Iterator;

/**
 * Камера, которая показывает карту.
 * В будущем она должна показывать карту не всю, а так, как её видит игрок.
 * Created by lgor on 17.01.14.
 */
public abstract class MapCamera {

    protected Point position = new Point(); //левый верхний угол экрана
    protected float dy, h, w;

    protected FPS fps = new FPS();
    protected int screenH, screenW;

    protected MapCamera(int spriteWidth, int spriteHeight) {
        h = spriteHeight;
        w = spriteWidth;
        dy = spriteHeight * 3 / 4;
    }

    /**
     * масштабирование относительно точки position.x+cx, position.y+cy;
     */
    public void scale(float scale, float centerX, float centerY) {
        h *= scale;
        w = h * 1.5f;
        dy = h * 0.75f;
        position.x = (int) (position.x * scale + centerX * (scale - 1));
        position.y = (int) (position.y * scale + centerY * (scale - 1));
    }

    /**
     * вертикальная координата клетки карты, если нажатие на экране с коориднатой у
     */
    public final float YonMap(float y) {
        return (y + position.y) / dy;
    }

    /**
     * горизонтальная координата клетки карты как функция от координат нажатия
     */
    public final float XonMap(float x, float y) {
        int delta = (int) YonMap(y); //целая часть числа
        return (x + position.x) / w + delta * 0.5f;
    }

    /**
     * отображение клетки карты на конечное ихображение
     */
    final public int MapToY(int y) {
        return (int) (y * dy - position.y);
    }

    /**
     * отображение клетки карты на конечное ихображение
     */
    final public int MapToX(int x, int y) {
        return (int) (x * w - y * w * 0.5f - position.x);
    }

    //возвращает клетку, в которую нажали на экране
    final public Cell getCell(Map map, float x, float y) {
        int cellY = (int) YonMap(y); //целая часть числа
        int cellX = (int) ((x + position.x) / w + cellY * 0.5f);
        return map.getCell(cellX, cellY);
    }

    /**
     * возвращает ширину клетки
     */
    final public float getCellWidth() {
        return w;
    }

    /**
     * возвращает высоту клетки
     */
    final public float getCellHeight() {
        return h;
    }

    public void setScreenSize(int width, int height) {
        screenW = width;
        screenH = height;
    }

    public int getScreenHeight() {
        return screenH;
    }

    public int getScreenWidth() {
        return screenW;
    }

    private RectF rectangle4return = new RectF();

    /**
     * прямоугольник, в который вписана клетка (x,y) карты.
     * на самом деле возвращается один и тот же прямоугольник, чтобы не мучать сборщик мусора
     */
    final public RectF getRectF(int x, int y) {
        float xLeft = MapToX(x, y);
        float yTop = MapToY(y);
        rectangle4return.set(xLeft, yTop, xLeft + w, yTop + h);
        return rectangle4return;
    }

    /**
     * прямоугольник, в который вписана клетка карты.
     * на самом деле возвращается один и тот же прямоугольник, чтобы не мучать сборщик мусора
     */
    final public RectF getRectF(Cell c) {
        float xLeft = MapToX(c.x, c.y);
        float yTop = MapToY(c.y);
        rectangle4return.set(xLeft, yTop, xLeft + w, yTop + h);
        return rectangle4return;
    }

    /**
     * подвинуть камеру на (dx,dy)
     */
    final public void move(float dx, float dy) {
        position.x += dx;
        position.y += dy;
    }

    //если камера уползла за край карты, двигаем её обратно
    protected void checkPosition(int viewWidth, int viewHeight, float maxX, float maxY) {
        float s = viewHeight / h / 3;
        if (s < 1) {
            scale(s, viewWidth / 2, viewHeight / 2);
        }
        float scaleX = viewWidth / maxX;
        float scaleY = viewHeight / maxY;
        if (scaleX > 1 || position.x < 0) {
            position.x = 0;
        }
        if (scaleY > 1 || position.y < 0) {
            position.y = 0;
        }
        s = Math.max(scaleX, scaleY);
        if (s > 1) {
            scale(s, 0, 0);
            maxX *= s;
            maxY *= s;
        }
        if (position.x + viewWidth > maxX) {
            position.x = (int) maxX - viewWidth;
        }
        if (position.y + viewHeight > maxY) {
            position.y = (int) maxY - viewHeight;
        }
    }

    public Iterator<RenderObject> getIterator(Map map) {
        return new Frame(map);
    }

    public class RenderObject {
        public Cell cell;
        public Rect rect;

        protected RenderObject() {
            rect = new Rect();
        }
    }

    private class Frame implements Iterator<RenderObject> {

        private final RenderObject pair;
        private final Map map;
        private int x, y, minX, maxY, xx, yy;
        private boolean hasNext = true;

        Frame(Map map) {
            this.map = map;
            pair = new RenderObject();
            y = Math.max(0, (int) YonMap(0) - 1);
            x = (int) XonMap(screenW, y * dy - position.y + 1);
            yy = MapToY(y);
            xx = MapToX(x, y);
            maxY = Math.min(map.height, (int) YonMap(screenH) + 1);
            minX = Math.max(0, (int) XonMap(0, 1 + y * dy - position.y));
        }

        private void incXY() {
            x--;
            if (x < minX) {
                y++;
                x = (int) XonMap(screenW, y * dy - position.y + 1);
                minX = Math.max(0, (int) XonMap(0, 1 + y * dy - position.y));
                yy = MapToY(y);
                if (y >= maxY) {
                    hasNext = false;
                }
            }
            xx = MapToX(x, y);
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public RenderObject next() {
            pair.cell = map.getCell(x, y);
            pair.rect.set(xx - 1, yy - 1, xx + (int) (w + 1), yy + (int) (h + 1));
            incXY();
            return pair;
        }

        @Override
        public void remove() {
        }
    }
}

