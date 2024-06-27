package co.com.cesde.appmascotas;

public class Pet {
    private String tipo;
    private String raza;
    private String tamano;
    private String descripcion;
    private String collar;
    private String numero;
    private double latitud;
    private double longitud;

    public Pet() {
    }

    public Pet(String tipo, String raza, String tamano, String descripcion, String collar, String numero, double latitud, double longitud) {
        this.tipo = tipo;
        this.raza = raza;
        this.tamano = tamano;
        this.descripcion = descripcion;
        this.collar = collar;
        this.numero = numero;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRaza() {
        return raza;
    }

    public String getTamano() {
        return tamano;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCollar() {
        return collar;
    }

    public String getNumero() {
        return numero;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}


