package game.main.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
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

    public SpriteBank(Resources resources){
        Bitmap landS, landL, units;

        sprites = new TreeMap<String, Sprite>();

        landS = Sprite.loadBmp(resources, R.drawable.lands);
        landL = Sprite.loadBmp(resources, R.drawable.landl);
        units = Sprite.loadBmp(resources, R.drawable.xz2);

        Sprite[] arr=Sprite.fromBmp(landS, 4, 192, 128, 0);
        sprites.put("grass", arr[0]);
        sprites.put("hill", arr[1]);
        sprites.put("village", arr[2]);
        sprites.put("castle", arr[3]);

        arr = Sprite.fromBmp(landL, 1, 240, 160, 0);
        sprites.put("forest", arr[0]);

        arr = Sprite.fromBmp(units, 1, 192, 128, 0);
        sprites.put("crusader", arr[0]);
    }

    public Sprite get(String name){
        return sprites.get(name);
    }
}
