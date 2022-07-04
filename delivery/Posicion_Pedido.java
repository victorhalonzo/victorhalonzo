package app.elingeniero.atupuerta.delivery;

import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import com.example.usuario.delivery.R;

public class Posicion_Pedido extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,Response.Listener<JSONObject>, Response.ErrorListener,OnMapReadyCallback {

    public static final String id_orden="orden";
    RequestQueue rq;
    Handler handler = new Handler();
    private final int TIEMPO = 5000;
    //public final GoogleMap nmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posicion__pedido);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(Posicion_Pedido.this);
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Do stuff with the map here!
        LatLng sidney=new LatLng(-34,151);
        //googleMap.addMarker(new MarkerOptions().position(sidney).title("marcador"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sidney));

        final GoogleMap nmap= googleMap;

        nmap.clear();
        final String orden=getIntent().getStringExtra("orden");
        //String URL="http://192.168.0.103/pedidos/webservice/monitor_orden.php?id_orden="+orden;
        String URL="http://elingeniero.com.ec/atupuerta/webservice/monitor_orden.php?id_orden="+orden;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        LatLng punto=new LatLng(jsonObject.getDouble("latitud"),jsonObject.getDouble("longitud"));
                        System.out.println(punto);
                        //nmap.addMarker(new MarkerOptions().position(punto).title(jsonObject.getString("id_usuario")));
                        nmap.addMarker(new MarkerOptions().position(punto).title(jsonObject.getString("nombres")));
                        nmap.moveCamera(CameraUpdateFactory.newLatLng(punto ));
                        //nmap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        //nmap.animateCamera(CameraUpdateFactory.zoomBy(1,punto));
                        //System.out.println("tareas "+jsonObject.getString("cliente")+"");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                nmap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error de conexion",Toast.LENGTH_SHORT).show();
            }
        }
        );
        nmap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //googleMap.animateCamera(CameraUpdateFactory.zoomBy(1,punto));
        rq=Volley.newRequestQueue(Posicion_Pedido.this);
        rq.add(jsonArrayRequest);

        handler.postDelayed(new Runnable() {
          public void run() {

        nmap.clear();
        final String orden=getIntent().getStringExtra("orden");
        //String URL="http://192.168.0.103/pedidos/webservice/monitor_orden.php?id_orden="+orden;
        String URL="http://elingeniero.com.ec/atupuerta/webservice/monitor_orden.php?id_orden="+orden;
        System.out.println(URL);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        LatLng punto=new LatLng(jsonObject.getDouble("latitud"),jsonObject.getDouble("longitud"));
                        System.out.println(punto);
                        nmap.addMarker(new MarkerOptions().position(punto).title(jsonObject.getString("nombres")));
                        nmap.moveCamera(CameraUpdateFactory.newLatLng(punto ));

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                nmap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error de conexion",Toast.LENGTH_SHORT).show();
            }
        }
        );
        nmap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //googleMap.animateCamera(CameraUpdateFactory.zoomBy(1,punto));
        rq=Volley.newRequestQueue(Posicion_Pedido.this);
        rq.add(jsonArrayRequest);

        handler.postDelayed(this, TIEMPO);
    }

}, TIEMPO);

    }
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }



}
