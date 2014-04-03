package game.main.v;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vova on 01.04.14.
 */
class HelpScreen extends View {
    Bitmap background;
    MenusActivity menusActivity;
    Paint paint;
    public HelpScreen(Context context, AssetManager assetManager){
        super(context);
        paint = new Paint();
        this.menusActivity = (MenusActivity)context;
        this.setOnTouchListener(new MyListener());
        try{

            InputStream inputStream
                    = assetManager.open("background.png");
            background = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (IOException e){
            //
        }
    }


    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(background,150,150,null);

        paint.setColor(Color.BLUE);
        paint.setTextSize(25);
        canvas.drawText("touch the screen and you will return to Main Menu",50,50,paint);
    }


    class MyListener implements OnTouchListener{
        public boolean onTouch(View view, MotionEvent event) {
            // событие
            //int actionMask = event.getActionMasked();
            // индекс касания
            //int pointerIndex = event.getActionIndex();
            // число касаний
            //int pointerCount = event.getPointerCount();

            menusActivity.closeHelp();
            return true;
        }
    }
}
