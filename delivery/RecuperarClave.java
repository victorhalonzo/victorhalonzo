package app.elingeniero.atupuerta.delivery;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.elingeniero.atupuerta.delivery.R;

public class RecuperarClave extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    Button btnEnviar;
    String dest,clave,clave_cliente;
    RequestQueue rq,rq2;
    EditText mail;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_clave);
        mail=(EditText) this.findViewById(R.id.mail);
        btnEnviar = (Button) this.findViewById(R.id.btn_enviar_clave);
        rq= Volley.newRequestQueue(this);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {

                String url="http://elingeniero.com.ec/atupuerta/webservice/get_clave.php?mail="+mail.getText().toString();

                jrq= new JsonObjectRequest(Request.Method.GET,url,null,RecuperarClave.this,RecuperarClave.this);
                rq.add(jrq);

            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //Toast.makeText(getContext(),"No se encontró el usuario"+error.toString(),Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"No se encontró el usuario",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        User usuario= new User();


        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        try{
            jsonObject=jsonArray.getJSONObject(0);
            clave_cliente=jsonObject.optString("clave");
            enviarmail();

        }catch (JSONException e){
            e.printStackTrace();
        }



    }

    private void enviarmail(){
        String url="http://elingeniero.com.ec/atupuerta/webservice/cuenta_notificador.php";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = response.getJSONObject(0);
                    dest= jsonObject.getString("usuario");
                    clave= jsonObject.getString("clave");
                    System.out.println("try "+dest);
                    new EnviarMail().execute(
                            new EnviarMail.Mail(dest,clave, mail.getText().toString(), "Home Delivery", "Su clave es: "+clave_cliente+"")
                    );
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("c "+dest);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error de conexion",Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq2=Volley.newRequestQueue(RecuperarClave.this);
        rq2.add(jsonArrayRequest);

    }


}
