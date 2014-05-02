package game.main.v;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * Created by user on 01.05.14.
 */
public class Musics  implements
        SoundPool.OnLoadCompleteListener , MediaPlayer.OnPreparedListener , MediaPlayer.OnCompletionListener{
//класс для работы со звуками и музыкой. Осталось написать методы для работы с музыкой, пауза, плэй
    // также надо эти методы заюзать в активити
    SoundPool sp;
    Context context;
    int soundIdExplosion;


    int streamIDExplosion;



    MediaPlayer mediaPlayer;
    AudioManager am;


    void startMusic(){
        releaseMP();
        try{
            Log.d("myTag", "start SD");
            mediaPlayer = new MediaPlayer();
            String DATA_SD = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                    + "/music.mp3";
            mediaPlayer.setDataSource(DATA_SD);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (mediaPlayer == null)
            return;

        mediaPlayer.setOnCompletionListener(this);
    }


    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {

            }
        }
    }
    void musicInit(){
        am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        mediaPlayer.setLooping(true);
    }

    void soundInit(){
        sp = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);



        try {
            soundIdExplosion = sp.load(context.getAssets().openFd("explosion.ogg"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void play(){
        sp.play(soundIdExplosion, 1, 1, 0, 0, 1);
        //параметры позволяют задать громкость, кол-во повторений, приоритет

        //остановка и возобновление
       // sp.pause(soundIdExplosion);
       // sp.resume(soundIdExplosion);


        //sp.autoPause(); все остановит
        //громкость  - setVolume
        //приоритет – setPriority
        //повторы - setLoop
        //скорость - setRate
    }

    private void unload(int id){
        unload(id);
        //выгружает звук из памяти
    }

    private void release(){
        sp.release();
        //выгружает все звуки, SoundPool уже не пригоден, ссылку на него надо сделать null
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("myLog", "onPrepared");
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("myLog", "onCompletion");
    }


    protected void destroyMP() {
        releaseMP();
    }

    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.d("myLog", "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }

}
