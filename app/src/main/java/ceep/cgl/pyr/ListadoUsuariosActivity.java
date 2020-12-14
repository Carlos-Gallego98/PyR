package ceep.cgl.pyr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import ceep.cgl.pyr.sqlite.UsuarioDAO;
import ceep.cgl.pyr.sqlite.UsuarioPOJO;

public class ListadoUsuariosActivity extends AppCompatActivity {

    private ListView lv_usuarios;
    private String[] lista_usuarios;
    private Button btn_volver;
    private String usuario;


    public static final String SINSELECCION="";
    public static final String VER="Ver";
    public static final String EDITAR="Editar";
    public static final String BORRAR="Borrar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_usuarios);


        // recuperamos el parametro para pasarselo al Fragment
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        FragmentManager fm = getSupportFragmentManager();
        CabeceraFragment cf = new CabeceraFragment();
        cf.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cf, "new");
        ft.commit();

        // referencias a los controles
        lv_usuarios = (ListView) findViewById(R.id.lv_Usuarios);
        btn_volver = (Button) findViewById(R.id.btn_Volver);

        // creacion del arrayadapter
        cargarUsuario();
        final ArrayAdapter<String> usuariosAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lista_usuarios);
        lv_usuarios.setAdapter(usuariosAdapter);
        Log.i("Listado de usuarios", "seteando adapter");
        lv_usuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new DialogoOpciones( usuario, lista_usuarios[i])
                        .show(getSupportFragmentManager(), "tagconfirmacion");
          }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListadoUsuariosActivity.this, AdminActivity.class);
                Bundle b = new Bundle();
                b.putString("USUARIO", usuario);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


    }

    private void cargarUsuario() {
        UsuarioDAO usuario = new UsuarioDAO(this);
        ArrayList<UsuarioPOJO> lista = usuario.obtenerUsuarios();
        lista_usuarios = new String[lista.size()];
        Iterator it = lista.iterator();
        int indice = 0;
        while (it.hasNext()) {
            UsuarioPOJO pojo = (UsuarioPOJO) it.next();
            lista_usuarios[indice] = pojo.getUsuario();
            indice++;
        }

    }

}