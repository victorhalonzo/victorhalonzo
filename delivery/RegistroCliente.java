package app.elingeniero.atupuerta.delivery;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import app.elingeniero.atupuerta.delivery.R;
//import com.example.usuario.delivery.R;

public class RegistroCliente extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    RequestQueue rq,rq2;
    JsonRequest jrq;
    EditText txtNombres, txtApellidos, txtUsuario, txtClave,txtTelefono,txtMail;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //View vista = inflater.inflate(R.layout.activity_registro_cliente, container, false);
        txtNombres = (EditText) this.findViewById(R.id.txtNombres);
        txtApellidos = (EditText) this.findViewById(R.id.txtApellidos);
        txtTelefono=(EditText) this.findViewById(R.id.txtTelefono);
        txtUsuario = (EditText) this.findViewById(R.id.txtUsuario);
        txtClave = (EditText) this.findViewById(R.id.txtClave);
        txtMail=(EditText) this.findViewById(R.id.txtMail);

        btnRegistrar = (Button) this.findViewById(R.id.btnRegistrar);
        rq = Volley.newRequestQueue(this);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usuario = txtUsuario.getText().toString();
                //String URL="http://192.168.0.102/pedidos/webservice/validar_usuario.php?user="+usuario;
                String URL="http://elingeniero.com.ec/atupuerta/webservice/validar_usuario.php?user="+usuario;
                JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        System.out.println("filas "+response.length());
                        //System.out.println("filas");
                        if(response.length()==0){
                            String nombres = txtNombres.getText().toString();
                            String apellidos = txtApellidos.getText().toString();

                            String clave = txtClave.getText().toString();
                            if(!usuario.equals("") && !clave.equals("") && !nombres.equals("") && !apellidos.equals("")) {
                                registrar_usuario();
                            }else{
                                Toast.makeText(getApplicationContext(), "Complete los campos obligatorios(*)", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Usuario existente",Toast.LENGTH_SHORT).show();
                        }

                        }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"No se pudo registrar el cliente." +error.toString(),Toast.LENGTH_SHORT).show();

                    }
                }
                );
                rq2=Volley.newRequestQueue(RegistroCliente.this);
                rq2.add(jsonArrayRequest);

            }

        });
    }

    public void onResponse(JSONObject response) {
        Toast.makeText(this, "Se ha registrado el usuario con éxito.", Toast.LENGTH_SHORT).show();
        //limpiarCajas();
        Intent intencion=new Intent(this, LoginApp.class);
        startActivity(intencion);
    }

    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se pudo registrar el usuario." + error.toString(), Toast.LENGTH_LONG).show();
    }

    void registrar_usuario() {
        //192.168.1.66(172.29.243.3
        String lat = RegistrarOrden.getLat();
        String lon = RegistrarOrden.getLon();
        System.out.println(RegistrarOrden.getLat());
        String nombres = txtNombres.getText().toString().replace(" ", "%20");
        String apellidos = txtApellidos.getText().toString().replace(" ", "%20");
        String usuario = txtUsuario.getText().toString().replace(" ", "%20");
        String clave = txtClave.getText().toString().replace(" ", "%20");
        String telefono=txtTelefono.getText().toString().replace(" ","%20");
        String mail=txtMail.getText().toString().replace(" ","%20");

        //String url = "http://192.168.0.102/pedidos/webservice/registrar_cliente.php?nombres=" + nombres +
          //      "&apellidos=" + apellidos + "&usuario=" + usuario + "&clave=" + clave+"&identificacion="+identificacion+"&telefono="+telefono;
        String url = "http://elingeniero.com.ec/atupuerta/webservice/registrar_cliente.php?nombres=" + nombres +
              "&apellidos=" + apellidos + "&usuario=" + usuario + "&clave=" + clave+"&mail="+mail+"&telefono="+telefono;
        System.out.println(url);
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
        txtNombres.setText("");
        txtApellidos.setText("");
        txtUsuario.setText("");
        txtClave.setText("");
    }
}
