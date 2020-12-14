package ceep.cgl.pyr;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogoOpciones extends DialogFragment {

    public static final int VER = 1;
    public static final int EDITAR = 2;
    public static final int BORRAR = 3;

    private String usuario;
    private String usuarioseleccionado;


    public DialogoOpciones(String usuario, String usuarioseleccionado) {
        this.usuario = usuario;
        this.usuarioseleccionado = usuarioseleccionado;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Creamos la vista personalizada para añadirle los eventos
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogo_botones, null);
        Button btn_ver = (Button) v.findViewById(R.id.btn_Ver);
        Button btn_editar = (Button) v.findViewById(R.id.btn_Editar);
        Button btn_borrar = (Button) v.findViewById(R.id.btn_Borrar);

        btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarActividad(VER);
            }
        });

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarActividad(EDITAR);
            }
        });
        btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarActividad(BORRAR);
            }
        });

        // creamos el dialogo con la vista personalizada
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        return builder.create();
    }

    // llamada a la actividad de detalle con la opcion seleccionada
    private void llamarActividad(int opcionseleccionada) {
        Intent intent = new Intent(getActivity(), DetalleUsuarioActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USUARIO", this.usuario);
        bundle.putString("USUARIO_SELECCIONADO", this.usuarioseleccionado);
        bundle.putInt("MODO", opcionseleccionada);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
