package ceep.cgl.pyr;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import ceep.cgl.pyr.sqlite.PreguntaDAO;
import ceep.cgl.pyr.sqlite.PreguntaPOJO;
import ceep.cgl.pyr.sqlite.ResultadoDAO;
import ceep.cgl.pyr.sqlite.UsuarioDAO;
import ceep.cgl.pyr.sqlite.UsuarioPOJO;

public class JugarActivity extends AppCompatActivity {

    // PARAMETROS
    private String usuario, categoria;
    private int preguntastotales,tiempopartida;

    // controles
    private Chronometer cronometro;
    private TextView numaciertos, numerrores, tv_pregunta, tv_orden, tv_msg;
    private Button btn_contestar, btn_pasar;
    private RadioButton rb_respuesta1, rb_respuesta2, rb_respuesta3, rb_respuesta4;
    private RadioGroup rg;

    // variables
    private long iniciopartida; // momento en el que se inicia la partida
    private long finpartida; // momento en el que se finaliza la partida
    ArrayList<PreguntaPOJO> preguntasjuego = new ArrayList<PreguntaPOJO>(); // el arraylist contiene las preguntas que se van a jugar
    ArrayList<Integer> respuestasusuario = new ArrayList<Integer>(); // guarda las respuestas del usuario
    int numeropregunta = 0; // El numero de pregunta que se esta mostrando
    int contadorrespuestas = 0;  // El numero de respuestas del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        // recuperamos las referencias a los controles
        cronometro = findViewById(R.id.cronometro);
        numaciertos = findViewById(R.id.num_Aciertos);
        numerrores = findViewById(R.id.num_Errores);
        tv_pregunta = findViewById(R.id.tv_Pregunta);
        rg = findViewById(R.id.rg);
        rb_respuesta1 = findViewById(R.id.rb_Respuesta1);
        rb_respuesta2 = findViewById(R.id.rb_Respuesta2);
        rb_respuesta3 = findViewById(R.id.rb_Respuesta3);
        rb_respuesta4 = findViewById(R.id.rb_Respuesta4);
        btn_contestar = findViewById(R.id.btn_Contestar);
        btn_pasar = findViewById(R.id.btn_Pasar);
        tv_orden = findViewById(R.id.tv_Orden);
        tv_msg = findViewById(R.id.tv_Msg);

        // recuperamos el parametro para pasarselo al Fragment
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        FragmentManager fm = getSupportFragmentManager();
        CabeceraFragment cf = new CabeceraFragment();
        cf.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cf, "new");
        ft.commit();

        // recuperamos los parametros de la partida
        categoria = bundle.getString("CATEGORIA");
        preguntastotales = Integer.parseInt(bundle.getString("PREGUNTAS"));
        tiempopartida = Integer.parseInt(bundle.getString("TIEMPO"));
        Log.i("JUGAR", "Parametros del juego " + categoria + " " + preguntastotales + " " + tiempopartida);

        // ajustamos el cronometro        ;
        iniciopartida = SystemClock.elapsedRealtime();
        cronometro.setBase(iniciopartida + tiempopartida * 1000);
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long pasado = SystemClock.elapsedRealtime() - iniciopartida;
                if (pasado > tiempopartida * 1000) {
                    cronometro.stop();
                    finpartida=iniciopartida+tiempopartida * 1000;
                    Log.i("JUGAR", "Acabo el tiempo");
                    mostrarDialogo(true);
                }
            }
        });
        cronometro.start();

        // inicializamos los datos de la actividad
        numaciertos.setText("0");
        numerrores.setText("0");

        cargarDatos(); // recupera los datos de la base de datos
        cargarPregunta(); // carga la pregunta

        // clickeamos en los botones
        btn_contestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contestarpregunta();
            }
        });
        btn_pasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasarpregunta();
            }
        });
    }

    // metodo de cargado de preguntas y respuestas (dependiendo del numero de preguntas y de la categoria)
    private void cargarDatos() {
        Log.i("JUGAR", "Cargando preguntas de categoria " + categoria + " y numero de preguntas " + preguntastotales);
        PreguntaDAO dao = new PreguntaDAO(this);
        ArrayList<PreguntaPOJO> lista = dao.obtenerPreguntas();
        ArrayList<PreguntaPOJO> listacategoria = new ArrayList<PreguntaPOJO>();
        Iterator it = lista.iterator();
        int iinteresa = 0;
        while (it.hasNext()) {
            PreguntaPOJO tmp = (PreguntaPOJO) it.next();
            //   Log.i("JUGAR", "Leido base de datos id= " + tmp.getId() + " categoria " + tmp.getCategoria());
            if (categoria.equals(tmp.getCategoria()) || categoria.equals("Cualquier categoria")) {
                iinteresa++;
                listacategoria.add(tmp);
            }
        }

        Log.i("JUGAR", "Fin de lectura leidos Me interesa " + iinteresa);

        Random aleatorio = new Random();
        for (int i = 0; i < preguntastotales; i++) {
            int bola = aleatorio.nextInt(iinteresa);
            Log.i("JUGAR", "Sacando bolas " + bola + " total " + iinteresa);
            PreguntaPOJO bolasacada = listacategoria.get(bola);
            Log.i("JUGAR", "Pregunta id " + bolasacada.getId() + " " + bolasacada.getTexto());
            preguntasjuego.add(bolasacada);
            respuestasusuario.add(null); // se cargan sin respuesta
            listacategoria.remove(bola);
            iinteresa--;
        }

    }

    // carga una pregunta en el Activity la correspondiente a numero de pregunta
    private void cargarPregunta() {
        Log.i("Jugar","Cargando la pregunta " +numeropregunta);
        tv_pregunta.setText(preguntasjuego.get(numeropregunta).getTexto());
        rb_respuesta1.setText(preguntasjuego.get(numeropregunta).getRespuesta().get(0).getTexto());
        rb_respuesta2.setText(preguntasjuego.get(numeropregunta).getRespuesta().get(1).getTexto());
        rb_respuesta3.setText(preguntasjuego.get(numeropregunta).getRespuesta().get(2).getTexto());
        rb_respuesta4.setText(preguntasjuego.get(numeropregunta).getRespuesta().get(3).getTexto());
        tv_orden.setText(String.valueOf(numeropregunta+1) + " / " + preguntastotales);
        tv_msg.setText("");

        // se activan o no las opciones si se han respondido anteriormente
        if (respuestasusuario.get(numeropregunta)== null) {
            Log.i("JUGAR","Pregunta " + numeropregunta + " NO contestada");
            rg.clearCheck();
            rb_respuesta1.setEnabled(true);
            rb_respuesta2.setEnabled(true);
            rb_respuesta3.setEnabled(true);
            rb_respuesta4.setEnabled(true);
            btn_contestar.setEnabled(true);
        }
        else {
            Log.i("JUGAR","Pregunta " + numeropregunta + " YA contestada");
            btn_contestar.setEnabled(false);
            rb_respuesta1.setEnabled(false);
            rb_respuesta2.setEnabled(false);
            rb_respuesta3.setEnabled(false);
            rb_respuesta4.setEnabled(false);
            rg.check(obtenerRadioButton(respuestasusuario.get(numeropregunta)));
        }
    }


    // pasa a la siguiente pregunta y la carga
    private void pasarpregunta() {
        if (numeropregunta+1==preguntastotales)
        {
            numeropregunta=0;
        }
        else {
            numeropregunta++;
        }
        cargarPregunta();
    }

    // comprueba si la respuesta es correcta o incorrecta
    private void contestarpregunta() {
        // comprobamos que el usuario ha seleccionado una opcion
        if (rg.getCheckedRadioButtonId() == -1) {
           // Toast.makeText(this, "Debes seleccionar una respuesta", Toast.LENGTH_SHORT);
            tv_msg.setText("Debes seleccionar una respuesta");
            return;
        }

        // calculamos la opcion seleccionada
        int opcionseleccionada=0;
        switch(rg.getCheckedRadioButtonId())
        {
            case R.id.rb_Respuesta1:
                opcionseleccionada=1;
                break;
            case R.id.rb_Respuesta2:
                opcionseleccionada=2;
                break;
            case R.id.rb_Respuesta3:
                opcionseleccionada=3;
                break;
            case R.id.rb_Respuesta4:
                opcionseleccionada=4;
                break;
        }

        // guardamos la respuesta y comprobamos si la solucion es correcta
        respuestasusuario.set(numeropregunta,opcionseleccionada);
         Log.i("JUGAR","Opcion seleccionada "+opcionseleccionada+ " Respuesta correcta "+preguntasjuego.get(numeropregunta).getIdSolucion());

        if (opcionseleccionada== preguntasjuego.get(numeropregunta).getIdSolucion()) {
            numaciertos.setText(String.valueOf(Integer.parseInt(numaciertos.getText().toString())+1));
            Log.i("JUGAR","Correcta ");

        }
        else {
            numerrores.setText(String.valueOf(Integer.parseInt(numerrores.getText().toString())+1));
            Log.i("JUGAR","Incorrecta ");
        }

        contadorrespuestas++;

        if (contadorrespuestas== preguntastotales) {

            Log.i("JUGAR", "Se contestaron todas");
            finpartida=SystemClock.elapsedRealtime();
            cronometro.stop();
            mostrarDialogo(false);
        }
        else {
            pasarpregunta();
        }

    }

    // recupera el id del radioButton en funcion de la opcion
    private int obtenerRadioButton(int opcionseleccionada) {
        int radioButton=0;
        switch(opcionseleccionada)
        {
            case 1:
                radioButton=R.id.rb_Respuesta1;
                break;
            case 2:
                radioButton=R.id.rb_Respuesta2;
                break;
            case 3:
                radioButton=R.id.rb_Respuesta3;
                break;
            case 4:
                radioButton=R.id.rb_Respuesta4;
                break;
        }
        return radioButton;
    }

    // mostramos un dialogo con los resultados de la partida
    private void mostrarDialogo(boolean tiempo) {

        // bloqueamos los controles
        btn_contestar.setEnabled(false);
        rb_respuesta1.setEnabled(false);
        rb_respuesta2.setEnabled(false);
        rb_respuesta3.setEnabled(false);
        rb_respuesta4.setEnabled(false);

        // registramos el resultado
        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        UsuarioPOJO usuariopojo = usuarioDAO.leerusuario(usuario);
        ResultadoDAO resultadoDAO = new ResultadoDAO(this);
        resultadoDAO.altaresultado(usuariopojo.getIdUsuario(),Integer.parseInt(numaciertos.getText().toString()),preguntastotales,categoria,new Long(finpartida-iniciopartida).intValue());

        // mostramos el dialogo
        String mensaje;
        if (tiempo){
            mensaje = "Oops, se te ha acabado el tiempo";
        }
        else {
            mensaje = "Buen intento";
        }
        DialogoAlerta dialogo = new DialogoAlerta();
        Log.i("Dialogo","Inicio Partida" +iniciopartida + " Fin partida " +finpartida);
        dialogo.setCancelable(false);
        dialogo.ponerparametros(this,mensaje,usuario,categoria,String.valueOf(preguntastotales),numaciertos.getText().toString(),new Long(finpartida-iniciopartida).intValue());
        dialogo.show(getSupportFragmentManager(), "tagalerta");
    }


    public static class DialogoAlerta extends DialogFragment {

        String mensaje, usuario, categoria, preguntas,aciertos;
        Activity activity;
        int tiempo;

        public void ponerparametros (Activity activity, String mensaje, String usuario, String categoria, String preguntas, String aciertos, int tiempo){
            this.activity= activity;
            this.mensaje=mensaje;
            this.usuario=usuario;
            this.categoria=categoria;
            this.preguntas=preguntas;
            this.aciertos=aciertos;
            this.tiempo=tiempo;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            double dtiempo = tiempo/1000d;
            builder.setTitle(this.mensaje)
                    .setMessage("Jugador: "+usuario+ '\n' +
                            "Categoria: " +categoria+ '\n' +
                            "Preguntas: " +preguntas+ '\n' +
                            "Aciertos: " +aciertos+ '\n' +
                            "Tiempo: " +dtiempo + " sg")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // vamos a Hall Of Fame
                    Intent intent = new Intent(activity, HallOfFameActivity.class);
                    Bundle b = new Bundle();
                    b.putString("USUARIO", usuario);
                    b.putString("CATEGORIA",categoria);
                    b.putInt("PREGUNTAS",Integer.parseInt(preguntas));
                    intent.putExtras(b);
                    startActivity(intent);
                }
            })        ;
            return builder.create();
        }

    }

}


