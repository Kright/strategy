package game.main.gamelogic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.GamePanel;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.gamelogic.world.Map;
import game.main.gamelogic.world.Settlement;
import game.main.utils.Sprite;

import java.util.Iterator;

/**
 * объект, который рисует карту и эффекты на ней
 * Created by lgor on 20.02.14.
 */
public class MapRender extends MapCamera {

    private final Paint p = new Paint();
    private final Sprite[] roads;

    public MapRender(int spriteHeight, Sprite[] roads) {
        super(spriteHeight / 2 * 3, spriteHeight);
        p.setTextSize(p.getTextSize() * 2);
        this.roads=roads;
    }

    public void render(GameSession session, Map map, Canvas canv, GamePanel panel) {
        setScreenSize(panel.getFreeRight(canv.getWidth()), canv.getHeight());
        checkPosition(screenW, screenH, map.width * w, map.height * dy + h - dy);

        canv.drawColor(0xFF444444); //фон
        drawLandscapeAndRoads(map, canv, null);
        if (session.properties.renderBorders) {
            drawBorders(canv);
        }
        drawFlora(map, canv, null);
        drawShadows(map, canv, null);
        for (iRenderFeature rf : session.currentPlayer.getRenderFeatures()) {
            rf.render(this, canv);
        }
        drawUnits(map, canv, null);
        if (session.properties.showFPS)
            canv.drawText("fps=" + fps.get(), 20, 20, p);
    }

    private void drawLandscapeAndRoads(Map map, Canvas canv, Paint paint) {
        Rect road4 = new Rect();
        int w=(int)(this.getCellWidth()*312/192);
        int h=(int)(this.getCellHeight()*120/128);
        int dx = -(int)(this.getCellWidth()*12/192);
        int dy = -(int)(this.getCellHeight()*44/128);

        Iterator<RenderObject> iter = getIterator(map);
        while (iter.hasNext()) {
            RenderObject ro = iter.next();
            int left = ro.rect.left+dx;
            int top= ro.rect.top+dy;
            ro.cell.render(canv, ro.rect, paint);
            if (ro.cell.hasRoad()){
                int number = map.getRoads(ro.cell)-1;
                if (number>=0){
                    road4.set(left,top,left+w, top+h);
                    canv.drawBitmap(roads[number].bmp, roads[number].rect, road4, paint);
                    //canv.drawBitmap(roads[number].bmp, roads[number].rect, roads[number].rect, paint);
                }
            }
        }
    }

    private void drawFlora(Map map, Canvas canv, Paint paint) {
        Iterator<RenderObject> iter = getIterator(map);
        while (iter.hasNext()) {
            RenderObject ro = iter.next();
            ro.cell.nextRender.render(canv, ro.rect, paint);
        }
    }

    private void drawShadows(Map map, Canvas canv, Paint paint) {
        Iterator<RenderObject> iter = getIterator(map);
        while (iter.hasNext()) {
            RenderObject ro = iter.next();
            if (ro.cell.shadowded) {
                canv.drawBitmap(Settlement.shadow.bmp, Settlement.shadow.rect, ro.rect, paint);
            }
        }
    }

    private void drawUnits(Map map, Canvas canvas, Paint paint) {
        Iterator<RenderObject> iter = getIterator(map);
        while (iter.hasNext()) {
            RenderObject ro = iter.next();
            if (ro.cell.hasUnit()) {
                ro.cell.getUnit().render(canvas, ro.rect, paint);
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
}
