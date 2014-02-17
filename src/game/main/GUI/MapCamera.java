package game.main.GUI;

import android.graphics.*;
import game.main.gamelogic.Cell;
import game.main.gamelogic.GameProperties;
import game.main.gamelogic.Map;
import game.main.gamelogic.World;

import java.util.List;

/**
 * Created by lgor on 17.01.14.
 * Камера, которая показывает карту.
 * В будущем она должна показывать карту не всю, а так, как её видит игрок.
 */
public class MapCamera {

    private Point position = new Point(); //левый верхний угол экрана
    private float dy, h, w;
    private FPS fps = new FPS();
    private int screenH, screenW;
    private Paint p = new Paint();

    {
        p.setTextSize(p.getTextSize() * 2);
    }

    public MapCamera(int spriteWidth, int spriteHeight) {
        h = spriteHeight;
        w = spriteWidth;
        dy = spriteHeight * 3 / 4;
    }

    /**
     * масштабирование относительно точки position.x+cx, position.y+cy;
     */
    public final void scale(float scale, float centerX, float centerY) {
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

    /**
     * прямоугольник, в который вписана клетка (x,y) карты
     */
    final public RectF getRectF(int x, int y) {
        float xLeft = MapToX(x, y);
        float yTop = MapToY(y);
        return new RectF(xLeft, yTop, xLeft + w, yTop + h);
    }

    final public void move(float dx, float dy) {
        position.x += dx;
        position.y += dy;
    }

    public void render(World world, Canvas canv, GameProperties properties, List<iRenderFeature> features) {
        screenH = canv.getHeight();
        screenW = canv.getWidth();
        checkPosition(screenW, screenH);

        canv.drawColor(0xFFFF00FF); //фон
        drawLandscape(world, canv);
        if (properties.renderBorders) {
            drawBorders(canv);
        }
        if (features != null)
            for (iRenderFeature rf : features)
                rf.render(this, canv);
        drawUnits(world, canv);
        if (properties.showFPS)
            canv.drawText("fps=" + fps.get(), 20, 20, p);
    }

    public void trollRender(World world, Canvas canv) {
        canv.drawColor(0xFF880088);
        int w = canv.getWidth() / 2;
        int h = canv.getHeight();
        Paint p = new Paint();
        p.setColor(0xFFFF0000);
        canv.drawRect(0, 0, w, h, p);
        p.setColor(0xFF0000FF);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if ((i + j) % 2 == 0) {
                    canv.drawPoint(i, j, p);
                }
            }
        }
    }

    private void drawLandscape(World world, Canvas canv) {
        int minY = Math.max(0, (int) YonMap(0) - 1);
        int maxY = Math.min(world.map.height, (int) YonMap(screenH) + 1);
        Rect r = new Rect();
        for (int y = minY; y < maxY; y++) {
            int minX = Math.max(0, (int) XonMap(0, 1 + y * dy - position.y));
            int maxX = Math.min(world.map.width, (int) XonMap(screenW, y * dy - position.y + 1) + 1);
            int yy = MapToY(y);
            for (int x = minX; x < maxX; x++) {
                int xx = MapToX(x, y);
                r.set(xx - 1, yy - 1, xx + (int) (w + 1), yy + (int) (h + 1));  //рисуем немного внахлёст
                world.map.getCell(x, y).render(canv, r);
            }
        }
    }

    private void drawUnits(World world, Canvas canvas) {
        int minY = Math.max(0, (int) YonMap(0) - 1);
        int maxY = Math.min(world.map.height, (int) YonMap(screenH) + 1);
        Rect r = new Rect();
        for (int y = minY; y < maxY; y++) {
            int minX = Math.max(0, (int) XonMap(0, 1 + y * dy - position.y));
            int maxX = Math.min(world.map.width, (int) XonMap(screenW, y * dy - position.y + 1) + 1);
            int yy = MapToY(y);
            for (int x = minX; x < maxX; x++) {
                Cell c = world.map.getCell(x, y);
                if (!c.hasUnit()) {
                    continue;
                }
                int xx = MapToX(x, y);
                r.set(xx - 1, yy - 1, xx + (int) (w + 1), yy + (int) (h + 1));  //рисуем немного внахлёст
                c.getUnit().render(canvas, r);
            }
        }
    }

    private void drawBorders(Canvas canv) {
        int minY = (int) YonMap(0) - 1;
        int maxY = (int) YonMap(screenH) + 1;
        for (int y = minY; y < maxY; y++) {
            int minX = (int) XonMap(0, y * dy - position.y + 1);
            int maxX = (int) XonMap(screenW, y * dy - position.y + 1) + 1;
            float yy = y * dy - position.y;
            for (int x = minX; x < maxX; x++) {
                float xx = x * w - y * w * 0.5f - position.x;
                canv.drawLine(xx, yy + h / 4, xx + w / 2, yy, p);
                canv.drawLine(xx + w / 2, yy, xx + w, yy + h / 4, p);
                canv.drawLine(xx, yy + h / 4, xx, yy + dy, p);
            }
        }
    }

    //если камера уползла за край карты, двигаем её обратно
    private void checkPosition(int ViewWidth, int ViewHeight) {
        if (position.x < 0)
            position.x = 0;
        if (position.y < 0)
            position.y = 0;
        //TODO проверку на правый и нижний края
    }
}
