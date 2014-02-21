package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import game.main.gamelogic.GameProperties;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;
import game.main.gamelogic.world.World;
import game.main.utils.FPS;

import java.util.List;

/**
 * Created by lgor on 17.01.14.
 * Камера, которая показывает карту.
 * В будущем она должна показывать карту не всю, а так, как её видит игрок.
 */
public abstract class MapCamera {

    protected Point position = new Point(); //левый верхний угол экрана
    protected float dy, h, w;
    protected FPS fps = new FPS();
    protected int screenH, screenW;

    public abstract void render(World world, Canvas canv, GameProperties properties, List<iRenderFeature> features);

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
        w *= scale;
        dy = h * 3 / 4;
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

    public int getScreenHeight() {
        return screenH;
    }

    public int getScreenWidth() {
        return screenW;
    }

    /**
     * прямоугольник, в который вписана клетка (x,y) карты
     */
    final public RectF getRectF(int x, int y) {
        float xLeft = MapToX(x, y);
        float yTop = MapToY(y);
        return new RectF(xLeft, yTop, xLeft + w, yTop + h);
    }

    /**
     * подвинуть камеру на (dx,dy)
     */
    final public void move(float dx, float dy) {
        position.x += dx;
        position.y += dy;
    }

    //если камера уползла за край карты, двигаем её обратно
    protected void checkPosition(int viewWidth, int viewHeight, int maxX, int maxY) {
        if (position.x < 0)
            position.x = 0;
        if (position.y < 0)
            position.y = 0;
        if (position.x + viewWidth > maxX) {
            position.x = maxX - viewWidth;
        }
        if (position.y + viewHeight > maxY) {
            position.y = maxY - viewHeight;
        }
        //TODO проверку на слишком маленький или большой масштаб
    }
}
