package com.vk.lgorsl.utils.sprites;

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

    protected final Resources resources;
    protected final BitmapFactory.Options bmpOptions;
    private List<Data> spritesData = new ArrayList<Data>(32);
    private Data[][] borders = new Data[8][12];

    public SpriteBank(Resources resources) {
        this.resources = resources;
        bmpOptions = new BitmapFactory.Options();
        bmpOptions.inScaled = false;

        addLine(R.drawable.lands, 192, 128, 192, 0, new String[]{"grass", "hill", "village", "castle", "shadow", "windmill", "field"});
        add(R.drawable.mountains, 250, 160, "mountain", new int[]{0, -32, 192, 128} );
        add(R.drawable.landl, 240, 160, "forest", new int[]{0, -32, 192, 128});
        add(R.drawable.xz2, 192, 128, "crusader");

        addLine(R.drawable.roads, 312, 120, 0, 120, new String[]{
                "road100", "road010", "road001", "road110", "road101", "road011", "road111"},
                new int[]{-12, -44, 192, 128});

        /*
        add(R.drawable.arrows, 0, 0, 64, 64, "↗", new int[]{96 + 16, -16, 192, 128});
        add(R.drawable.arrows, 128, 0, 128, 64, "→", new int[]{128, 32, 192, 128});
        add(R.drawable.arrows, 64, 0, 64, 64, "↘", new int[]{96 + 16, 64 + 16, 192, 128});
        */

        add(R.drawable.rounds, 0, 0, 64, 64, "↗", new int[]{96 + 16, -16, 192, 128});
        add(R.drawable.rounds, 0, 64, 128, 64, "→", new int[]{128, 32, 192, 128});
        add(R.drawable.rounds, 64, 0, 64, 64, "↘", new int[]{96 + 16, 64 + 16, 192, 128});

        add(R.drawable.rounds, 0, 128, 128, 128, "buttonZz");
        add(R.drawable.rounds, 0, 256, 128, 128, "buttonShield");
        add(R.drawable.rounds, 0, 384, 128, 128, "buttonCastle");
        add(R.drawable.rounds, 0, 512, 128, 128, "buttonShadow");

        add(R.drawable.circles4, 400, 0, 320, 1080, "rightButtonsPanel");

        int Rc = R.drawable.circles4;
        for (int i = 0; i < borders.length; i++) {
            borders[i][0] = new Data("otop", Rc, 48, i * 128 + 8, 96, 16, new int[]{48, 8, 192, 128});
            borders[i][1] = new Data("otop&right", Rc, 96 + 48, i * 128 + 16, 48, 48, new int[]{48 + 96, 16, 192, 128});
            borders[i][2] = new Data("obottom&right", Rc, 96 + 48, i * 128 + 64, 48, 48, new int[]{48 + 96, 16 + 48, 192, 128});
            borders[i][3] = new Data("obottom", Rc, 48, i * 128 + 104, 96, 16, new int[]{48, 104, 192, 128});
            borders[i][4] = new Data("obottom&left", Rc, 0, i * 128 + 64, 48, 48, new int[]{0, 64, 192, 128});
            borders[i][5] = new Data("otop&left", Rc, 0, i * 128 + 16, 48, 48, new int[]{0, 16, 192, 128});

            borders[i][6] = new Data("Otop", Rc, 192 + 56, i * 128, 96, 16, new int[]{48, 0, 192, 128});
            borders[i][7] = new Data("Otop&right", Rc, 192 + 8 + 96 + 48, i * 128 + 8, 56, 56, new int[]{96 + 48, 8, 192, 128});
            borders[i][8] = new Data("Obottom&right", Rc, 192 + 8 + 96 + 48, i * 128 + 64, 56, 56, new int[]{96 + 48, 64, 192, 128});
            borders[i][9] = new Data("Obottom", Rc, 192 + 56, i * 128 + 128 - 16, 96, 16, new int[]{48, 128 - 16, 192, 128});
            borders[i][10] = new Data("Obottom&left", Rc, 192, i * 128 + 64, 56, 56, new int[]{-8, 64, 192, 128});
            borders[i][11] = new Data("Otop&left", Rc, 192, i * 128 + 8, 56, 56, new int[]{-8, 8, 192, 128});

            for (int j = 0; j < 12; j++) {
                spritesData.add(borders[i][j]);
            }
        }
        load();
    }

    /**
     * reloading bitmaps, на которые ссылаются прямоугольники
     */
    public void load() {
        for (Data s : spritesData) {
            if (s.sprite.bmp != null) {
                try {
                    s.sprite.bmp.recycle();
                } catch (Exception ex) {
                    //nothing
                } finally {
                    s.sprite.bmp = null;
                }
            }
        }
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

    protected void add(int bmpId, int x, int y, int w, int h, String name) {
        spritesData.add(new Data(name, bmpId, x, y, w, h));
    }

    protected void add(int bmpId, int w, int h, String name, int[] k) {
        spritesData.add(new Data(name, bmpId, 0, 0, w, h, k));
    }

    protected void add(int bmpId, int x, int y, int w, int h, String name, int[] k) {
        spritesData.add(new Data(name, bmpId, x, y, w, h, k));
    }

    public iRender getSprite(String name) {
        //Я знаю про бинарный поиск, но так проще, а функция вызывается всего лишь несколько раз
        for (Data sd : spritesData) {
            if (sd.name.equals(name)) {
                return sd.sprite;
            }
        }
        throw new IllegalArgumentException("Sprite with name \"" + name + "\" doesn't exists");
    }

    public iRender[][] getCircles() {
        iRender[][] result = new iRender[borders.length][borders[0].length];
        for (int i = 0; i < borders.length; i++) {
            for (int j = 0; j < borders[0].length; j++) {
                result[i][j] = borders[i][j].sprite;
            }
        }
        return result;
    }
}

