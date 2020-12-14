package ceep.cgl.pyr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import ceep.cgl.pyr.sqlite.PreguntaDAO;
import ceep.cgl.pyr.sqlite.PreguntaPOJO;

public class DetallePreguntaActivity extends AppCompatActivity {


    private String usuario;
    private int opcion;
    private PreguntaPOJO pregunta;
    private final String[] categorias = {ListadoPreguntasActivity.DEPORTES,
            ListadoPreguntasActivity.CINE, ListadoPreguntasActivity.GEOGRAFIA};
    private final Integer[] soluciones = {1, 2, 3, 4};

    private TextView tv_titulo, tv_mensaje, tv_Categoria, tv_Solucion;
    private EditText et_pregunta, et_respuesta1, et_respuesta2, et_respuesta3, et_respuesta4;
    private Spinner sp_categoria, sp_solucion;
    private Button btn_confirmar, btn_volver;
    private Space space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pregunta);

        // recuperamos los parametros para pasarselo al Fragment
        Bundle bundle = this.getIntent().getExtras();
        usuario = bundle.getString("USUARIO");
        opcion = bundle.getInt("MODO");
        pregunta = (PreguntaPOJO) bundle.getSerializable("PREGUNTA");

        FragmentManager fm = getSupportFragmentManager();
        CabeceraFragment cf = new CabeceraFragment();
        bundle = new Bundle();
        bundle.putString("USUARIO", usuario);
        cf.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(cf, "new");
        ft.commit();

        // referencias a los controles
        tv_titulo = (TextView) findViewById(R.id.tv_TituloDetallePregunta);
        tv_mensaje = (TextView) findViewById(R.id.tv_Mensaje3);
        et_pregunta = (EditText) findViewById(R.id.et_Pregunta);
        et_respuesta1 = (EditText) findViewById(R.id.et_Respuesta1);
        et_respuesta2 = (EditText) findViewById(R.id.et_Respuesta2);
        et_respuesta3 = (EditText) findViewById(R.id.et_Respuesta3);
        et_respuesta4 = (EditText) findViewById(R.id.et_Respuesta4);
        tv_Categoria = (TextView) findViewById(R.id.tv_Categoria);
        sp_categoria = (Spinner) findViewById(R.id.sp_Categorias2);
        ArrayAdapter<String> adaptador_categoria = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categorias);
        sp_categoria.setAdapter(adaptador_categoria);
        tv_Solucion = (TextView) findViewById(R.id.tv_Solucion);
        sp_solucion = (Spinner) findViewById(R.id.sp_Solucion);
        ArrayAdapter<Integer> adaptador_solucion = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item, soluciones);
        sp_solucion.setAdapter(adaptador_solucion);
        sp_solucion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (opcion == ListadoPreguntasActivity.ALTA || opcion == ListadoPreguntasActivity.EDITAR) {
                    Log.i("Seleccionar","Llamando desde item selected del Spinner");
                    seleccionar(i+1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_confirmar = (Button) findViewById(R.id.btn_Confirmar2);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar();
            }
        });
        btn_volver = (Button) findViewById(R.id.btn_Volver2);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volver();
            }
        });
        space = (Space) findViewById(R.id.Espacio);
        cambiarModo();

    }


    private void cambiarModo() {
        if (opcion == ListadoPreguntasActivity.ALTA) {
            tv_titulo.setText("Alta de preguntas");
            sp_categoria.setVisibility(View.VISIBLE);
            tv_Categoria.setVisibility(View.INVISIBLE);
            sp_solucion.setVisibility(View.VISIBLE);
            tv_Solucion.setVisibility(View.INVISIBLE);
            Log.i("Seleccionar","Llamando desde cambiar modo del Spinner");
             seleccionar((Integer) sp_solucion.getSelectedItem());
        } else if (opcion == ListadoPreguntasActivity.VER) {
            tv_titulo.setText("Consulta de preguntas");
            et_pregunta.setText(pregunta.getTexto());
            et_respuesta1.setText(pregunta.getRespuesta().get(0).getTexto());
            et_respuesta2.setText(pregunta.getRespuesta().get(1).getTexto());
            et_respuesta3.setText(pregunta.getRespuesta().get(2).getTexto());
            et_respuesta4.setText(pregunta.getRespuesta().get(3).getTexto());
          //  et_pregunta.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            et_pregunta.setEnabled(false);
            et_pregunta.setTextColor(getResources().getColor(R.color.msg_text));
            et_pregunta.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta1.setInputType(InputType.TYPE_NULL);
            et_respuesta2.setInputType(InputType.TYPE_NULL);
            et_respuesta3.setInputType(InputType.TYPE_NULL);
            et_respuesta4.setInputType(InputType.TYPE_NULL);
            et_respuesta1.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta2.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta3.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta4.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            sp_categoria.setVisibility(View.INVISIBLE);
            tv_Categoria.setVisibility(View.VISIBLE);
            tv_Categoria.setText(pregunta.getCategoria());
            sp_solucion.setVisibility(View.INVISIBLE);
            tv_Solucion.setVisibility(View.VISIBLE);
            tv_Solucion.setText(String.valueOf(pregunta.getIdSolucion()));
            btn_confirmar.setVisibility(View.INVISIBLE);
            space.setVisibility(View.INVISIBLE);
            Log.i("Seleccionar","Llamando desde cambiar modo del TextView");
            seleccionar(Integer.parseInt(tv_Solucion.getText().toString()));
        } else if (opcion == ListadoPreguntasActivity.EDITAR) {
            tv_titulo.setText("Edicion de preguntas");
            et_pregunta.setText(pregunta.getTexto());
            et_respuesta1.setText(pregunta.getRespuesta().get(0).getTexto());
            et_respuesta2.setText(pregunta.getRespuesta().get(1).getTexto());
            et_respuesta3.setText(pregunta.getRespuesta().get(2).getTexto());
            et_respuesta4.setText(pregunta.getRespuesta().get(3).getTexto());
            sp_categoria.setVisibility(View.VISIBLE);
            tv_Categoria.setVisibility(View.INVISIBLE);
            if (pregunta.getCategoria().equals(ListadoPreguntasActivity.DEPORTES)) {
                sp_categoria.setSelection(0);
            } else if (pregunta.getCategoria().equals(ListadoPreguntasActivity.CINE)) {
                sp_categoria.setSelection(1);
            } else {
                sp_categoria.setSelection(2);
            }
            sp_solucion.setVisibility(View.VISIBLE);
            tv_Solucion.setVisibility(View.INVISIBLE);
            sp_solucion.setSelection(pregunta.getIdSolucion()-1);
            Log.i("Seleccionar","Llamando desde cambiar modo del Spinner");
            seleccionar((Integer) sp_solucion.getSelectedItem());
        } else if (opcion == ListadoPreguntasActivity.BORRAR) {
            tv_titulo.setText("Borrado de preguntas");
            et_pregunta.setText(pregunta.getTexto());
            et_respuesta1.setText(pregunta.getRespuesta().get(0).getTexto());
            et_respuesta2.setText(pregunta.getRespuesta().get(1).getTexto());
            et_respuesta3.setText(pregunta.getRespuesta().get(2).getTexto());
            et_respuesta4.setText(pregunta.getRespuesta().get(3).getTexto());
            et_pregunta.setInputType(InputType.TYPE_NULL);
            et_pregunta.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta1.setInputType(InputType.TYPE_NULL);
            et_respuesta2.setInputType(InputType.TYPE_NULL);
            et_respuesta3.setInputType(InputType.TYPE_NULL);
            et_respuesta4.setInputType(InputType.TYPE_NULL);
            et_respuesta1.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta2.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta3.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta4.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            sp_categoria.setVisibility(View.INVISIBLE);
            tv_Categoria.setVisibility(View.VISIBLE);
            tv_Categoria.setText(pregunta.getCategoria());
            sp_solucion.setVisibility(View.INVISIBLE);
            tv_Solucion.setVisibility(View.VISIBLE);
            tv_Solucion.setText(String.valueOf(pregunta.getIdSolucion()));
            Log.i("Seleccionar","Llamando desde cambiar modo del TextView");
            seleccionar(Integer.parseInt(tv_Solucion.getText().toString()));
        } else {
            tv_titulo.setText("");
        }
    }

    private void seleccionar(int numero) {
    Log.i("Seleccionar","Numero recibido "+numero);
        if (opcion == ListadoPreguntasActivity.ALTA || opcion == ListadoPreguntasActivity.EDITAR){
            et_respuesta1.setBackgroundColor(getResources().getColor(R.color.et_fondo));
            et_respuesta2.setBackgroundColor(getResources().getColor(R.color.et_fondo));
            et_respuesta3.setBackgroundColor(getResources().getColor(R.color.et_fondo));
            et_respuesta4.setBackgroundColor(getResources().getColor(R.color.et_fondo));
        }
        else {
            et_respuesta1.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta2.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta3.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
            et_respuesta4.setBackgroundColor(getResources().getColor(R.color.tv_fondo));
        }

        if (numero== 1){
            et_respuesta1.setBackgroundColor(getResources().getColor(R.color.selected));
            Log.i("Seleccionar","Selecciono el numero uno");
        }
        else if (numero== 2){
            et_respuesta2.setBackgroundColor(getResources().getColor(R.color.selected));
            Log.i("Seleccionar","Selecciono el numero dos");
        }
        else if (numero== 3){
            et_respuesta3.setBackgroundColor(getResources().getColor(R.color.selected));
            Log.i("Seleccionar","Selecciono el numero tres");
        }
        else {
            et_respuesta4.setBackgroundColor(getResources().getColor(R.color.selected));
            Log.i("Seleccionar","Selecciono el numero cuatro");
        }
    }


    private void confirmar() {
        tv_mensaje.setText("");
        PreguntaDAO dao = new PreguntaDAO(this);

        if (opcion == ListadoPreguntasActivity.ALTA) {
            if (et_pregunta.getText().toString().equals(null) || et_pregunta.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir el nombre de pregunta");
                return;
            } else if (et_respuesta1.getText().toString().equals(null) || et_respuesta1.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la primera respuesta");
                return;
            } else if (et_respuesta2.getText().toString().equals(null) || et_respuesta2.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la segunda respuesta");
                return;
            } else if (et_respuesta3.getText().toString().equals(null) || et_respuesta3.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la tercera respuesta");
                return;
            } else if (et_respuesta4.getText().toString().equals(null) || et_respuesta4.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la cuarta respuesta");
                return;
            }
            String[] respuestas = {et_respuesta1.getText().toString(), et_respuesta2.getText().toString(), et_respuesta3.getText().toString(), et_respuesta4.getText().toString()};
            dao.altapregunta(et_pregunta.getText().toString(), sp_categoria.getSelectedItem().toString(), (Integer) sp_solucion.getSelectedItem(), respuestas);
            Toast.makeText(this, "Alta realizada correctamente", Toast.LENGTH_SHORT).show();
            volver();

        }  else if (opcion == ListadoPreguntasActivity.EDITAR) {
            if (et_pregunta.getText().toString().equals(null) || et_pregunta.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir el nombre de pregunta");
                return;
            } else if (et_respuesta1.getText().toString().equals(null) || et_respuesta1.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la primera respuesta");
                return;
            } else if (et_respuesta2.getText().toString().equals(null) || et_respuesta2.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la segunda respuesta");
                return;
            } else if (et_respuesta3.getText().toString().equals(null) || et_respuesta3.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la tercera respuesta");
                return;
            } else if (et_respuesta4.getText().toString().equals(null) || et_respuesta4.getText().toString().equals("")) {
                tv_mensaje.setText("Debes introducir la cuarta respuesta");
                return;
            }
            String[] respuestas = {et_respuesta1.getText().toString(), et_respuesta2.getText().toString(), et_respuesta3.getText().toString(), et_respuesta4.getText().toString()};
            dao.modificarpregunta(pregunta.getId(), et_pregunta.getText().toString(), sp_categoria.getSelectedItem().toString(), (Integer) sp_solucion.getSelectedItem(), respuestas);
            Toast.makeText(this, "Modificacion realizada correctamente", Toast.LENGTH_SHORT).show();
            volver();
        } else if (opcion == ListadoPreguntasActivity.BORRAR) {
            dao.borrarpregunta(pregunta.getId());
            Toast.makeText(this, "Borrado realizado correctamente", Toast.LENGTH_SHORT).show();
            volver();
        }
    }

    private void volver() {
        Intent intent = new Intent(this, ListadoPreguntasActivity.class);
        Bundle b = new Bundle();
        b.putString("USUARIO", usuario);
        intent.putExtras(b);
        startActivity(intent);
    }

}