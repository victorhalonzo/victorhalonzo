package com.example.usuario.applogin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import android.view.LayoutInflater;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 public class Main2Activity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,Response.Listener<JSONObject>, Response.ErrorListener,OnMapReadyCallback {
public static final String nombres="names";
public static final String id_user="usuario";
    TextView cajaBienvenido,cajaPedido;
    RequestQueue rq,rq2,rq3,rq4;
    JsonRequest jrq;
    public LocationManager locManager;
    public Location loc;
    boolean isGPSEnabled = false;
    private static String Lat,nmap;
    private static String Lon;
    Button btnConsultar,btnReload;
    GoogleMap googleMap2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //return inflater.inflate(R.layout.fragment_sesion,container,false);
        View vista=inflater.inflate(R.layout.activity_main2,container,false);


        return vista;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        rq = Volley.newRequestQueue(Main2Activity.this);
        rq2 = Volley.newRequestQueue(Main2Activity.this);
        //cajaBienvenido=(TextView)findViewById(R.id.txtBienvenido);
        //cajaPedido=(TextView)findViewById(R.id.txtPedido);
        String usuario=getIntent().getStringExtra("names");
        //cajaBienvenido.setText("Bienvenido "+usuario);
        btnConsultar=(Button) findViewById(R.id.btnGet);
        btnReload=(Button) findViewById(R.id.btnReload);

        //getOrdenes("http://192.168.0.104/pedidos/webservice/listar_ordenes.php?id_usuario="+usuario);

        btnConsultar.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                System.out.println("clic");
                final String usuario=getIntent().getStringExtra("usuario");
                //getOrdenes2("http://192.168.2.67/pedidos/webservice/listar_ordenes.php?id_usuario="+usuario);
                getOrdenes2("http://35.239.252.182/delivery/webservice/listar_ordenes.php?id_usuario="+usuario);
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                //recargarMapa();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(Main2Activity.this);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(Main2Activity.this);

        ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

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


                    locManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 2 * 20 * 1000, 10, locationListenerGPS);
                }else{
                    locManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 2 * 20 * 1000, 10, locationListenerGPS);

                }


            }
        }

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Do stuff with the map here!
        LatLng sidney=new LatLng(-34,151);
        //googleMap.addMarker(new MarkerOptions().position(sidney).title("marcador"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sidney));
        final GoogleMap nmap=googleMap;

        final String usuario=getIntent().getStringExtra("usuario");
        //String URL="http://35.239.252.182/delivery/webservice/listar_ordenes.php?id_usuario="+usuario;
        //String URL="http://192.168.2.67/pedidos/webservice/listar_ordenes.php?id_usuario="+usuario;
        String URL="http://35.239.252.182/delivery/webservice/listar_ordenes.php?id_usuario="+usuario;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("filas "+response.length());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        LatLng punto=new LatLng(jsonObject.getDouble("latitud"),jsonObject.getDouble("longitud"));
                        System.out.println(punto);
                        nmap.addMarker(new MarkerOptions().position(punto).title(jsonObject.getString("cliente")));
                        nmap.moveCamera(CameraUpdateFactory.newLatLng(punto ));
                        nmap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        //nmap.animateCamera(CameraUpdateFactory.zoomBy(1,punto));
                        System.out.println("tareas "+jsonObject.getString("cliente")+"");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No tiene tareas asignadas el día de hoy.",Toast.LENGTH_SHORT).show();
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(1));
            }
        }
        );
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //googleMap.animateCamera(CameraUpdateFactory.zoomBy(1,punto));
        rq3=Volley.newRequestQueue(this);
        rq3.add(jsonArrayRequest);
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            Lat = String.valueOf(location.getLatitude());
            Lon = String.valueOf(location.getLongitude());
            final String usuario=getIntent().getStringExtra("usuario");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //String url = "http://192.168.2.67/pedidos/webservice/registrar_gps.php?id_usuario="+usuario+"&lat=" + Lat + "&lon=" + Lon;
                    String url = "http://35.239.252.182/delivery/webservice/registrar_gps.php?id_usuario="+usuario+"&lat=" + Lat + "&lon=" + Lon;
                    jrq = new JsonObjectRequest(Request.Method.GET, url, null, Main2Activity.this, Main2Activity.this);
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

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
    private void getOrdenes(String URL){



        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                LinearLayout linearLayout = new LinearLayout(Main2Activity.this);
                setContentView(linearLayout);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                Button mapa=new Button(Main2Activity.this);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        /*TextView textView = new TextView(Main2Activity.this);
                        textView.setText(jsonObject.getString("cliente")+" - "+jsonObject.getString("direccion"));
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setPadding(10,10,10,10);
                        linearLayout.addView(textView);*/

                        CheckBox tarea = new CheckBox(Main2Activity.this);
                        tarea.setText(jsonObject.getString("cliente")+" - "+jsonObject.getString("direccion"));
                        tarea.setTypeface(null, Typeface.BOLD);
                        tarea.setPadding(10,10,10,10);
                        linearLayout.addView(tarea);

                        System.out.println("tareas "+jsonObject.getString("cliente")+"");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                linearLayout.addView(mapa);
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
                Toast.makeText(getApplicationContext(),"error de conexion",Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq2=Volley.newRequestQueue(this);rq2.add(jsonArrayRequest);




    }

    private void getOrdenes2(String URL){
        Intent intencion = new Intent(Main2Activity.this, Tareas.class);
        intencion.putExtra(Tareas.url, URL);
        startActivity(intencion);


    }

    private void recargarMapa(){
        GoogleMap googleMap;
        LatLng sidney=new LatLng(-34,151);
        //googleMap.addMarker(new MarkerOptions().position(sidney).title("marcador"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sidney));
        final GoogleMap nmap=googleMap2;

        final String usuario=getIntent().getStringExtra("usuario");
        //String URL="http://35.239.252.182/delivery/webservice/listar_ordenes.php?id_usuario="+usuario;
        //String URL="http://192.168.2.67/pedidos/webservice/listar_ordenes.php?id_usuario="+usuario;
        String URL="http://35.239.252.182/delivery/webservice/listar_ordenes.php?id_usuario="+usuario;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("filas "+response.length());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        LatLng punto=new LatLng(jsonObject.getDouble("latitud"),jsonObject.getDouble("longitud"));
                        System.out.println(punto);
                        nmap.addMarker(new MarkerOptions().position(punto).title(jsonObject.getString("cliente")));
                        nmap.moveCamera(CameraUpdateFactory.newLatLng(punto ));
                        nmap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        //nmap.animateCamera(CameraUpdateFactory.zoomBy(1,punto));
                        System.out.println("tareas "+jsonObject.getString("cliente")+"");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No tiene tareas asignadas el día de hoy.",Toast.LENGTH_SHORT).show();
                googleMap2.animateCamera(CameraUpdateFactory.zoomTo(1));
            }
        }
        );
        googleMap2.animateCamera(CameraUpdateFactory.zoomTo(15));
        //googleMap.animateCamera(CameraUpdateFactory.zoomBy(1,punto));
        rq4=Volley.newRequestQueue(this);
        rq4.add(jsonArrayRequest);
    }
    @Override public void onBackPressed() { }



}
