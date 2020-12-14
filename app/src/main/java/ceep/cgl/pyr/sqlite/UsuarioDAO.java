package ceep.cgl.pyr.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class UsuarioDAO {

    SQLiteDatabase basededatos;


    public UsuarioDAO(Context context) {
          Estructura estructura = new Estructura(context, Estructura.BDD_NAME, null, Estructura.BDD_VERSION);
        basededatos =estructura.getReadableDatabase();
    }

    // DA DE ALTA UN USUARIO (-1 si se produce error en el alta)
    public long altausuario(String usuario, String password) {
        // insert a traves del metodo insert
        ContentValues miregistro = new ContentValues();
        miregistro.put(Estructura.CAMPO_USUARIOS_ID, nextId());
        miregistro.put(Estructura.CAMPO_USUARIOS_USUARIO, usuario);
        miregistro.put(Estructura.CAMPO_USUARIOS_PASSWORD, password);
        long id= basededatos.insert(Estructura.TABLA_USUARIOS, null, miregistro);
        return id;
    }

    // RECUPERA TODOS LOS USUARIOS
    public ArrayList<UsuarioPOJO> obtenerUsuarios(){
        String[] campos = new String[] {Estructura.CAMPO_USUARIOS_ID, Estructura.CAMPO_USUARIOS_USUARIO,Estructura.CAMPO_USUARIOS_PASSWORD};
        Cursor c = basededatos.query(Estructura.TABLA_USUARIOS, campos, null, null, null, null, null);
        ArrayList<UsuarioPOJO> lista = new ArrayList<>();
        if (c != null & c.moveToFirst()){
            do {
                UsuarioPOJO registro = new UsuarioPOJO();
                registro.setIdUsuario(c.getInt(0));
                registro.setUsuario(c.getString(1));
                registro.setPassword(c.getString(2));
                //Log.i("Usuario leido ", registro.getIdUsuario() + " / " + registro.getUsuario() + " / " + registro.getPassword());
                lista.add(registro);
            }
            while (c.moveToNext());
        }
        return lista;
    }

    // RECUPERA UN USUARIO POR ID(NULL SI NO EXISTE)
    public UsuarioPOJO leerusuario(int id){
        String[] campos = new String[] {Estructura.CAMPO_USUARIOS_ID, Estructura.CAMPO_USUARIOS_USUARIO,Estructura.CAMPO_USUARIOS_PASSWORD};
        String str_where = Estructura.CAMPO_USUARIOS_ID+"="+id;
        Cursor c = basededatos.query(Estructura.TABLA_USUARIOS, campos, str_where, null, null, null, null);
        UsuarioPOJO registro;
        if (c != null & c.moveToFirst()){
            registro = new UsuarioPOJO();
            registro.setIdUsuario(c.getInt(0));
            registro.setUsuario(c.getString(1));
            registro.setPassword(c.getString(2));
            Log.i("Usuario leido ", registro.getIdUsuario() + " / " + registro.getUsuario() + " / " + registro.getPassword());
        } else {
            registro = null;
        }
        return registro;
    }

    // RECUPERA UN USUARIO POR STRING(NULL SI NO EXISTE)
    public UsuarioPOJO leerusuario(String usuario){
        String[] campos = new String[] {Estructura.CAMPO_USUARIOS_ID, Estructura.CAMPO_USUARIOS_USUARIO,Estructura.CAMPO_USUARIOS_PASSWORD};
        String str_where = Estructura.CAMPO_USUARIOS_USUARIO+"='"+usuario+"'";
        Cursor c = basededatos.query(Estructura.TABLA_USUARIOS, campos, str_where, null, null, null, null);
        UsuarioPOJO registro;
        if (c != null & c.moveToFirst()){
            registro = new UsuarioPOJO();
            registro.setIdUsuario(c.getInt(0));
            registro.setUsuario(c.getString(1));
            registro.setPassword(c.getString(2));
            Log.i("Usuario leido ", registro.getIdUsuario() + " / " + registro.getUsuario() + " / " + registro.getPassword());
        } else {
            registro = null;
        }
        return registro;
    }

    // BORRA UN USUARIO
    public void borrarusuario(int id){
        String str_where = Estructura.CAMPO_USUARIOS_ID + "= "+id;
        basededatos.delete(Estructura.TABLA_USUARIOS, str_where, null);
    }

    // MODIFICAR LA PASSWORD
    public void modificarpassword(int id, String password){
        // modificacion a traves del metodo update
        ContentValues miregistro = new ContentValues();
        miregistro.put(Estructura.CAMPO_USUARIOS_PASSWORD, password);
        String str_where = Estructura.CAMPO_USUARIOS_ID + "= "+id;
        basededatos.update(Estructura.TABLA_USUARIOS, miregistro, str_where, null);
    }

    // Calcula el id siguiente
    private long nextId(){
        long id=0;
        String sql= "select MAX("+Estructura.CAMPO_USUARIOS_ID+") from "+Estructura.TABLA_USUARIOS;
        Log.i("UsuarioDAO", sql);
      Cursor c = basededatos.rawQuery(sql,null);
      if (c.moveToFirst()) {
          id=c.getInt(0);
      }
      return id+1;
    }


}
