package app.elingeniero.atupuerta.delivery;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
//import com.example.usuario.delivery.R;


public class EditarPedido extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String id_orden="orden";
    RequestQueue rq,rq2;
    JsonRequest jrq;
    //private static String orden;
    EditText direccion,observacion;
    Button btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);
        direccion=(EditText) this.findViewById(R.id.txtDir);
        observacion=(EditText) this.findViewById(R.id.txtObs);
        btnActualizar=(Button) this.findViewById(R.id.btnActualizar);
        System.out.println("a");
        final String orden=getIntent().getStringExtra("orden");
        System.out.println("b");
        rq = Volley.newRequestQueue(EditarPedido.this);
        rq2 = Volley.newRequestQueue(EditarPedido.this);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar_orden();
            }
        });
        //String url="http://192.168.0.103/pedidos/webservice/detalle_orden.php?id_orden="+orden;
        String url="http://elingeniero.com.ec/atupuerta/webservice/detalle_orden.php?id_orden="+orden;
        System.out.println(url);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject(0);
                    direccion.setText(jsonObject.getString("direccion"));
                    observacion.setText(jsonObject.getString("observacion"));
                    RadioGroup contenedor = (RadioGroup) findViewById(R.id.rgMotivo);
                    RadioButton opcionI2 = (RadioButton) contenedor.getChildAt(jsonObject.getInt("id_motivo"));
                    opcionI2.setChecked(true);
                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
    private void actualizar_orden(){
        RadioGroup rg_motivos = (RadioGroup) findViewById(R.id.rgMotivo);
        int radioButtonId = rg_motivos.getCheckedRadioButtonId();
        View radioButton = rg_motivos.findViewById(radioButtonId);
        int cod_motivo = rg_motivos.indexOfChild(radioButton);
        final String orden=getIntent().getStringExtra("orden");
        String p_direccion=direccion.getText().toString().replace(" ", "%20");
        String p_observacion=observacion.getText().toString().replace(" ", "%20");
        //String url = "http://192.168.0.103/pedidos/webservice/actualizar_orden.php?id_orden="+orden+"&direccion=" + p_direccion+"&observacion="+p_observacion+"&motivo="+cod_motivo;
        String url = "http://elingeniero.com.ec/atupuerta/webservice/actualizar_orden.php?id_orden="+orden+"&direccion=" + p_direccion+"&observacion="+p_observacion+"&motivo="+cod_motivo;
        System.out.println(url);
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq2.add(jrq);
        finish();
    }
}
