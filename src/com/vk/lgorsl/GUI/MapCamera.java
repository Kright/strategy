package com.vk.lgorsl.GUI;

import android.graphics.Point;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.Map;
import com.vk.lgorsl.utils.FPS;
import com.vk.lgorsl.utils.sprites.RenderParams;

import java.util.Iterator;

/**
 * Камера, которая показывает карту.
 * В будущем она должна показывать карту не всю, а так, как её видит игрок.
 * TODO - переделать итератор по клеткам - пусть в параметрах передаются границы
 * Для того, чтобы в будущем одним хитрым способом можно было ускорить рисование - рисовать кусок карты в текстуру, а
 * потом при перемещении камеры рисовать тот кусок + вручную небольшой карты.
 * Created by lgor on 17.01.14.
 */
public class MapCamera {

    protected Point position = new Point(); //левый верхний угол экрана
    protected float dy, h, w;

    protected FPS fps = new FPS();
    protected int screenH, screenW;

    public MapCamera(int spriteWidth, int spriteHeight) {
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
     * отображение клетки карты на конечное изображение
     */
    final public int MapToY(int y) {
        return (int) (y * dy - position.y);
    }

    /**
     * отображение клетки карты на конечное изображение
     */
    final public int MapToX(int x, int y) {
        return (int) (x * w - y * w * 0.5f - position.x);
    }

    /**
     * устанавливает в renderParams коориднаты клетки
     */
    final public void setXY(RenderParams params, Cell c) {
        params.y = (int) (c.y * dy - position.y);
        params.x = (int) (c.x * w - c.y * w * 0.5f - position.x);
    }

    /**
     * возвращает клетку, в которую нажали на экране
     */
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

    /**
     * подвинуть камеру на (dx,dy)
     */
    final public void move(float dx, float dy) {
        position.x += dx;
        position.y += dy;
    }

    /**
     * установить камеру
     * @param cell - клетка, которая будет в центре обзора камеры
     */
    final public void setPosition(Cell cell){
        int x = MapToX(cell.x, cell.y);
        int y = MapToY(cell.y);
        position.x = x - screenW/2+(int)w/2;
        position.y = y - screenW/2+(int)h/2;
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

    public CellIterator getIterator(final Map map, final RenderParams params) {
        return getIterator(map, params, 0, 0, screenW, screenH);
    }

    public CellIterator getIterator(final Map map, final RenderParams params, final int xLeft, final int yTop, final int xRight, final int yBottom) {
        return new CellIterator() {

            private int x;
            private int y;
            private int minX;
            private int maxY;
            private int xx;
            private int yy;
            private boolean hasNext;

            {   //вместо конструктора
                clearState();
            }

            public void clearState() {
                y = Math.max(0, (int) YonMap(yTop) - 1);
                x = (int) XonMap(xRight, y * dy - position.y + 1);
                yy = MapToY(y);
                xx = MapToX(x, y);
                maxY = Math.min(map.height, (int) YonMap(yBottom) + 1);
                minX = Math.max(0, (int) XonMap(-w / 2 + xLeft, 1 + y * dy - position.y));
                hasNext = true;
            }

            private void incXY() {
                x--;
                if (x < minX) {
                    y++;
                    x = (int) XonMap(xRight, y * dy - position.y + 1);
                    minX = Math.max(0, (int) XonMap(-w / 2 + xLeft, 1 + y * dy - position.y));
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
            public Cell next() {
                params.x = xx - 1;
                params.y = yy - 1;
                Cell c = map.getCell(x, y);
                incXY();
                return c;
            }

            @Override
            public void remove() {
            }

            @Override
            public Iterator<Cell> iterator() {
                return this;
            }
        };
    }

    public interface CellIterator extends Iterator<Cell>, Iterable<Cell> {

        /**
         * после вызова этого метода итератор начинает обход сначала
         * Можно использовать вместо того, чтобы создавать новый итератор.
         */
        public void clearState();
    }
}