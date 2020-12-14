package ceep.cgl.pyr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ceep.cgl.pyr.sqlite.UsuarioDAO;
import ceep.cgl.pyr.sqlite.UsuarioPOJO;

public class AdminActivity extends AppCompatActivity {

    private Button btn_MtoPreguntas, btn_MtoUsuarios, btn_volver;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // referencias a los botones
        btn_MtoPreguntas = (Button)findViewById(R.id.btn_MtoPreguntas);
        btn_MtoUsuarios = (Button)findViewById(R.id.btn_MtoUsuarios);
        btn_volver = (Button)findViewById(R.id.btn_Volver4);


        // recuperamos el parametro para pasarselo al Fragment
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        FragmentManager fm = getSupportFragmentManager();
        CabeceraFragment cf = new CabeceraFragment();
        cf.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cf,"new");
        ft.commit();

        btn_MtoUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ListadoUsuariosActivity.class);
                Bundle b = new Bundle();
                b.putString("USUARIO", usuario);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btn_MtoPreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ListadoPreguntasActivity.class);
                Bundle b = new Bundle();
                b.putString("USUARIO", usuario);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, SeleccionUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }


}