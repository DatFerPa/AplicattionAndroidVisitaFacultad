package es.uniovi.visitafacultad;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by eduardomartinez on 7/12/17.
 */

public class Comunicacion {
    private static final Comunicacion instance = new Comunicacion();
    private final Twitter twitter;
    private String estadoActual;
    private TweetLoaderTask mBackgroundTweetLoaderTask;
    private ReadTweetLoaderTask mBackgroundReadTweetLoaderTask;
    private String mensajeActual;

    public Comunicacion() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("fYlLXyJhczJ6EUrgmrxf7cf3i");
        cb.setOAuthConsumerSecret("L5CKEAt0Ew8qAfFQ8py32dNEgG1ICSFO6otmbmcUv8sgXJRFqJ");
        cb.setOAuthAccessToken("933360447432032258-eoRThDIKqNBDzPFARkdAGsqtligj3cK");
        cb.setOAuthAccessTokenSecret("dSrvTEYvbtNxHhSL7RGamC4h6P4KMp05MB8HPUekPzRsi");

        TwitterFactory tf = new TwitterFactory(cb.build());

        twitter = tf.getInstance();
    }

    public static Comunicacion getInstance() {
        return instance;
    }

    public void sendTweet(String msg) {
        try
        {
            mensajeActual = msg;
            mBackgroundTweetLoaderTask = new TweetLoaderTask();
            mBackgroundTweetLoaderTask.execute();
        }
        catch (Exception e)
        {
            System.out.println("Error: "+ e.getMessage());
        }
    }

    public String readTweet(){
        try {
            mBackgroundReadTweetLoaderTask = new ReadTweetLoaderTask();
            mBackgroundReadTweetLoaderTask.execute();
        } catch( Exception ex) {
            System.out.println("Error"+ex.getMessage());
        }

        while (estadoActual == null) {
            System.out.println("Esperando");
        }

        if (estadoActual.contains(MainActivity.MENSAJE_ACABADO))
            return null;

        return estadoActual;
    }

    class TweetLoaderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                twitter4j.Status status = twitter.updateStatus(mensajeActual+System.currentTimeMillis());
                System.out.println("Status updated to [" + status.getText() + "].");
            } catch(Exception e ) {
                e.printStackTrace();
            }

            return true;
        }
    }

    class ReadTweetLoaderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                List<twitter4j.Status> statusList = twitter.getUserTimeline();
                estadoActual = String.valueOf(statusList.get(0).getText());
            } catch(Exception e ) {
                e.printStackTrace();
            }

            return true;
        }
    }
}
