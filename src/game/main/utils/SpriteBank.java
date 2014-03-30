package game.main.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.R;

import java.util.Map;
import java.util.TreeMap;

/**
 * класс, ответственный за загрузку и сохранение спрайтов
 * В частности, при загрузке игры спарйты берутся отсюда
 * Created by lgor on 23.03.14.
 */
public class SpriteBank {

    private Map<String, Sprite> sprites;
    private Sprite[] roads;

    public SpriteBank(Resources resources) {
        Bitmap landS, landL, units;

        sprites = new TreeMap<String, Sprite>();

        landS = Sprite.loadBmp(resources, R.drawable.lands);
        landL = Sprite.loadBmp(resources, R.drawable.landl);
        units = Sprite.loadBmp(resources, R.drawable.xz2);

        Sprite[] arr = Sprite.fromBmp(landS, 5, 192, 128, 0);
        sprites.put("grass", arr[0]);
        sprites.put("hill", arr[1]);
        sprites.put("village", arr[2]);
        sprites.put("castle", arr[3]);
        sprites.put("shadow", arr[4]);

        arr = Sprite.fromBmp(landL, 1, 240, 160, 0);
        sprites.put("forest", arr[0]);

        arr = Sprite.fromBmp(units, 1, 192, 128, 0);
        sprites.put("crusader", arr[0]);

        sprites.put("game panel" ,Sprite.fromBmp(Sprite.loadBmp(resources, R.drawable.menu),1,320, 1080, 0)[0]);

        arr = Sprite.fromBmpVert(Sprite.loadBmp(resources, R.drawable.roads), 120);
        roads = new Sprite[7];
        roads[0] = arr[0];
        roads[1] = arr[1];
        roads[3] = arr[2];
        roads[2] = arr[3];
        roads[4] = arr[4];
        roads[5] = arr[5];
        roads[6] = arr[6];
    }

    public Sprite get(String name) {
        return sprites.get(name);
    }

    public Sprite[] getRoads(){
        return roads;
    }

    public Bitmap getWithEffects(Bitmap sample, Paint paint) {
        Bitmap bmp = Bitmap.createBitmap(sample.getWidth(), sample.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(sample, 0, 0, paint);
        return bmp;
    }
}
