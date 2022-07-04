package app.elingeniero.atupuerta.delivery;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.elingeniero.atupuerta.delivery.R;
//import com.example.usuario.delivery.R;

public class Listado extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String id_user="usuario";
    //public static final String url="url";
    RequestQueue rq,rq2;
    JsonRequest jrq;
    String orden="";
    private static String id_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        id_usuario=getIntent().getStringExtra("usuario");
        rq2 = Volley.newRequestQueue(Listado.this);
        //String url="http://192.168.0.103/pedidos/webservice/listado_cliente.php?id_cliente="+id_usuario;
        String url="http://elingeniero.com.ec/atupuerta/webservice/listado_cliente.php?id_cliente="+id_usuario;
        System.out.println(url);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                LinearLayout linearLayout = new LinearLayout(Listado.this);
                setContentView(linearLayout);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                Button mapa=new Button(Listado.this);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        orden=jsonObject.getString("id_orden");

                        final CheckBox tarea = new CheckBox(Listado.this);
                        tarea.setText("Orden N°"+jsonObject.getString("id_orden")+" - "+jsonObject.getString("fecha_hora"));
                        int numero=Integer.parseInt(jsonObject.getString("id_orden"));
                        tarea.setId(numero);
                        tarea.setTypeface(null, Typeface.BOLD);
                        tarea.setPadding(10,10,10,10);
                        linearLayout.addView(tarea);

                        final TextView cod_orden=new TextView(Listado.this);
                        cod_orden.setText(jsonObject.getString("id_orden"));
                        cod_orden.setVisibility(View.INVISIBLE);

                        final TextView estado=new TextView(Listado.this);
                        estado.setText(jsonObject.getString("estado"));
                        estado.setVisibility(View.INVISIBLE);

                        tarea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(estado.getText().toString().equals("A")) {
                                    Intent intencion = new Intent(Listado.this, Posicion_Pedido.class);
                                    intencion.putExtra(Posicion_Pedido.id_orden, cod_orden.getText());
                                    startActivity(intencion);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Esta orden aún no ha sido asignada a un repartidor.",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                //linearLayout.addView(mapa);
                mapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //setContentView(R.layout.activity_main2);
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error de conexion",Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq=Volley.newRequestQueue(this);
        rq.add(jsonArrayRequest);

    }
    public void onErrorResponse(VolleyError error) {

    }


    public void onResponse(JSONObject response) {

    }
}
