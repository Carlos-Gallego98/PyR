package ceep.cgl.pyr.sqlite;

import java.io.Serializable;

public class ResultadoPOJO implements Serializable
{
    private Integer id;
    private Integer idUsuario;
    private Integer numaciertos;
    private Integer numpreguntas;
    private String categorias;
    private Integer tiempo;

    public ResultadoPOJO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getNumaciertos() {
        return numaciertos;
    }

    public void setNumaciertos(Integer numaciertos) {
        this.numaciertos = numaciertos;
    }

    public Integer getNumpreguntas() {
        return numpreguntas;
    }

    public void setNumpreguntas(Integer numpreguntas) {
        this.numpreguntas = numpreguntas;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }
}
