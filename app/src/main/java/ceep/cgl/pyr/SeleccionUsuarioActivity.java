package ceep.cgl.pyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import ceep.cgl.pyr.sqlite.UsuarioDAO;
import ceep.cgl.pyr.sqlite.UsuarioPOJO;

public class SeleccionUsuarioActivity extends AppCompatActivity {

    private TextView tv_MensajeSeleccion, tv_usuario, tv_contrasena, tv_Mensaje;
    private EditText et_Usuario, et_Contrasena;
    private Button btn_Usuario;
    private Spinner sp_Usuarios;
    private String[] lista_usuarios;
    private UsuarioDAO usuarioDAO;

    // modo de activacion true es nuevo usuario false si es usuario existente
    private boolean bo_Nuevo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_usuario);

        usuarioDAO = new UsuarioDAO(this);

        // referencia a los demas controles
        tv_MensajeSeleccion = (TextView) findViewById(R.id.tv_MensajeSeleccion);
        tv_usuario = (TextView) findViewById(R.id.tv_usuario);
        tv_contrasena = (TextView) findViewById(R.id.tv_contrasena);
        tv_Mensaje = (TextView) findViewById(R.id.tv_Mensaje);
        sp_Usuarios = (Spinner) findViewById(R.id.sp_Usuarios);
        et_Usuario = (EditText) findViewById(R.id.et_Usuario);
        et_Contrasena = (EditText) findViewById(R.id.et_Contrasena);
        btn_Usuario = (Button) findViewById(R.id.btn_Usuario);

        // evento click de botones
        btn_Usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bo_Nuevo) {
                    crearUsuario();

                } else {
                    usuarioExistente();
                }

            }
        });

        // creacion del arrayadapter
        cargarUsuario();
        final ArrayAdapter<String> usuariosAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lista_usuarios);
        usuariosAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_Usuarios.setAdapter(usuariosAdapter);


        // añadimos un evento
        sp_Usuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Log.i("Spinner ", "He seleccionado el valor 0, no hago nada");
                    et_Usuario.setText(null);
                    et_Contrasena.setText(null);
                    bo_Nuevo = true;
                    cambiarmodo();
                } else {
                    et_Usuario.setText(usuariosAdapter.getItem(i).toString());
                    et_Contrasena.setText("");
                    Log.i("Spinner ", "He seleccionado el valor " + i);
                    bo_Nuevo = false;
                    cambiarmodo();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cambiarmodo();

    }

    private void usuarioExistente() {
        tv_Mensaje.setText("");
        // Toast.makeText(view.getContext(), "Hay que recuperar la contraseña", Toast.LENGTH_SHORT).show();


        // comprobacion contrasena
        if (et_Contrasena.getText().toString().equals(null) || et_Contrasena.getText().toString().equals("")) {
            tv_Mensaje.setText("Debes introducir la contraseña");
            return;
        } else if (et_Contrasena.getText().toString().length() < 6) {
            tv_Mensaje.setText("La contraseña debe tener al menos 6 caracteres");
            return;
        } else if (et_Contrasena.getText().toString().length() > 10) {
            tv_Mensaje.setText("La contraseña no puede tener mas de 10 caracteres");
            return;
        }

        UsuarioPOJO usuarioPOJO = usuarioDAO.leerusuario(et_Usuario.getText().toString());
        if (!usuarioPOJO.getPassword().equals(et_Contrasena.getText().toString())){
            tv_Mensaje.setText("La contraseña no es correcta");
            return;
        }
        //Toast.makeText(view.getContext(), "Falta comprobar que la contraseña coincide", Toast.LENGTH_SHORT).show();

        // llamamos a la actividad de un juego
        Intent intent;
        String usuario=et_Usuario.getText().toString();
        if (usuario.equals("admin")){
         //   Log.i("SELECCION USUARIO","usuario administrador "+usuario);
            intent = new Intent(SeleccionUsuarioActivity.this, AdminActivity.class);
        } else {
         //   Log.i("SELECCION USUARIO","usuario jugador "+usuario);
            intent = new Intent(SeleccionUsuarioActivity.this, NuevoJuegoActivity.class);
        }
        Bundle b = new Bundle();
        b.putString("USUARIO", et_Usuario.getText().toString());
        intent.putExtras(b);
        startActivity(intent);
    }

    private void crearUsuario() {
        tv_Mensaje.setText("");
        //comprobacion usuarios
        if (et_Usuario.getText().toString().equals(null) || et_Usuario.getText().toString().equals("")) {
            tv_Mensaje.setText("Debes introducir el nombre de usuario");
            return;
        } else if (et_Usuario.getText().toString().length() < 6) {
            tv_Mensaje.setText("El usuario debe tener al menos 6 caracteres");
            return;
        } else if (et_Usuario.getText().toString().length() > 10) {
            tv_Mensaje.setText("El usuario no puede tener mas de 10 caracteres");
            return;
        }

        UsuarioPOJO usuarioPOJO = usuarioDAO.leerusuario(et_Usuario.getText().toString());
        if (usuarioPOJO!=null){
            tv_Mensaje.setText("El usuario ya existe");
            return;
        }
        //Toast.makeText(view.getContext(), "Falta validar que no este dado de alta", Toast.LENGTH_SHORT).show();


        // comprobacion contrasena
        if (et_Contrasena.getText().toString().equals(null) || et_Contrasena.getText().toString().equals("")) {
            tv_Mensaje.setText("Debes introducir la contraseña");
            return;
        } else if (et_Contrasena.getText().toString().length() < 6) {
            tv_Mensaje.setText("La contraseña debe tener al menos 6 caracteres");
            return;
        } else if (et_Contrasena.getText().toString().length() > 10) {
            tv_Mensaje.setText("La contraseña no puede tener mas de 10 caracteres");
            return;
        }

        //Toast.makeText(view.getContext(), "Falta dar de alta el usuario", Toast.LENGTH_SHORT).show();

        long id= usuarioDAO.altausuario(et_Usuario.getText().toString(), et_Contrasena.getText().toString());
        if (id==-1){
            tv_Mensaje.setText("Se ha producido un error en el alta");
            return;
        }

        // llamamos a la actividad de un juego

        Intent intent = new Intent(SeleccionUsuarioActivity.this, NuevoJuegoActivity.class);
        Bundle b = new Bundle();
        b.putString("USUARIO", et_Usuario.getText().toString());
        intent.putExtras(b);
        startActivity(intent);
    }

    private void cambiarmodo() {
        if (bo_Nuevo) {
           btn_Usuario.setText(getResources().getString(R.string.btn_CrearUsuario));
            et_Usuario.setEnabled(true);
        } else {
            btn_Usuario.setText(getResources().getString(R.string.btn_UsuarioExistente));
            et_Usuario.setEnabled(false);
        }
    }

    private void cargarUsuario() {
        //  public final String[] lista_usuarios = {"Seleccione usuario", "admin", "carlos", "julio"};

        UsuarioDAO usuario = new UsuarioDAO(this);
        ArrayList<UsuarioPOJO> lista = usuario.obtenerUsuarios();
        lista_usuarios = new String[lista.size()+1];
                lista_usuarios[0]="Seleccione usuario";
        Iterator it = lista.iterator();
        int indice=1;
        while (it.hasNext()){
            UsuarioPOJO pojo = (UsuarioPOJO) it.next();
            lista_usuarios[indice] = pojo.getUsuario();
            indice++;
        }

    }
}