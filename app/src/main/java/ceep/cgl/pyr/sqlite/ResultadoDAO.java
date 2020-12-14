package ceep.cgl.pyr.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ResultadoDAO {

    SQLiteDatabase basededatos;


    public ResultadoDAO(Context context) {
        Estructura estructura = new Estructura(context, Estructura.BDD_NAME, null, Estructura.BDD_VERSION);
        basededatos = estructura.getReadableDatabase();
    }



    // DA DE ALTA UNA PREGUNTA CON SUS RESPUESTAS (-1 si se produce error en el alta)
    public long altaresultado(int idUsuario, int numaciertos, int numpreguntas, String categoria, int tiempo) {

        // insert a traves del metodo insert
        ContentValues miregistro = new ContentValues();
        miregistro.put(Estructura.CAMPO_RESULTADOS_ID, nextId());
        miregistro.put(Estructura.CAMPO_RESULTADOS_USUARIO, idUsuario);
        miregistro.put(Estructura.CAMPO_RESULTADOS_ACIERTOS, numaciertos);
        miregistro.put(Estructura.CAMPO_RESULTADOS_PREGUNTAS, numpreguntas);
        miregistro.put(Estructura.CAMPO_RESULTADOS_CATEGORIA, categoria);
        miregistro.put(Estructura.CAMPO_RESULTADOS_TIEMPO, tiempo);
        long id = basededatos.insert(Estructura.TABLA_RESULTADOS, null, miregistro);
        Log.i("Resultado","Alta "+id + " Usuario " +idUsuario+ " Aciertos " +numaciertos +
                " Preguntas " +numpreguntas + " Categoria " +categoria + " Tiempo " +tiempo);
        return id;

    }

    // Calcula el id siguiente de Resultados
    private long nextId() {
        long id = 0;
        String sql = "select MAX(" + Estructura.CAMPO_RESULTADOS_ID + ") from " + Estructura.TABLA_RESULTADOS;
        Log.i("ResultadoDAO", sql);
        Cursor c = basededatos.rawQuery(sql, null);
        if (c.moveToFirst()) {
            id = c.getInt(0);
        }
        return id + 1;
    }
    // RECUPERA LOS RESULTADOS DE UNA CATEGORIA Y UN NUMERO DE PREGUNTAS
    public ArrayList<ResultadoPOJO> hallOfFame(String categoria, int preguntas) {
        String[] campos = new String[]{Estructura.CAMPO_RESULTADOS_ID,
                Estructura.CAMPO_RESULTADOS_USUARIO,
                Estructura.CAMPO_RESULTADOS_ACIERTOS,
                Estructura.CAMPO_RESULTADOS_PREGUNTAS,
                Estructura.CAMPO_RESULTADOS_CATEGORIA,
                Estructura.CAMPO_RESULTADOS_TIEMPO};
        String where = Estructura.CAMPO_RESULTADOS_CATEGORIA + " = '" + categoria + "' AND " + Estructura.CAMPO_RESULTADOS_PREGUNTAS + " = " + preguntas;
        Log.i("ResultadoDAO", where);
        String order = Estructura.CAMPO_RESULTADOS_ACIERTOS + " DESC, " + Estructura.CAMPO_RESULTADOS_TIEMPO + " ASC ";
        Log.i("ResultadoDAO", order);
        Cursor c = basededatos.query(Estructura.TABLA_RESULTADOS, campos, where, null, null, null, order);

        ArrayList<ResultadoPOJO> lista = new ArrayList<>();
        if (c != null & c.moveToFirst()) {
            do {
                ResultadoPOJO registro = new ResultadoPOJO();
                registro.setId(c.getInt(0));
                registro.setIdUsuario(c.getInt(1));
                registro.setNumaciertos(c.getInt(2));
                registro.setNumpreguntas(c.getInt(3));
                registro.setCategorias(c.getString(4));
                registro.setTiempo(c.getInt(5));
                Log.i("Resultado leido ", registro.getId() + " / " + registro.getIdUsuario() + " / " + registro.getNumaciertos()
                        + " / " + registro.getNumpreguntas() + " / " + registro.getCategorias() + " / " + registro.getTiempo());
                lista.add(registro);
            }
            while (c.moveToNext());
        }
        return lista;
    }

}
