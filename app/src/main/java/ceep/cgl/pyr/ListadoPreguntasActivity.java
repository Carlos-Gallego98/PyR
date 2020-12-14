package ceep.cgl.pyr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import ceep.cgl.pyr.sqlite.PreguntaDAO;
import ceep.cgl.pyr.sqlite.PreguntaPOJO;

public class ListadoPreguntasActivity extends AppCompatActivity {

    public static final int ALTA = 0;
    public static final int VER = 1;
    public static final int EDITAR = 2;
    public static final int BORRAR = 3;

    private Button btn_buscar, btn_alta,  btn_volver;
    private Spinner sp_categorias;
    private ListView lv_preguntas;
    private String usuario;

    private PreguntaPOJO[] preguntas;
    private PreguntaPOJO[] preguntas_filtradas;
    public static final String DEPORTES= "Deportes";
    public static final String CINE= "Cine";
    public static final String GEOGRAFIA= "Geografía";
    private final String[] categorias={"Todas las categorías",DEPORTES,CINE,GEOGRAFIA};
    AdaptadorPreguntas miadaptador;
    private String categoria_seleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_preguntas);

        cargarDatos();

        // referencias a los controles
        btn_alta = (Button) findViewById(R.id.btn_AltaPreguntas);
        btn_buscar = (Button) findViewById(R.id.btn_BuscarPreguntas);
        btn_volver = (Button) findViewById(R.id.btn_Volver3);
        sp_categorias = (Spinner) findViewById(R.id.sp_Categorias);
        miadaptador = new AdaptadorPreguntas(this,preguntas_filtradas);
        lv_preguntas=(ListView)findViewById(R.id.lv_Preguntas);
        lv_preguntas.setAdapter(miadaptador);

        // recuperamos el parametro para pasarselo al Fragment
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        FragmentManager fm = getSupportFragmentManager();
        CabeceraFragment cf = new CabeceraFragment();
        cf.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cf, "new");
        ft.commit();

        // evento del boton alta
        btn_alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarActividad(ALTA, null);
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filtrarDatos();

            }
        });
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volver();
            }
        });

        // adaptador del Spinner
        ArrayAdapter<String> adaptadorcategorias= new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categorias);
        adaptadorcategorias.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_categorias.setAdapter(adaptadorcategorias);
        sp_categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Item seleccionado", adapterView.getItemAtPosition(i).toString());
                categoria_seleccionada = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void volver() {

        Intent intent = new Intent(this, AdminActivity.class);
        Bundle b = new Bundle();
        b.putString("USUARIO", usuario);
        intent.putExtras(b);
        startActivity(intent);
    }

    // CARGA TODOS LOS DATOS DE LA BASE DE DATOS
    private void cargarDatos() {

        PreguntaDAO dao = new PreguntaDAO(this);
        ArrayList<PreguntaPOJO> lista= dao.obtenerPreguntas();
        preguntas = new PreguntaPOJO[lista.size()];
        preguntas_filtradas = new PreguntaPOJO[lista.size()];
        Iterator it = lista.iterator();
        int i=0;
        while (it.hasNext()) {
            PreguntaPOJO tmp = new PreguntaPOJO();
            tmp = (PreguntaPOJO) it.next();
            preguntas[i] = tmp;
            preguntas_filtradas[i]=tmp;
            i++;
        }
    }

    // FILTRA LOS DATOS POR CATEGORIA SELECCIONADA
    private void filtrarDatos() {
        Log.i("Filtrar datos", "inicio");
        // cargamos en un arraylist las preguntas de la categoria seleccionada
        ArrayList<PreguntaPOJO> lista = new ArrayList<PreguntaPOJO>();
        for (int i=0; i<preguntas.length; i++) {
            if (categoria_seleccionada.equals(preguntas[i].getCategoria()) ||
            categoria_seleccionada.equals("Todas las categorías")){
                lista.add(preguntas[i]);
                Log.i("Filtrar datos", "CARGANDO "+preguntas[i].getTexto());
            }
        }
        // cargamos las preguntas seleccionadas en el adaptador del Spinner
        preguntas_filtradas = new PreguntaPOJO[lista.size()];
        Iterator it = lista.iterator();
        int i=0;
        while (it.hasNext()) {
            PreguntaPOJO tmp = new PreguntaPOJO();
            tmp = (PreguntaPOJO) it.next();
            preguntas_filtradas[i]=tmp;
            i++;
        }
        Log.i("Filtrar datos", "fin");

        miadaptador = new AdaptadorPreguntas(this,preguntas_filtradas);
        lv_preguntas.setAdapter(miadaptador);

    }

    // llamada a la actividad de detalle con la opcion seleccionada
    private void llamarActividad(int opcionseleccionada, PreguntaPOJO pregunta) {
        Intent intent = new Intent(this, DetallePreguntaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USUARIO", this.usuario);
        bundle.putInt("MODO", opcionseleccionada);
        if (opcionseleccionada== VER || opcionseleccionada== EDITAR || opcionseleccionada== BORRAR )  {
            bundle.putSerializable("PREGUNTA",pregunta);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class AdaptadorPreguntas extends ArrayAdapter<PreguntaPOJO> {
        Activity context;
        PreguntaPOJO[] preguntas_lv;

        AdaptadorPreguntas(Activity context, PreguntaPOJO[] preguntas_lv){
            super(context, R.layout.item_preguntas,preguntas_filtradas);
            this.context = context;
            this.preguntas_lv = preguntas_lv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            // definimos el item multilinea view
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_preguntas, null);
              // seteamos los 3 campos del item
            TextView lblid = (TextView)item.findViewById(R.id.item_IdPregunta);
            lblid.setText(String.valueOf(this.preguntas_lv[position].getId()));
            TextView lblpregunta = (TextView)item.findViewById(R.id.item_TextoPregunta);
            lblpregunta.setText(this.preguntas_lv[position].getTexto());
            TextView lblcategoria = (TextView)item.findViewById(R.id.item_CategoriaPregunta);
            lblcategoria.setText(this.preguntas_lv[position].getCategoria());
            // seteamos los 3 botones del item
            ImageButton btn_ver = (ImageButton) item.findViewById(R.id.ibtn_Ver);
            ImageButton btn_editar = (ImageButton) item.findViewById(R.id.ibtn_Editar);
            ImageButton btn_borrar = (ImageButton) item.findViewById(R.id.ibtn_Borrar);
            btn_ver.setOnClickListener(new ButtonListener(VER, this.preguntas_lv[position]));
            btn_editar.setOnClickListener(new ButtonListener(EDITAR, this.preguntas_lv[position]));
            btn_borrar.setOnClickListener(new ButtonListener(BORRAR, this.preguntas_lv[position]));
            return(item);
        }
    }

    private class ButtonListener implements View.OnClickListener{

        PreguntaPOJO pregunta;
        int opcion;

        ButtonListener(int opcion, PreguntaPOJO pregunta){
            this.pregunta = pregunta;
            this.opcion = opcion;
        }
        @Override
        public void onClick(View view) {
            llamarActividad(opcion, pregunta);
        }
    }
}