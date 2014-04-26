package game.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;
import game.main.gamelogic.GameSession;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.LandType;
import game.main.gamelogic.world.Map;
import game.main.gamelogic.world.PMap.MMapConstructor;
import game.main.utils.sprites.SpriteBank;
import game.main.v.Menus;

import java.io.*;

/**
 * активити самой игры
 * Created by lgor on 13.01.14.
 */
public class MapActivity extends Activity {
    public static String savePath =null;

    public static boolean newgame = true;
    public static volatile Typeface font;
    private static volatile GameThread thread = null;
    private static final Object monitor = new Object();

    private SurfaceView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (font != null) {
            font = Typeface.createFromAsset(getAssets(), "fonts/oleoscriptbold.ttf");
        }
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        /*
        setContentView(new MapView(this));
        */
        view = new SurfaceView(this);
        setContentView(view);

        openSessionFile();

        synchronized (monitor) {
            if (thread == null) {

                if(true) {
                    GameSession session = new GameSession(getResources());
                    session.createNewWorld(120, 120);

                    thread = new GameThread(monitor, session );
                    newgame = false;
                    }
                else {
                    thread = new GameThread(monitor, getResources());
                }
            }
        }
    }


    protected  void openSessionFile(){
       FileOutputStream fos;
        try{
            File f = getFileStreamPath("session");

            if(newgame & f.exists() )deleteFile("session");
            fos= openFileOutput("session",MODE_PRIVATE);


            ObjectOutputStream oos = new ObjectOutputStream(openFileOutput("session", MODE_PRIVATE));
            oos.writeObject("str4");


            LandType lt=new LandType((new SpriteBank(getResources())).getSprite("grass"), 2, "Поле");
            Cell c = new Cell(5,5,lt);

            oos.writeObject(c);



            GameSession ss = new GameSession(getResources());
            ss.createNewWorld(120,120);
            long time = System.currentTimeMillis();
            oos.writeObject(ss.world);
            time = System.currentTimeMillis()-time;
            Log.d("my",time+"ms");

            oos.writeObject("str6");
            oos.flush();
            oos.close();

       }
       catch (IOException e){
           e.printStackTrace();
       }

        try{
            File f = getFileStreamPath("session");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            //String s = (String)ois. readObject();
            //Cell c = (Cell)ois.readObject();

            ois.close();

        }
        //catch (ClassNotFoundException e ){}
        catch (IOException e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        synchronized (monitor) {
            thread.resume(view.getHolder(), getResources());
            view.setOnTouchListener(thread);

            monitor.notifyAll();
        }
    }

    @Override
    protected void onPause() {

        thread.setWaiting();
        while (!thread.isWaiting()) {
            Thread.yield();
        }
        super.onPause();
    }

    public void onBackPressed() {
        Menus.playing = true;
        Intent intent = new Intent(this, Menus.class);

        startActivity(intent);
    }
}
