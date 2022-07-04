package app.elingeniero.atupuerta.delivery;

import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.ActivityCompat;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONObject;
//import com.example.usuario.delivery.R;


public class RegistrarOrden extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String nombres="names";
    public static final String id_user="usuario";
    public static final String Lat2="latitud";
    public static final String Lon2="longitud";
    public static final String Dir="direccion";
    public static final String place_name="nombre_lugar";
    public static String lugar_pedido;
    public static String numero;

    public TextView tvLatitud, tvLongitud, tvAltura, tvPrecision;
    public LocationManager locManager;
    public Location loc;
    public static final int REQUEST_LOCATION = 1;
    public final static String EXTRA_LATITUD="com.example.demo.MESSAGE";
    public final static String EXTRA_LONGITUD="com.example.demo.MESSAGE";

    private static String id_usuario;
    private static String Lat;
    private static String Lon;

    private static String latitud;
    private static String longitud;
    private static String direccion;

    public static String getLat() {
        return Lat;
    }
    public static String getLon() {
        return Lon;
    }
    public static String getUser() {
        return id_usuario;
    }

    public static String getLat2() {
        return latitud;
    }
    public static String getLon2() {
        return longitud;
    }
    public static String getDir() {
        return direccion;
    }
    public static String getLugarPedido(){return lugar_pedido;}
    public static String getNumero(){return numero;}

    boolean isGPSEnabled = false;

    protected LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    //private final Context mContext;
    android.location.Location location;

    RequestQueue rq;
    JsonRequest jrq;
    EditText txtCliente, txtDir, txtObs;
    TextView txtLat,txtLong,txt_lugar_pedido;
    Button btnRegistrar;
    private RadioGroup radiogr;
    ImageButton btnMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_orden);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        direccion=getIntent().getStringExtra("direccion");
        lugar_pedido=getIntent().getStringExtra("nombre_lugar");
        numero=getPhoneNumber();

        FragmentManager fm =  getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.form_registro, new Registrar()).commit();

    }

    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "No se pudo registrar el pedido." +error.toString(), Toast.LENGTH_LONG).show();
    }


    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Se ha registrado el pedido con éxito.", Toast.LENGTH_SHORT).show();

        Intent intencion=new Intent(getApplicationContext(), Menu.class);
        intencion.putExtra(Menu.id_user, RegistrarOrden.getUser());
        startActivity(intencion);
    }

    void registrar_orden(){
        //192.168.1.66(172.29.243.3

        int radioButtonId = radiogr.getCheckedRadioButtonId();
        View radioButton = radiogr.findViewById(radioButtonId);
        int indice = radiogr.indexOfChild(radioButton);
        //RadioButton rb = (RadioButton)  radiogr.getChildAt(indice);
        //String motivo = rb.getText().toString();
        System.out.println(indice);
        int cod_motivo=indice;

        //rb.getId();

        String lat=RegistrarOrden.getLat2();
        String lon=RegistrarOrden.getLon2();
        if (lat==null || lon==null){
            lat=Menu.getLat();
            lon=Menu.getLon();
        }
        System.out.println(RegistrarOrden.getLat2());
        System.out.println(RegistrarOrden.getUser());
        String cliente=txtCliente.getText().toString().replace(" ", "%20");
        String direccion=txtDir.getText().toString().replace(" ", "%20");
        direccion=direccion.replace("&","%26");
        direccion=direccion.replaceAll("(\n|\r)", "%20");
        String observacion=txtObs.getText().toString().replace(" ", "%20");
        observacion=observacion.replaceAll("(\n|\r)", "%20");
        String id_cliente=RegistrarOrden.getUser();
        //final String usuario=getIntent().getStringExtra("usuario");
        //String url = "http://192.168.0.103/pedidos/webservice/registrar_orden.php?id_cliente="+id_cliente+"&cliente="+cliente+
        //      "&dir="+direccion+"&obs="+observacion+"&lat="+lat+"&lon="+lon+"&motivo="+cod_motivo;
        String url = "http://35.239.252.182/delivery/webservice/registrar_orden.php?id_cliente="+id_cliente+"&cliente="+cliente+
                "&dir="+direccion+"&obs="+observacion+"&lat="+lat+"&lon="+lon+"&motivo="+cod_motivo;
        System.out.println(url);

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
        txtCliente.setText("");
        txtDir.setText("");
        txtObs.setText("");
    }

    private final LocationListener locationListenerGPS = new LocationListener() {

        public void onLocationChanged(Location location) {
            System.out.println("location changed");
            Lat = String.valueOf( location.getLatitude());
            Lon = String.valueOf(location.getLongitude());
            System.out.println("coordenada actual: "+Lat);
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

    public String getPhoneNumber(){
        ActivityCompat.requestPermissions(RegistrarOrden.this,new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        if (ActivityCompat.checkSelfPermission(RegistrarOrden.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        else{

            TelephonyManager mTelephonyManager;
            mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return mTelephonyManager.getLine1Number();
        }
    }

}
