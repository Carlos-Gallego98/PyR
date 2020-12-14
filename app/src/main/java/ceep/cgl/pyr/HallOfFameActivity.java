package ceep.cgl.pyr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import ceep.cgl.pyr.sqlite.ResultadoDAO;
import ceep.cgl.pyr.sqlite.ResultadoPOJO;
import ceep.cgl.pyr.sqlite.UsuarioDAO;

public class HallOfFameActivity extends AppCompatActivity {

    // parametros
    String usuario, categoria;
    int numpreguntas;

    // controles
    TextView tv_categoria, tv_numpreguntas;
    Button btn_Seguir, btn_Salir;
    ListView lv_resultados;

    // datos de resultados
    Item[] datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

        // recuperamos los parametros
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        categoria = bundle.getString("CATEGORIA");
        numpreguntas = bundle.getInt("PREGUNTAS");

        // REFERENCIAS A LOS CONTROLES
        tv_categoria = findViewById(R.id.tv_Categoria2);
        tv_categoria.setText(categoria);
        tv_numpreguntas = findViewById(R.id.tv_NumPreguntas);
        tv_numpreguntas.setText(String.valueOf(numpreguntas));
        btn_Seguir = findViewById(R.id.btn_Seguir);
        btn_Salir = findViewById(R.id.btn_Salir);

        btn_Seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HallOfFameActivity.this, NuevoJuegoActivity.class);
                Bundle b = new Bundle();
                b.putString("USUARIO", usuario);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btn_Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        // cargar los resultados
        lv_resultados = findViewById(R.id.lv_Resultados);
        cargardatos();
        final AdaptadorHallOfFame miadaptador = new AdaptadorHallOfFame(this);
        lv_resultados.setAdapter(miadaptador);
    }

    private void cargardatos() {
        // recuperamos los resultados de la base de datos
        ResultadoDAO dao = new ResultadoDAO(this);
        ArrayList<ResultadoPOJO> lista = dao.hallOfFame(categoria, numpreguntas);
        // los cargamos en el ArrayAdapter (limitandolo a los 10 mejores)
        Log.i("Cargar datos", "Tamaño Lista de resultados " + lista.size());
        if (lista.size() > 10) {
            datos = new Item[10];
            Log.i("Cargar datos", "Cargo solo 10 ");
        } else {
            datos = new Item[lista.size()];
            Log.i("Cargar datos", "Cargo todos ");
        }
        Iterator<ResultadoPOJO> it = lista.iterator();
        int num = 1;
        UsuarioDAO usuariodao = new UsuarioDAO(this);
        while (it.hasNext()) {
            ResultadoPOJO tmp = it.next();
            Item item = new Item();
            item.setPosicion(num);
            item.setJugador(usuariodao.leerusuario(tmp.getIdUsuario()).getUsuario());
            item.setPuntos(tmp.getNumaciertos());
            item.setTiempo(tmp.getTiempo());
            datos[num - 1] = item;
            num++;
            Log.i("Cargar datos", "Jugador " + item.getPosicion() + " " + item.getJugador() + " " + item.getPuntos() + " " + item.getTiempo());
            if (num == 11) {
                break;
            }
        }
    }

    // representa cada item del listview
    private class Item {

        int posicion;
        String jugador;
        int puntos;
        int tiempo;

        public int getPosicion() {
            return posicion;
        }

        public void setPosicion(int posicion) {
            this.posicion = posicion;
        }

        public String getJugador() {
            return jugador;
        }

        public void setJugador(String jugador) {
            this.jugador = jugador;
        }

        public int getPuntos() {
            return puntos;
        }

        public void setPuntos(int puntos) {
            this.puntos = puntos;
        }

        public int getTiempo() {
            return tiempo;
        }

        public void setTiempo(int tiempo) {
            this.tiempo = tiempo;
        }

    }

    // adaptador personalizado para el listview
    class AdaptadorHallOfFame extends ArrayAdapter<Item> {
        Activity context;

        AdaptadorHallOfFame(Activity context){
            super(context, R.layout.item_resultados,datos);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            // definimos el item multilinea view
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.item_resultados, null);

            // seteamos los 4 campos del item
            TextView lblposicion = item.findViewById(R.id.tv_Posicion);
            lblposicion.setText(String.valueOf(datos[position].getPosicion()));
            TextView lbljugador = item.findViewById(R.id.tv_Jugador);
            lbljugador.setText(datos[position].getJugador());
            TextView lblpuntos = item.findViewById(R.id.tv_Puntos);
            lblpuntos.setText(String.valueOf(datos[position].getPuntos()));
            TextView lbltiempo = item.findViewById(R.id.tv_Tiempo);
            double dtiempo = datos[position].getTiempo()/1000d;
            lbltiempo.setText(String.valueOf(dtiempo));
            return(item);
        }

    }
}
