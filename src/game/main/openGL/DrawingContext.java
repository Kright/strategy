package game.main.openGL;

import android.content.res.Resources;
import game.main.GUI.MapCamera;
import game.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Через этот объект будут передаваться данные от потока логики потоку рисования
 * Created by lgor on 29.04.14.
 */
public class DrawingContext {

    //  флаг, поток логики устанавливает его в false, поток рисования - в true
    public volatile boolean repainted = false;

    public final Resources resources;

    protected int textureHandle;
    private final List<TextureSprite> sprites = new ArrayList<TextureSprite>();
    public final MapCamera camera;

    public DrawingContext(Resources resources) {
        this.resources = resources;
        camera = new MapCamera(192, 128);
        synchronized (sprites) {
            TextureSprite.SpriteFactory sp = new TextureSprite.SpriteFactory(1024);
            sp.setSpriteDefaultSize(192, 128);
            sp.addSpritesLine(sprites, 0, 0, 192, 0, new String[]
                    {"grass", "hill", "village", "castle", "shadow"});
        }
    }

    /**
     * вызывать только из потока OpenGL
     */
    public synchronized void loadTextures() {
        textureHandle = TextureSprite.loadTexture(resources, R.drawable.texture);
    }

    public TextureSprite getSprite(String name) {
        synchronized (sprites) {
            for (TextureSprite sprite : sprites) {
                if (sprite.name.equals(name)) {
                    return sprite;
                }
            }
        }
        throw new IllegalArgumentException("Sprite with name \"" + name + "\" doesn't exists");
    }
}
