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

public class MainActivity extends AppCompatActivity {
    public static String MENSAJE_ACABADO = "MENSAJE_ACABADO";
    public boolean comenzado = false;
    private Video videoActual;

    private VrVideoView mVrVideoView;
    private VideoLoaderTask mBackgroundVideoLoaderTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        buscarComponentes();

        videoActual = ArbolVideo.getInstance().getRaiz();

        mBackgroundVideoLoaderTask = new VideoLoaderTask();
        mBackgroundVideoLoaderTask.execute();

        //mVrVideoView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_STEREO);
        mVrVideoView.pauseVideo();
    }

    private void buscarComponentes() {
        mVrVideoView = (VrVideoView) findViewById(R.id.video_view);

        mVrVideoView.setEventListener(new ActivityEventListener());
    }

    private class ActivityEventListener extends VrVideoEventListener {
        @Override
        public void onLoadSuccess() {

            super.onLoadSuccess();

            if (comenzado) {
                mVrVideoView.playVideo();
            } else {
                comenzado = true;
            }
        }

        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);
        }

        @Override
        public void onClick() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mVrVideoView.playVideo();
        }

        @Override
        public void onNewFrame() {
            super.onNewFrame();
        }

        @Override
        public void onCompletion() {
            super.onCompletion();
            Comunicacion.getInstance().sendTweet(MENSAJE_ACABADO);
            //dialogo();

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
                videoActual = videoActual.getIzquierda();
            else if (eleccion.contains("DERECHA"))
                videoActual = videoActual.getDerecha();

            ejecutarVideo();
        }
    }

    private void ejecutarVideo() {
        mBackgroundVideoLoaderTask = new VideoLoaderTask();
        mBackgroundVideoLoaderTask.execute();
        mVrVideoView.pauseVideo();
        mVrVideoView.seekTo(0);
        //mVrVideoView.playVideo();
    }

    class VideoLoaderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                VrVideoView.Options options = new VrVideoView.Options();
                options.inputType = VrVideoView.Options.TYPE_MONO;

                mVrVideoView.loadVideoFromAsset(videoActual.getNombre()+".mp4", options);

                /*if (videoActual.getId() != 1L) {
                    mVrVideoView.pauseVideo();
                    mVrVideoView.seekTo(0);
                    mVrVideoView.playVideo();
                }*/
            } catch( IOException e ) {
                e.printStackTrace();
            }

            return true;
        }
    }

    public void dialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿A donde quieres ir ahora? Solo tienes que moverte")
                .setPositiveButton("Derecha", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Izquierda", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

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
