package ceep.cgl.pyr.sqlite;

import java.io.Serializable;
import java.util.ArrayList;

public class PreguntaPOJO implements Serializable {

    private Integer id;
    private String texto;
    private String categoria;
    private ArrayList<RespuestaPOJO> respuesta;



    private Integer idSolucion;

    public PreguntaPOJO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getIdSolucion() {
        return idSolucion;
    }

    public void setIdSolucion(Integer idSolucion) {
        this.idSolucion = idSolucion;
    }

    public String getCategoria() {return categoria;}

    public void setCategoria(String categoria) { this.categoria = categoria;   }

    public ArrayList<RespuestaPOJO> getRespuesta() {   return respuesta;     }

    public void setRespuesta(ArrayList<RespuestaPOJO> respuesta) {         this.respuesta = respuesta;    }



}
