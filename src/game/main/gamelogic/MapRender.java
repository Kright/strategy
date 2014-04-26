package game.main.gamelogic;

import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.GUI.GamePanel;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.MapActivity;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;
import game.main.gamelogic.world.Settlement;
import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;

import java.io.Serializable;
import java.util.Iterator;

/**
 * объект, который рисует карту и эффекты на ней
 * Created by lgor on 20.02.14.
 */
public class MapRender extends MapCamera implements Serializable{

    private final RenderParams renderParams;
    private final Sprite[] roads;

    public MapRender(int spriteHeight, Sprite[] roads) {
        super(spriteHeight / 2 * 3, spriteHeight);
        renderParams = new RenderParams(new Paint());
        renderParams.paint.setTypeface(MapActivity.font);
        renderParams.paint.setTextSize(36);
        renderParams.paint.setColor(0xFF000055);
        this.roads=roads;
    }

    public void render(GameSession session, Map map, Canvas canv, GamePanel panel) {
        setScreenSize(panel.getFreeRight(canv.getWidth()), canv.getHeight());
        checkPosition(screenW, screenH, map.width * w, map.height * dy + h - dy);

        renderParams.setCellSize((int)getCellWidth()+1,(int)getCellHeight()+1 );
        renderParams.canvas = canv;

        canv.drawColor(0xFF444444); //фон
        drawLandscapeAndRoads(map, renderParams);
        if (session.properties.renderBorders) {
            drawBorders(canv);
        }
        drawFlora(map, renderParams);
        for (iRenderFeature rf : session.currentPlayer.getRenderFeatures()) {
            rf.render(this, canv);
        }
        drawUnitsAndShadows(map, renderParams);
        if (session.properties.showFPS)
            canv.drawText("fps=" + fps.get(), 20, 30, renderParams.paint);
    }

    private void drawLandscapeAndRoads(Map map, RenderParams renderParams) {
        Iterator<Cell> iter = getIterator(map, renderParams);
        while (iter.hasNext()) {
            Cell cell = iter.next();
            cell.render(renderParams);
            if (cell.hasRoad()){
                int number = map.getRoads(cell)-1;
                if (number>=0){
                    roads[number].render(renderParams);
                }
            }
        }
    }

    private void drawFlora(Map map, RenderParams renderParams) {
        Iterator<Cell> iter = getIterator(map, renderParams);
        while (iter.hasNext()) {
            iter.next().nextRender.render(renderParams);
        }
    }

    private void drawUnitsAndShadows(Map map, RenderParams renderParams) {
        Iterator<Cell> iter = getIterator(map, renderParams);
        while (iter.hasNext()) {
            Cell cell = iter.next();
            if (cell.shadowded) {
                Settlement.shadow.render(renderParams);
            } else{
                if (cell.hasUnit()){
                    cell.getUnit().render(renderParams);
                }
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
                canv.drawLine(xx, yy + h / 4, xx + w / 2, yy, renderParams.paint);
                canv.drawLine(xx + w / 2, yy, xx + w, yy + h / 4, renderParams.paint);
                canv.drawLine(xx, yy + h / 4, xx, yy + dy, renderParams.paint);
            }
        }
    }
}
