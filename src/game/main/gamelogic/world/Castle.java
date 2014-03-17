package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.utils.Sprite;

import java.util.ArrayList;

/**
 * замок
 * Created by lgor on 14.03.14.
 */
public class Castle extends Settlement {
    protected static Sprite sprite;

    protected Region region;    //подконтрольная территория

    public Castle(Country country, Cell cell) {
        super(country, cell);
        ArrayList<Cell> near = new ArrayList<Cell>(7);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Map.getInterval(i, j) < 2) {
                    near.add(country.map.getCell(cell.x + i, cell.y + j));
                }
            }
        }
        region = new Region(near);
    }

    @Override
    public void nextTurn() {

    }

    @Override
    public int getTaxes(){
        return 1; //TODO
    }

    @Override
    public void render(Canvas canv, Rect r, Paint paint) {
        canv.drawBitmap(sprite.bmp, sprite.rect, r, paint);
    }
}
