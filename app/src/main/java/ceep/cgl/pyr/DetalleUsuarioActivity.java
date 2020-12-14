package ceep.cgl.pyr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ceep.cgl.pyr.sqlite.UsuarioDAO;
import ceep.cgl.pyr.sqlite.UsuarioPOJO;

public class DetalleUsuarioActivity extends AppCompatActivity {

    // parametros recibidos
    String usuario;
    String usuarioseleccionado;
    int opcion;
    // Controles de la actividad
    TextView tv_titulo, tv_Idusuario, tv_UsuarioSeleccionado, tv_Mensaje2;
    EditText et_Password;
    Button btn_confirmar, btn_vuelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_usuario);

        // recuperamos los parametros para pasarselo al Fragment
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        usuarioseleccionado = bundle.getString("USUARIO_SELECCIONADO");
        opcion = bundle.getInt("MODO");

        FragmentManager fm = getSupportFragmentManager();
        CabeceraFragment cf = new CabeceraFragment();
        bundle = new Bundle();
        bundle.putString("USUARIO", usuario);
        cf.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cf, "new");
        ft.commit();

        // referencias a los controles
        tv_titulo = (TextView) findViewById(R.id.tv_Titulo);
        tv_Idusuario = (TextView) findViewById(R.id.tv_IdUsuario);
        tv_UsuarioSeleccionado = (TextView) findViewById(R.id.tv_UsuarioSeleccionado);
        et_Password = (EditText) findViewById(R.id.et_Password);
        tv_Mensaje2 = (TextView) findViewById(R.id.tv_Mensaje2);
        btn_confirmar = (Button)findViewById(R.id.btn_Confirmar);
        btn_vuelta = (Button)findViewById(R.id.btn_Vuelta);

        cambiarModo();
        tv_UsuarioSeleccionado.setText(usuarioseleccionado);
        UsuarioDAO dao = new UsuarioDAO(this);
        UsuarioPOJO pojo = dao.leerusuario(usuarioseleccionado);
        et_Password.setText(pojo.getPassword());
        tv_Idusuario.setText(String.valueOf(pojo.getIdUsuario()));


        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar();
                volver();
            }
        });

        btn_vuelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volver();
            }
        });
    }


    private void cambiarModo(){

        if (opcion==DialogoOpciones.VER){
            tv_titulo.setText("Consulta de usuario");
            btn_confirmar.setVisibility(Button.INVISIBLE);
            et_Password.setBackgroundColor(getResources().getColor(R.color.fondo));
            et_Password.setEnabled(false);
        } else if (opcion==DialogoOpciones.EDITAR){
            tv_titulo.setText("Modificacion de usuario");
        } else if (opcion==DialogoOpciones.BORRAR){
            tv_titulo.setText("Borrado de usuario");
            et_Password.setBackgroundColor(getResources().getColor(R.color.fondo));
            et_Password.setEnabled(false);
        } else {
            tv_titulo.setText("");
        }
    }

    private void confirmar(){
        UsuarioDAO dao = new UsuarioDAO(this);
        UsuarioPOJO pojo = dao.leerusuario(usuarioseleccionado);
        if (opcion==DialogoOpciones.EDITAR){
            // comprobacion contrasena
            if (et_Password.getText().toString().equals(null) || et_Password.getText().toString().equals("")) {
                tv_Mensaje2.setText("Debes introducir la contraseña");
                return;
            } else if (et_Password.getText().toString().length() < 6) {
                tv_Mensaje2.setText("La contraseña debe tener al menos 6 caracteres");
                return;
            } else if (et_Password.getText().toString().length() > 10) {
                tv_Mensaje2.setText("La contraseña no puede tener mas de 10 caracteres");
                return;
            }
            dao.modificarpassword(pojo.getIdUsuario(), et_Password.getText().toString());

        } else if (opcion==DialogoOpciones.BORRAR){
          dao.borrarusuario(pojo.getIdUsuario());

        }
    }

    private void volver(){
        Intent intent = new Intent(this, ListadoUsuariosActivity.class);
        Bundle b = new Bundle();
        b.putString("USUARIO", usuario);
        intent.putExtras(b);
        startActivity(intent);
    }
}


