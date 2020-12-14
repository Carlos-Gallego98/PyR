package ceep.cgl.pyr.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class PreguntaDAO {

    SQLiteDatabase basededatos;


    public PreguntaDAO(Context context) {
        Estructura estructura = new Estructura(context, Estructura.BDD_NAME, null, Estructura.BDD_VERSION);
        basededatos = estructura.getReadableDatabase();
    }

    // DA DE ALTA UNA PREGUNTA CON SUS RESPUESTAS (-1 si se produce error en el alta)
    public long altapregunta(String pregunta, String categoria, int solucion, String[] respuestas) {
        if (respuestas == null || respuestas.length != 4 || solucion < 1 || solucion > 4) {
            Log.e("PREGUNTASDAO", "Error en alta parametros recibidos erroneos");
            return -1;
        }
        // insert a traves del metodo insert
        ContentValues miregistro = new ContentValues();
        miregistro.put(Estructura.CAMPO_PREGUNTAS_ID, nextId());
        miregistro.put(Estructura.CAMPO_PREGUNTAS_TEXT, pregunta);
        miregistro.put(Estructura.CAMPO_PREGUNTAS_CATEGORIA, categoria);
        miregistro.put(Estructura.CAMPO_PREGUNTAS_SOLUCION, solucion);
        long id = basededatos.insert(Estructura.TABLA_PREGUNTAS, null, miregistro);

        // DAR DE ALTA LAS RESPUESTAS
        for (int i = 0; i < respuestas.length; i++) {
            miregistro = new ContentValues();
            miregistro.put(Estructura.CAMPO_RESPUESTAS_ID, id);
            miregistro.put(Estructura.CAMPO_RESPUESTAS_ORDEN, i + 1);
            miregistro.put(Estructura.CAMPO_RESPUESTAS_TEXT, respuestas[i]);
            basededatos.insert(Estructura.TABLA_RESPUESTAS, null, miregistro);
        }
        return id;
    }

    // RECUPERA TODAS LAS PREGUNTAS
    public ArrayList<PreguntaPOJO> obtenerPreguntas() {
        String[] campos = new String[]{Estructura.CAMPO_PREGUNTAS_ID, Estructura.CAMPO_PREGUNTAS_TEXT, Estructura.CAMPO_PREGUNTAS_CATEGORIA, Estructura.CAMPO_PREGUNTAS_SOLUCION};
        Cursor c = basededatos.query(Estructura.TABLA_PREGUNTAS, campos, null, null, null, null, null);
        ArrayList<PreguntaPOJO> lista = new ArrayList<>();
        if (c != null & c.moveToFirst()) {
            do {
                PreguntaPOJO registro = new PreguntaPOJO();
                registro.setId(c.getInt(0));
                registro.setTexto(c.getString(1));
                registro.setCategoria(c.getString(2));
                registro.setIdSolucion(c.getInt(3));
                //Log.i("Pregunta leida ", registro.getId() + " / " + registro.getTexto() + " / " + registro.getCategoria() + " / " + registro.getIdSolucion());

                // Recuperamos la respuesta de cada pregunta
                ArrayList<RespuestaPOJO> respuesta = new ArrayList<RespuestaPOJO>();
                String[] camposRespuesta = new String[]{Estructura.CAMPO_RESPUESTAS_ID, Estructura.CAMPO_RESPUESTAS_TEXT, Estructura.CAMPO_RESPUESTAS_ORDEN};
                String str_where = Estructura.CAMPO_RESPUESTAS_ID + "=" + registro.getId();
                Cursor cRespuesta = basededatos.query(Estructura.TABLA_RESPUESTAS, camposRespuesta, str_where, null, null, null, null);
                if (cRespuesta != null & cRespuesta.moveToFirst()) {
                    do {
                        RespuestaPOJO registrorespuesta = new RespuestaPOJO();
                        registrorespuesta.setIdPregunta(cRespuesta.getInt(0));
                        registrorespuesta.setTexto(cRespuesta.getString(1));
                        registrorespuesta.setOrden(cRespuesta.getInt(2));
                        Log.i("Respuesta leida ", registrorespuesta.getIdPregunta() + " / " + registrorespuesta.getTexto() + " / " + registrorespuesta.getOrden());
                        respuesta.add(registrorespuesta);
                    }
                    while (cRespuesta.moveToNext());
                }

                registro.setRespuesta(respuesta);
                lista.add(registro);
            }
            while (c.moveToNext());
        }
        return lista;
    }



    // BORRA UNA PREGUNTA Y SUS RESPUESTAS
    public void borrarpregunta(int id) {
        String str_where_pregunta = Estructura.CAMPO_PREGUNTAS_ID + "= " + id;
        String str_where_respuesta = Estructura.CAMPO_RESPUESTAS_ID + "= " + id;
        // Log.i("borrar pregunta ", str_where_pregunta);
        // Log.i("borrar respuesta ", str_where_respuesta);
        basededatos.delete(Estructura.TABLA_RESPUESTAS, str_where_respuesta, null);
        basededatos.delete(Estructura.TABLA_PREGUNTAS, str_where_pregunta, null);
    }

    // MODIFICAR LAS PREGUNTAS Y SUS RESPUESTAS
    public void modificarpregunta(int id, String pregunta, String categoria, int solucion, String[] respuestas) {
        // modificacion a traves del metodo update
        ContentValues miregistro = new ContentValues();
        miregistro.put(Estructura.CAMPO_PREGUNTAS_TEXT, pregunta);
        miregistro.put(Estructura.CAMPO_PREGUNTAS_CATEGORIA, categoria);
        miregistro.put(Estructura.CAMPO_PREGUNTAS_SOLUCION, solucion);
        String str_where = Estructura.CAMPO_PREGUNTAS_ID + "= " + id;
        basededatos.update(Estructura.TABLA_PREGUNTAS, miregistro, str_where, null);
        //Log.i("modificar pregunta ", str_where);

        // MODIFICAR LAS RESPUESTAS
        for (int i = 0; i < respuestas.length; i++) {
            miregistro = new ContentValues();
            miregistro.put(Estructura.CAMPO_RESPUESTAS_TEXT, respuestas[i]);
            str_where = Estructura.CAMPO_RESPUESTAS_ID + "= " + id + " AND " +Estructura.CAMPO_RESPUESTAS_ORDEN + "= " + (i+1)  ;
            basededatos.update(Estructura.TABLA_RESPUESTAS, miregistro, str_where, null);
            //   Log.i("modificar respuesta ", str_where);

        }
    }

    // Calcula el id siguiente de preguntas
    private long nextId() {
        long id = 0;
        String sql = "select MAX(" + Estructura.CAMPO_PREGUNTAS_ID + ") from " + Estructura.TABLA_PREGUNTAS;
        Log.i("PreguntaDAO", sql);
        Cursor c = basededatos.rawQuery(sql, null);
        if (c.moveToFirst()) {
            id = c.getInt(0);
        }
        return id + 1;
    }

}
