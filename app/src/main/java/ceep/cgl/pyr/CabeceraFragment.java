package ceep.cgl.pyr;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CabeceraFragment extends Fragment {

    String usuario;
    TextView text_usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // recuperamos la actividad y las views

        //View v=getActivity().getLayoutInflater().inflate(R.layout.fragment_cabecera, null);
        Activity activity = getActivity();
        text_usuario = (TextView) activity.findViewById(R.id.tv_Usuario);
        //text_usuario = (TextView) v.findViewById(R.id.tv_Usuario);

        // recuperamos el usuario recibido como parametro
        Bundle bundle = getArguments();
        Log.i("CabeceraFragment"," antes Usuario"+usuario+"Text Usuario"+text_usuario+"Bundle "+bundle);
        if (bundle!= null) {
            Log.i("CabeceraFragment"," despues Usuario"+usuario+"Text Usuario"+text_usuario+"Bundle "+bundle);
            usuario = bundle.getString("USUARIO");
            text_usuario.setText("usuario ( "+ usuario + " )");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cabecera, container, false);
    }



  }