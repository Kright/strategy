package game.main.gamelogic.world;


import android.graphics.*;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.utils.sprites.RenderParams;
import game.main.utils.sprites.Sprite;
import game.main.utils.sprites.SpriteBank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturor on 02.04.2014.
 * Рисуем стрелочки по пути, где идет Unit
 * NE - северо-восток
 * E - восток
 * SE - юго-восток
 * SW - юго-запад
 * W - запад
 * NW - северо-запад
 */


public class PathPaint implements iRenderFeature {
    public static Sprite[] arrows;
    protected List<Cell> path;
    protected Paint p;

    public PathPaint(List<Cell> path) {
        this.path = path;
        p = new Paint();
    }

    public void render(MapCamera camera, Canvas canvas) {
        int i;
        RectF r;
        float w;
        float h;
        float cx;
        float cy;

        for (i = 0; i <= path.size(); i++) {                  // проходим по всему пути

            if ((path.get(i + 1).x - path.get(i).x == 0) && (path.get(i + 1).y - path.get(i).y == -1)) {    // NE
                w = camera.getCellWidth();
                h = camera.getCellHeight();
                cx = camera.MapToX(path.get(i).x, path.get(i).y)+w/2;                                      // далее вместо w/2 и h/2 нужно будет аккуратно подобрать значения, чтобы стрелочка лежала между клетками
                cy = camera.MapToY(path.get(i).y) + h/2;
                float scale = 1.0f;                                                                       // let it be
                r = new RectF(cx - w * 0.5f*scale, cy - h * 0.433f*scale, cx + w * 0.5f*scale, cy + h * 0.433f*scale);

                canvas.drawBitmap(arrows[0].bmp, arrows[0].rect, r, p);
            }

            if ((path.get(i + 1).x - path.get(i).x == 1) && (path.get(i + 1).y - path.get(i).y == 0)) {    // E
                w = camera.getCellWidth();
                h = camera.getCellHeight();
                cx = camera.MapToX(path.get(i).x, path.get(i).y)+w/2;
                cy = camera.MapToY(path.get(i).y)+ h/2;
                float scale = 1.0f;
                r = new RectF(cx - w * 0.5f* scale, cy - h * 0.433f* scale, cx + w * 0.5f* scale, cy + h * 0.433f* scale);

                canvas.drawBitmap(arrows[1].bmp, arrows[1].rect, r, p);
            }

            if ((path.get(i + 1).x - path.get(i).x == 1) && (path.get(i + 1).y - path.get(i).y == 1)) {    // SE
                w = camera.getCellWidth();
                h = camera.getCellHeight();
                cx = camera.MapToX(path.get(i).x, path.get(i).y)+w/2;
                cy = camera.MapToY(path.get(i).y)+ h/2;
                float scale = 1.0f;
                r = new RectF(cx - w * 0.5f* scale, cy - h * 0.433f* scale, cx + w * 0.5f* scale, cy + h * 0.433f* scale);

                canvas.drawBitmap(arrows[2].bmp, arrows[2].rect, r, p);
            }

            if ((path.get(i + 1).x - path.get(i).x == 0) && (path.get(i + 1).y - path.get(i).y == 1)) {    // SW
                w = camera.getCellWidth();
                h = camera.getCellHeight();
                cx = camera.MapToX(path.get(i).x, path.get(i).y)+w/2;
                cy = camera.MapToY(path.get(i).y)+ h/2;
                float scale = 1.0f;
                r = new RectF(cx - w * 0.5f* scale, cy - h * 0.433f* scale, cx + w * 0.5f* scale, cy + h * 0.433f* scale);

                canvas.drawBitmap(arrows[3].bmp, arrows[3].rect, r, p);
            }

            if ((path.get(i + 1).x - path.get(i).x == -1) && (path.get(i + 1).y - path.get(i).y == 0)) {    // W
                w = camera.getCellWidth();
                h = camera.getCellHeight();
                cx = camera.MapToX(path.get(i).x, path.get(i).y)+w/2;
                cy = camera.MapToY(path.get(i).y)+ h/2;
                float scale = 1.0f;
                r = new RectF(cx - w * 0.5f* scale, cy - h * 0.433f* scale, cx + w * 0.5f* scale, cy + h * 0.433f* scale);

                canvas.drawBitmap(arrows[4].bmp, arrows[4].rect, r, p);
            }

            if ((path.get(i + 1).x - path.get(i).x == -1) && (path.get(i + 1).y - path.get(i).y == -1)) {    // NW
                w = camera.getCellWidth();
                h = camera.getCellHeight();
                cx = camera.MapToX(path.get(i).x, path.get(i).y)+w/2;
                cy = camera.MapToY(path.get(i).y)+ h/2;
                float scale = 1.0f;
                r = new RectF(cx - w * 0.5f* scale, cy - h * 0.433f* scale, cx + w * 0.5f* scale, cy + h * 0.433f* scale);

                canvas.drawBitmap(arrows[5].bmp, arrows[5].rect, r, p);
            }
        }
    }
}
