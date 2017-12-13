package es.uniovi.visitafacultad;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static String MENSAJE_ACABADO = "MENSAJE_ACABADO";
    private static int NUMERO_VIDEOS = 3;
    private int videoActual = 1;

    public HashMap<Integer, String> videos = new HashMap<>();
    private VrVideoView mVrVideoView;
    private VideoLoaderTask mBackgroundVideoLoaderTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        buscarComponentes();
        rellenarListaVideos();

        mBackgroundVideoLoaderTask = new VideoLoaderTask();
        mBackgroundVideoLoaderTask.execute();

        //mVrVideoView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_STEREO);
        mVrVideoView.playVideo();
    }

    private void buscarComponentes() {
        mVrVideoView = (VrVideoView) findViewById(R.id.video_view);

        mVrVideoView.setEventListener(new ActivityEventListener());
    }

    private void rellenarListaVideos() {
        for (int i = 1; i <= NUMERO_VIDEOS; i++) {
            videos.put(i, "video"+i);
        }
    }

    private class ActivityEventListener extends VrVideoEventListener {
        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
        }

        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
        }

        @Override
        public void onClick() {

        }

        @Override
        public void onNewFrame() {
            super.onNewFrame();
        }

        @Override
        public void onCompletion() {
            super.onCompletion();
            Comunicacion.getInstance().sendTweet(MENSAJE_ACABADO);
            Toast.makeText(getApplicationContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();

            String eleccion = null;

            while (eleccion == null) {
                try {
                    Thread.sleep(3000);
                    eleccion = Comunicacion.getInstance().readTweet();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(getApplicationContext(), eleccion, Toast.LENGTH_SHORT).show();

            if (eleccion.contains("IZQUIERDA"))
                videoActual++;
            else if (eleccion.contains("DERECHA"))
                videoActual += 2;

            ejecutarVideo();
        }
    }

    private void ejecutarVideo() {
        mVrVideoView.pauseVideo();
        mVrVideoView.seekTo(0);
        mBackgroundVideoLoaderTask = new VideoLoaderTask();
        mBackgroundVideoLoaderTask.execute();
    }

    class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                VrVideoView.Options options = new VrVideoView.Options();
                options.inputType = VrVideoView.Options.TYPE_MONO;

                mVrVideoView.loadVideoFromAsset(videos.get(videoActual)+".mp4", options);
            } catch( IOException e ) {
                e.printStackTrace();
            }

            return true;
        }
    }

    public void dialogoPrueba() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿A donde quieres ir ahora?")
                .setPositiveButton("Derecha", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        videoActual++;
                        mVrVideoView.pauseVideo();
                        mVrVideoView.seekTo(0);
                        mBackgroundVideoLoaderTask = new VideoLoaderTask();
                        mBackgroundVideoLoaderTask.execute();
                    }
                })
                .setNegativeButton("Izquierda", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        videoActual+=2;
                        mVrVideoView.pauseVideo();
                        mVrVideoView.seekTo(0);
                        mBackgroundVideoLoaderTask = new VideoLoaderTask();
                        mBackgroundVideoLoaderTask.execute();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVrVideoView.pauseRendering();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVrVideoView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        mVrVideoView.shutdown();
        super.onDestroy();
    }
}
