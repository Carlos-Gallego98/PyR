package ceep.cgl.pyr.sqlite;

import java.io.Serializable;

public class UsuarioPOJO  implements Serializable {

    private Integer idUsuario;
    private String usuario;
    private String password;

    public UsuarioPOJO() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
