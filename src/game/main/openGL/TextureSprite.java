package game.main.openGL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import game.main.utils.sprites.Sprite;

import java.util.List;

/**
 * описывает картинку на кусочке текстуры.
 * Фактически, это нужно для рисования и прочего
 *
 * для спрайта в верхнем левом углу текстуры
 * yTop = 0.0, yBottom >0;
 *
 * Created by lgor on 30.04.14.
 */
public class TextureSprite {

    public final String name;
    float xLeft, yBottom, xRight, yTop;

    public TextureSprite(String name, float xLeft, float yTop, float xRight, float yBottom) {
        this.name = name;
        this.xLeft = xLeft;
        this.yBottom = yBottom;
        this.xRight = xRight;
        this.yTop = yTop;
    }

    public static class SpriteFactory {
        public final int textureSize;
        private int width, height;

        public SpriteFactory(int textureSize) {
            this.textureSize = textureSize;
        }

        /**
         * @param width  of sprite in pixels
         * @param height of sprite in pixels
         */
        public void setSpriteDefaultSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * @param name of sprite
         * @param x    coordinate of left side in pixels
         * @param y    of bottom in pixels
         * @return TextureSprite with default size
         */
        public TextureSprite getSprite(String name, int x, int y) {
            return getSprite(name, x, y, width, height);
        }

        public TextureSprite getSprite(String name, int x, int y, int w, int h) {
            return new TextureSprite(name, (float) x / textureSize, (float) y / textureSize,
                    (float) (x + w) / textureSize, (float) (y + h) / textureSize);
        }

        public void addSpritesLine(List<TextureSprite> list, int x, int y, int dx, int dy, String[] names) {
            for (int i = 0; i < names.length; i++) {
                list.add(getSprite(names[i], x, y));
                x += dx;
                y += dy;
            }
        }
    }

    /*
    * didn't tested
    */
    public static int loadTexture(Resources resources, final int resourceId) {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);
        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId, options);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        bitmap.recycle();
        return textureHandle[0];
    }
}
