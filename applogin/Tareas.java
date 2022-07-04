package com.example.usuario.applogin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tareas extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String url="url";
    public static final String usu="usuario";
    RequestQueue rq,rq2;
    JsonRequest jrq;
    String orden="";
    String observacion="";
    private static String Lat;
    private static String Lon;
    public LocationManager locManager;
    public Location loc;
    boolean isGPSEnabled = false;
    String usuario;
    private FusedLocationProviderClient fusedLocationClient;
    private static String Lat2;
    private static String Lon2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);
        rq2 = Volley.newRequestQueue(Tareas.this);
        String url=getIntent().getStringExtra("url");
        usuario=getIntent().getStringExtra("usuario");
        System.out.println(url);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                LinearLayout linearLayout = new LinearLayout(Tareas.this);
                ScrollView sv=new ScrollView(Tareas.this);
                sv.addView(linearLayout);
                setContentView(sv);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                Button mapa=new Button(Tareas.this);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        orden=jsonObject.getString("id_orden");
                        observacion=jsonObject.getString("observacion");

                        //final CheckBox tarea = new CheckBox(Tareas.this);
                        final TextView tarea=new TextView(Tareas.this);
                        tarea.setText(jsonObject.getString("id_orden")+" - " +jsonObject.getString("cliente")+" - "+jsonObject.getString("direccion"));
                        int numero=Integer.parseInt(jsonObject.getString("id_orden"));
                        //tarea.set(jsonObject.getString("id_orden"));
                        tarea.setId(numero);
                        tarea.setTypeface(null, Typeface.BOLD);
                        tarea.setPadding(10,10,10,10);
                        linearLayout.addView(tarea);
                        //sv.addView(tarea);

                        final Button obs_tarea=new Button(Tareas.this);
                        obs_tarea.setText("Ver Detalle");
                        linearLayout.addView(obs_tarea);
                        //sv.addView(tarea);

                        final TextView id_orden=new TextView(Tareas.this);
                        id_orden.setText(jsonObject.getString("id_orden"));
                        id_orden.setVisibility(View.INVISIBLE);

                        final TextView cliente=new TextView(Tareas.this);
                        cliente.setText(jsonObject.getString("cliente"));
                        cliente.setVisibility(View.INVISIBLE);

                        final TextView lugar_pedido=new TextView(Tareas.this);
                        lugar_pedido.setText(jsonObject.getString("lugar_pedido"));
                        lugar_pedido.setVisibility(View.INVISIBLE);

                        final TextView lugar_entrega=new TextView(Tareas.this);
                        lugar_entrega.setText(jsonObject.getString("direccion"));
                        lugar_entrega.setVisibility(View.INVISIBLE);

                        final TextView observ=new TextView(Tareas.this);
                        observ.setText(jsonObject.getString("observacion"));
                        observ.setVisibility(View.INVISIBLE);

                        final TextView numero_contacto=new TextView(Tareas.this);
                        numero_contacto.setText(jsonObject.getString("telefono"));
                        numero_contacto.setVisibility(View.INVISIBLE);

                        final TextView latitud=new TextView(Tareas.this);
                        latitud.setText(jsonObject.getString("latitud"));
                        latitud.setVisibility(View.INVISIBLE);

                        final TextView longitud=new TextView(Tareas.this);
                        longitud.setText(jsonObject.getString("longitud"));
                        longitud.setVisibility(View.INVISIBLE);

                        System.out.println("tareas "+jsonObject.getString("cliente")+"");

                        /*tarea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String est="";
                                if (tarea.isChecked()){
                                    est="P";
                                }else{
                                    est="A";
                                }
                                //String url = "http://192.168.2.67/pedidos/webservice/actualizar_estado.php?id_orden="+tarea.getId()+"&est=" + est;
                                String url = "http://35.239.252.182/delivery/webservice/actualizar_estado.php?id_orden="+tarea.getId()+"&est=" + est;
                                System.out.println(url);
                                jrq = new JsonObjectRequest(Request.Method.GET, url, null, Tareas.this, Tareas.this);
                                rq2.add(jrq);
                            }
                        });*/

                        obs_tarea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intencion = new Intent(Tareas.this, Detalle_Tarea.class);
                                intencion.putExtra(Detalle_Tarea.id, id_orden.getText());
                                intencion.putExtra(Detalle_Tarea.cli, cliente.getText());
                                intencion.putExtra(Detalle_Tarea.lug_ped, lugar_pedido.getText());
                                intencion.putExtra(Detalle_Tarea.lug_ent, lugar_entrega.getText());
                                intencion.putExtra(Detalle_Tarea.obs, observ.getText());
                                intencion.putExtra(Detalle_Tarea.num_cont, numero_contacto.getText());
                                intencion.putExtra(Detalle_Tarea.lat, latitud.getText());
                                intencion.putExtra(Detalle_Tarea.lon, longitud.getText());
                                intencion.putExtra(Detalle_Tarea.usu,usuario);
                                tarea.getId();
                                startActivity(intencion);
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
                        setContentView(R.layout.activity_main2);
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No tiene tareas asignadas el día de hoy.",Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq=Volley.newRequestQueue(this);
        rq.add(jsonArrayRequest);

        ActivityCompat.requestPermissions(Tareas.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            return;

        }else
        {
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);



            if (isGPSEnabled) {
                loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //loc = locManager.getLastKnownLocation(bestProvider);
                if (loc != null) {

                    System.out.println("loc");
                    locManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
                }else{
                    System.out.println("loc2");
                    locManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerGPS);

                }


            }
        }

    }

    public void onErrorResponse(VolleyError error) {

    }


    public void onResponse(JSONObject response) {

    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            System.out.println("location changed");
            Lat = String.valueOf(location.getLatitude());
            Lon = String.valueOf(location.getLongitude());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //String url = "http://192.168.2.67/pedidos/webservice/registrar_gps.php?id_usuario="+usuario+"&lat=" + Lat + "&lon=" + Lon;
                    String url = "http://elingeniero.com.ec/atupuerta/webservice/registrar_gps.php?id_usuario="+usuario+"&lat=" + Lat + "&lon=" + Lon;
                    System.out.println(url);
                    jrq = new JsonObjectRequest(Request.Method.GET, url, null, Tareas.this, Tareas.this);
                    rq.add(jrq);

                }
            });
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };
    @Override public void onBackPressed() {

    }
}
