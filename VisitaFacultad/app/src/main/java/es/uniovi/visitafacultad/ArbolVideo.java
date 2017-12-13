package es.uniovi.visitafacultad;

import java.util.HashMap;
import java.util.List;

/**
 * Created by eduardomartinez on 13/12/17.
 */

public class ArbolVideo {
    private HashMap<Long, Video> videos = new HashMap<>();
    private static final ArbolVideo instance = new ArbolVideo();

    public static ArbolVideo getInstance() {
        return instance;
    }

    public ArbolVideo() {
        crearArbolVideo();
    }

    public Video getRaiz() {
        return videos.get(1L);
    }

    private void crearArbolVideo() {
        Video video1 = new Video(1L, "video1");
        Video video2 = new Video(2L, "video2");
        Video video3 = new Video(3L, "video3");

        video1.setDerecha(video2);
        video1.setIzquierda(video3);

        video2.setIzquierda(video1);
        video2.setDerecha(video3);

        video3.setIzquierda(video1);
        video3.setDerecha(video2);

        videos.put(video1.getId(), video1);
        videos.put(video2.getId(), video2);
        videos.put(video3.getId(), video3);
    }

}
