package game.main.utils.sprites;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import game.main.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * класс, ответственный за загрузку и сохранение спрайтов
 * В частности, при загрузке игры спарйты берутся отсюда
 * Created by lgor on 23.03.14.
 */
public class SpriteBank {

    protected static class Data {
        Sprite sprite;
        final int bmpId;
        final String name;

        private Data(String name, int bmpId, int x, int y, int w, int h) {
            this.name = name;
            this.bmpId = bmpId;
            sprite = new Sprite(null, x, y, w, h);
        }

        private Data(String name, int bmpId, int x, int y, int w, int h, int[] k) {
            this.name = name;
            this.bmpId = bmpId;
            sprite = new AdvancedSprite(null, x, y, w, h, k[0], k[1], k[2], k[3]);
        }
    }

    private final Resources resources;
    private final BitmapFactory.Options bmpOptions;
    private List<Data> spritesData = new ArrayList<Data>(32);

    public SpriteBank(Resources resources) {
        this.resources = resources;
        bmpOptions = new BitmapFactory.Options();
        bmpOptions.inScaled = false;

        addLine(R.drawable.lands, 192, 128, 192, 0, new String[]{"grass", "hill", "village", "castle", "shadow"});
        add(R.drawable.landl, 240, 160, "forest", new int[]{0, -32, 192, 128});
        add(R.drawable.xz2, 192, 128, "crusader");
        add(R.drawable.menu, 320, 1080, "game panel");
        addLine(R.drawable.roads, 312, 120, 0, 120, new String[]{
                "road100", "road010", "road001", "road110", "road101", "road011", "road111"},
                new int[]{ -12, -44, 192, 128});
        load();
    }

    /**
     * reloading bitmaps, на которые ссылаются прямоугольники
     */
    public void load() {
        TreeMap<Integer, Bitmap> bmps = new TreeMap<Integer, Bitmap>();
        for (Data s : spritesData) {
            if (!bmps.containsKey(s.bmpId)) {
                bmps.put(s.bmpId, BitmapFactory.decodeResource(resources, s.bmpId, bmpOptions));
            }
            s.sprite.bmp = bmps.get(s.bmpId);
        }
    }

    protected void addLine(int bmpId, int w, int h, int dx, int dy, String[] names) {
        for (int i = 0; i < names.length; i++) {
            spritesData.add(new Data(names[i], bmpId, dx * i, dy * i, w, h));
        }
    }

    protected void addLine(int bmpId, int w, int h, int dx, int dy, String[] names, int[] k) {
        for (int i = 0; i < names.length; i++) {
            spritesData.add(new Data(names[i], bmpId, dx * i, dy * i, w, h, k));
        }
    }

    protected void add(int bmpId, int w, int h, String name) {
        spritesData.add(new Data(name, bmpId, 0, 0, w, h));
    }

    protected void add(int bmpId, int w, int h, String name, int[] k) {
        spritesData.add(new Data(name, bmpId, 0, 0, w, h, k));
    }

    public Sprite getSprite(String name) {
        //Я знаю про бинарный поиск, но так проще, а функция вызывается всего лишь несколько раз
        for (Data sd : spritesData) {
            if (sd.name.equals(name)) {
                return sd.sprite;
            }
        }
        throw new IllegalArgumentException("Sprite with name \"" + name + "\" doesn't exists");
    }
}

