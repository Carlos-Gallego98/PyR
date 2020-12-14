package ceep.cgl.pyr.sqlite;

import java.io.Serializable;

public class RespuestaPOJO implements Serializable {


    private Integer idPregunta;
    private Integer orden;
    private String texto;

    public RespuestaPOJO() {
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
