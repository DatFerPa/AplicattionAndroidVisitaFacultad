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
        Video presentacion = new Video(1L, "presentacion");
        Video hall = new Video(2L, "hall");
        Video lobitos = new Video(3L, "lobitos");
        Video macs = new Video(4L, "macs");
        Video primeraPlanta = new Video(5L, "primeraPlanta");
        Video segundaPlanta = new Video(6L, "segundaPlanta");
        Video fin = new Video(7L, "fin");

        presentacion.setDerecha(lobitos);
        presentacion.setIzquierda(hall);

        hall.setIzquierda(macs);
        hall.setDerecha(primeraPlanta);

        macs.setIzquierda(presentacion);
        macs.setDerecha(presentacion);

        primeraPlanta.setIzquierda(segundaPlanta);
        primeraPlanta.setDerecha(presentacion);

        segundaPlanta.setIzquierda(fin);
        segundaPlanta.setDerecha(presentacion);

        fin.setIzquierda(presentacion);
        fin.setDerecha(presentacion);

        videos.put(presentacion.getId(), presentacion);
        videos.put(hall.getId(), hall);
        videos.put(lobitos.getId(), lobitos);
        videos.put(macs.getId(), macs);
        videos.put(primeraPlanta.getId(), primeraPlanta);
        videos.put(segundaPlanta.getId(), segundaPlanta);
        videos.put(fin.getId(), fin);
    }

}
