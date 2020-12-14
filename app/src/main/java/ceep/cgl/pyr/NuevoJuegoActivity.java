package ceep.cgl.pyr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class NuevoJuegoActivity extends AppCompatActivity {

    //Fragment fragment;
    private Spinner sp_Categoria, sp_Preguntas, sp_Tiempo;
    public final String[] lista_categorias = {"Cualquier categoria", "Deportes", "Cine", "Geografia"};
    public final String[] lista_preguntas = {"5","10","15"};
    public final String[] lista_tiempo = {"60","90","120"};
    private Button btn_Jugar;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_juego);

        // referencias a los controles
        sp_Categoria = (Spinner) findViewById(R.id.sp_Categoria);
        sp_Preguntas = (Spinner) findViewById(R.id.sp_Jugadas);
        sp_Tiempo = (Spinner) findViewById(R.id.sp_Tiempo);
        btn_Jugar = (Button) findViewById(R.id.btn_Jugar);

        // creacion de los adaptadores
        final ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lista_categorias);
        categoriasAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_Categoria.setAdapter(categoriasAdapter);
        final ArrayAdapter<String> preguntasAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lista_preguntas);
        preguntasAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_Preguntas.setAdapter(preguntasAdapter);
        final ArrayAdapter<String> tiempoAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, lista_tiempo);
        tiempoAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_Tiempo.setAdapter(tiempoAdapter);

    // recuperamos el parametro para pasarselo al Fragment
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        FragmentManager fm = getSupportFragmentManager();
        CabeceraFragment cf = new CabeceraFragment();
        cf.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cf,"new");
        ft.commit();

        // definimos el evento del boton
        btn_Jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NuevoJuegoActivity.this, JugarActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("USUARIO", usuario);
                bundle.putString("CATEGORIA",sp_Categoria.getSelectedItem().toString());
                bundle.putString("PREGUNTAS",sp_Preguntas.getSelectedItem().toString());
                bundle.putString("TIEMPO",sp_Tiempo.getSelectedItem().toString());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


    }



}