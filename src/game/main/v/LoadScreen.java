package game.main.v;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vova on 03.04.14.
 */
class LoadScreen extends View {
    AssetManager assetManager;
    MenusActivity menusActivity;

    Bitmap background;
    Bitmap startgame;
    Bitmap help;
    Rect dst1 = new Rect();
    Rect dst2 = new Rect();

    public LoadScreen(Context context, AssetManager _assetManager, MenusActivity _menusActivity) {
        super(context);
        assetManager = _assetManager;
        this.setOnTouchListener(new MyListener());
        menusActivity = _menusActivity;

        try{

            InputStream inputStream
                    = assetManager.open("background.png");
            background = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            inputStream = assetManager.open("startgame.png");
            startgame = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            inputStream = assetManager.open("help.png");
            help = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (IOException e){
            //
        }
    }

    protected void onDraw(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        canvas.drawColor(Color.GREEN);
        canvas.drawBitmap(background, 0, 0, null);
        int l,t,r,b;
        l = w/2;
        t = h/5;
        r=w-1;
        b=2*t;
        dst1.set(l,t,r,b);
        canvas.drawBitmap(startgame, null, dst1, null);
        b= 4*t;
        t = 3*t;
        dst2.set(l,t,r,b);
        canvas.drawBitmap(help,null, dst2, null);
    }


    class MyListener implements OnTouchListener{
        boolean isOnButtonGame = true;
        boolean isOnButtonHelp = true;

        public boolean onTouch(View view, MotionEvent event) {
            // событие
            int actionMask = event.getActionMasked();
            // индекс касания
            //int pointerIndex = event.getActionIndex();
            // число касаний
            int pointerCount = event.getPointerCount();

            if (pointerCount > 1 )
            {
                isOnButtonHelp = false;
                isOnButtonGame = false;
                if(actionMask == MotionEvent.ACTION_UP ){
                    isOnButtonHelp = true;
                    isOnButtonGame = true;}
                return true;
            }

            if(event.getPointerId(0) != 0)
            {
                if(actionMask == MotionEvent.ACTION_UP ){
                    isOnButtonHelp = true;
                    isOnButtonGame = true;}
                return true;
            }

            float x=event.getX();
            float y=event.getY();

            switch (actionMask) {
                case MotionEvent.ACTION_DOWN: // первое и пока единственное касание
                case MotionEvent.ACTION_POINTER_DOWN:
                    isOnButtonGame &= isInRect(x,y,dst1);
                    isOnButtonHelp &= isInRect(x,y,dst2);
                    break;

                case MotionEvent.ACTION_UP: // прерывание касания
                case MotionEvent.ACTION_POINTER_UP:

                    isOnButtonGame &= isInRect(x,y,dst1);
                    isOnButtonHelp &= isInRect(x,y,dst2);

                    if(isOnButtonGame) {
                        isOnButtonHelp =true;
                        menusActivity.goGame();
                        return true;
                    }
                    if(isOnButtonHelp) {
                        isOnButtonGame =true;
                        menusActivity.goHelp();
                        return true;
                    }
                    isOnButtonGame = true;
                    isOnButtonHelp = true;

                    break;

                case MotionEvent.ACTION_MOVE: // движение
                    isOnButtonGame &= isInRect(x,y,dst1);
                    isOnButtonHelp &= isInRect(x,y,dst2);
                    break;
            }
            return true;
        }
    }

    boolean isInRect(float x, float y , Rect rect){
        if( x> rect.left && x<rect.right)
            if(y>rect.top && y <rect.bottom)
                return true;
        return false;
    }

}
