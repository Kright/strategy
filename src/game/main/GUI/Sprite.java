package game.main.GUI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by lgor on 17.01.14.
 */
public class Sprite{

    public final Bitmap bmp;
    public final Rect rect;

    public Sprite(Bitmap bmp, int x, int y, int width, int height){
        this.bmp=bmp;
        rect = new Rect(x,y,x+width,y+height);
    }

    public static Sprite[] loadHorisontalN(Resources res, int id, int count){
        Bitmap bmp = loadBmp(res, id);
        return fromBmp(bmp,count, bmp.getWidth()/count, bmp.getHeight());
    }

    public static Sprite[] loadHorisontalWithWidth(Resources res, int id, int width){
        Bitmap bmp = loadBmp(res, id);
        return fromBmp(bmp, bmp.getWidth()/width, bmp.getWidth(), bmp.getHeight());
    }

    private static Bitmap loadBmp(Resources res, int id){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inScaled=false;
        Bitmap bmp= BitmapFactory.decodeResource(res, id, options);
        Log.d("mylog", "Bmp loaded as "+bmp.getWidth()+"x"+bmp.getHeight());
        return bmp;
    }

    private static Sprite[] fromBmp(Bitmap bmp, int count, int w, int h){
        Sprite[] sp=new Sprite[count];
        for(int i=0;i<count;i++){
            sp[i]=new Sprite(bmp, w*i,0,w,h);
        }
        return sp;
    }
}
