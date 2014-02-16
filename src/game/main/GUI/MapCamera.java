package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import game.main.gamelogic.Cell;
import game.main.gamelogic.GameProperties;
import game.main.gamelogic.World;

/**
 * Created by lgor on 17.01.14.
 */
public class MapCamera {

    private Point position = new Point(); //левый верхний угол экрана
    private float dy, h, w;
    private FPS fps = new FPS();
    private int screenH, screenW;

    public MapCamera(int spriteWidth, int spriteHeight) {
        h = spriteHeight;
        w = spriteWidth;
        dy = spriteHeight * 3 / 4;
    }

    /**
     * масштабирование относительно левого угла экрана
     */
    public void scale(float scale) {
        h *= scale;
        w *= scale;
        dy = h * 3 / 4;
        position.x *= scale;
        position.y *= scale;
    }

    /**
     * масштабирование относительно точки position.x+cx, position.y+cy;
     */
    public void scale(float scale, float cx, float cy) {
        h *= scale;
        w *= scale;
        dy = h * 3 / 4;
        position.x = (int) (position.x * scale + cx * (scale - 1));
        position.y = (int) (position.y * scale + cy * (scale - 1));
    }

    //если камера уползла за край карты, двигаем её обратно
    private void checkPosition(int ViewWidth, int ViewHeight) {
        if (position.x < 0)
            position.x = 0;
        if (position.y < 0)
            position.y = 0;
        //TODO проверку на правый и нижний края
    }

    final float YonMap(float y) {
        return (y + position.y) / dy;
    }

    final float XonMap(float x, float y) {
        int delta = (int) YonMap(y); //целая часть числа
        return (x + position.x) / w + delta * 0.5f;
    }

    final public void move(float dx, float dy) {
        position.x += dx;
        position.y += dy;
    }

    public void render(World world, Canvas canv, GameProperties properties) {
        screenH = canv.getHeight();
        screenW = canv.getWidth();
        checkPosition(screenW, screenH);
        canv.drawColor(0xFFFF00FF); //фон
        drawLandscape(world, canv);
        if (properties.renderBorders)
            drawBorders(canv);
        canv.drawText("fps=" + fps.get(), 20, 20, p);
    }

    private Paint p = new Paint();{
        p.setTextSize(p.getTextSize() * 2);
    }

    private void drawLandscape(World world, Canvas canv) {
        int minY = Math.max(0, (int) YonMap(0) - 1);
        int maxY = Math.min(world.map.height, (int) YonMap(screenH) + 1);
        for (int y = minY; y < maxY; y++) {
            int minX = Math.max(0, (int) XonMap(0, 1 + y * dy - position.y));
            int maxX = Math.min(world.map.width, (int) XonMap(screenW, y * dy - position.y + 1) + 1);
            Rect r = new Rect();
            Cell[] line = world.map.table[y];
            int yy = (int) (y * dy - position.y);
            for (int x = minX; x < maxX; x++) {
                if (line[x] == null)
                    continue;
                int xx = (int) (x * w - y * w * 0.5f - position.x);
                r.set(xx - 1, yy - 1, xx + (int) (w + 1), yy + (int) (h + 1));  //рисуем немного внахлёст
                line[x].render(canv, r);
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
}
