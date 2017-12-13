package es.uniovi.visitafacultad;

/**
 * Created by eduardomartinez on 13/12/17.
 */

public class Video {
    private Long id;
    private String nombre;
    private Video izquierda;
    private Video derecha;

    public Video (Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    public Long getId(){
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIzquierda(Video izquierda) {
        this.izquierda = izquierda;
    }

    public void setDerecha(Video derecha) {
        this.derecha = derecha;
    }

    public Video getIzquierda() {
        return izquierda;
    }

    public Video getDerecha() {
        return derecha;
    }
}
