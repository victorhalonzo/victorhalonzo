package app.elingeniero.atupuerta.delivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import com.example.usuario.delivery.R;



public class Login extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    RequestQueue rq;
    JsonRequest jrq;
    EditText cajaUser, cajaPwd;
    Button btnConsultar,btnNuevo;
    TextView txtObtClave;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {


        //return inflater.inflate(R.layout.fragment_sesion,container,false);
        View vista=inflater.inflate(R.layout.fragment_login,container,false);
        cajaUser=(EditText) vista.findViewById(R.id.txtUser);
        cajaPwd=(EditText) vista.findViewById(R.id.txtPwd);
        btnConsultar=(Button) vista.findViewById(R.id.btnSesion);
        btnNuevo=(Button) vista.findViewById(R.id.btnNuevo);
        txtObtClave=(TextView) vista.findViewById(R.id.txt_obt_clave);
        rq= Volley.newRequestQueue(getContext());

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarCliente();
            }
        });
        txtObtClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion = new Intent(getContext(), RecuperarClave.class);
                startActivity(intencion);
            }
        });

        return vista;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        //Toast.makeText(getContext(),"No se encontró el usuario"+error.toString(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(),"No se encontró el usuario",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        User usuario= new User();
        Toast.makeText(getContext(),"Se ha encontrado el usuario "+ cajaUser.getText().toString(),Toast.LENGTH_SHORT).show();

        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        try{
            jsonObject=jsonArray.getJSONObject(0);
            usuario.setUser(jsonObject.optString("id_cliente"));
            usuario.setNames(jsonObject.optString("nombres"));

        }catch (JSONException e){
            e.printStackTrace();
        }
        /*Intent intencion = new Intent(getContext(), RegistrarOrden.class);
        intencion.putExtra(RegistrarOrden.nombres, usuario.getNames());
        intencion.putExtra(RegistrarOrden.id_user, usuario.getUser());
        startActivity(intencion);*/
        Intent intencion = new Intent(getContext(), Menu.class);
        intencion.putExtra(Menu.id_user, usuario.getUser());
        startActivity(intencion);


    }

    private void iniciarSesion(){

        //String url="http://192.168.0.103/pedidos/webservice/sesion_cliente.php?user="+cajaUser.getText().toString()+
          // "&pwd="+cajaPwd.getText().toString();
        String url="http://elingeniero.com.ec/atupuerta/webservice/sesion_cliente.php?user="+cajaUser.getText().toString()+
             "&pwd="+cajaPwd.getText().toString();
        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }
    private void registrarCliente(){

        Intent intencion = new Intent(getContext(), RegistroCliente.class);
        startActivity(intencion);

    }

}
