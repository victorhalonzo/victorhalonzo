package app.elingeniero.atupuerta.delivery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.core.app.ActivityCompat;
import app.elingeniero.atupuerta.delivery.R;
//import com.example.usuario.delivery.R;

public class Menu extends AppCompatActivity {

    Button btnConsultar,btnNuevo,btnEditar,btnOpciones;
    public static final String id_user="usuario";
    private static String id_usuario;
    private static String Lat;
    private static String Lon;
    public LocationManager locManager;
    public Criteria criteria;
    public String bestProvider;
    boolean isGPSEnabled = false;
    public Location loc;

    public static String getLat() {
        return Lat;
    }
    public static String getLon() {
        return Lon;
    }
    public static String getUser() {
        return id_usuario;
    }

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        id_usuario=getIntent().getStringExtra("usuario");
        btnNuevo=(Button) this.findViewById(R.id.btnNew);
        btnConsultar=(Button) this.findViewById(R.id.btnListar);
        btnEditar=(Button) this.findViewById(R.id.btnEditar);
        btnOpciones=(Button) this.findViewById(R.id.btnOpciones);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarPedido();
            }
        });
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarPedidos();
            }
        } );
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPedido();
            }
        });
        btnOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.exit(0);
                opciones();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityCompat.requestPermissions(Menu.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("no hay permiso");
            Toast.makeText(this, "No hay permiso para mostrar la ubicación." , Toast.LENGTH_LONG).show();

        }
        else {

        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

        } else {
            /* No user is signed in */
        }

    }

    private void registrarPedido(){

            Intent intencion = new Intent(this, Opciones_Pedido.class);
            startActivity(intencion);

    }
    private void listarPedidos(){
        Intent intencion=new Intent(this, Listado.class);
        intencion.putExtra(Listado.id_user,id_usuario);
        startActivity(intencion);
    }
    private void editarPedido(){
        Intent intencion=new Intent(this, Listado2.class);
        intencion.putExtra(Listado2.id_user,id_usuario);
        startActivity(intencion);
    }
    private void opciones(){
        Intent intencion=new Intent(this, Opciones_App.class);
        startActivity(intencion);
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

    @Override public void onBackPressed() {
        //System.exit(0);
        finishAffinity();
    }


}
