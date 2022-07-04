package app.elingeniero.atupuerta.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Timer;

public class Opciones_Pedido extends AppCompatActivity {
    Button btnRest,btnMarket,btnPharm,btnOthers;
    private FusedLocationProviderClient fusedLocationClient;
    private static String Lat;
    private static String Lon;
    private static String Lat2;
    private static String Lon2;
    public LocationManager locManager;
    public Criteria criteria;
    public String bestProvider;
    boolean isGPSEnabled = false;
    public static String getLat() {
        return Lat;
    }
    public static String getLon() {
        return Lon;
    }
    public static String getLat2() {
        return Lat2;
    }
    public static String getLon2() {
        return Lon2;
    }
    Timer timer1;
    LocationResult locationResult;
    LocationManager lm;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones__pedido);
        btnRest=(Button) this.findViewById(R.id.btnRest);
        btnMarket=(Button) this.findViewById(R.id.btnMarket);
        btnPharm=(Button) this.findViewById(R.id.btnPharm);
        btnOthers=(Button) this.findViewById(R.id.btnOthers);
        btnRest.setEnabled(false);
        btnMarket.setEnabled(false);
        btnOthers.setEnabled(false);
        btnPharm.setEnabled(false);
        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(Opciones_Pedido.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                if (ActivityCompat.checkSelfPermission(Opciones_Pedido.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {

                }else {
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
                }
                Intent intencion=new Intent(getApplicationContext(),Lugares_a.class);
                intencion.putExtra(Lugares_a.cod_categoria,"restaurant");
                startActivity(intencion);



            }
        });
        btnMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion=new Intent(getApplicationContext(),Lugares_a.class);
                intencion.putExtra(Lugares_a.cod_categoria,"supermarket");
                startActivity(intencion);

            }
        });
        btnPharm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion=new Intent(getApplicationContext(),Lugares_a.class);
                intencion.putExtra(Lugares_a.cod_categoria,"pharmacy");
                startActivity(intencion);
            }
        });
        btnOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion = new Intent(getApplicationContext(), Rest_Activity.class);
                intencion.putExtra(Rest_Activity.cod_categoria,"4");
                startActivity(intencion);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityCompat.requestPermissions(Opciones_Pedido.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("no hay permiso");
            Toast.makeText(this, "No hay permiso para mostrar la ubicación." , Toast.LENGTH_LONG).show();

        }
        else {
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locManager.getBestProvider(criteria, true)).toString();
            isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                        @Override
                        public void onSuccess(Location location) {

                            // Got last known location. In some rare situations this can be null.
                            System.out.println("linea 114");
                            if (location != null) {
                                // Logic to handle location object
                                location.getLatitude();
                                System.out.println("coordenada anterior "+location.getLatitude());
                                Lat2 = String.valueOf( location.getLatitude());
                                Lon2 = String.valueOf(location.getLongitude());
                            }else{
                                System.out.println("no se obtuvo la coordenada anterior");
                            }
                        }
                    });


            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPS);
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerGPS);

            System.out.println("hola");
            //}
        }



    }

    @Override
    public void onStart() {
        super.onStart();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(Lat==null) {
                    Toast.makeText(getApplicationContext(), "No se pudo establecer la ubicación del dispositivo.", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    btnRest.setEnabled(true);
                    btnMarket.setEnabled(true);
                    btnOthers.setEnabled(true);
                    btnPharm.setEnabled(true);
                }
            }
        }, 10000);

    }

    private final LocationListener locationListenerGPS = new LocationListener() {

        public void onLocationChanged(Location location) {
            System.out.println("location changed");
            Lat = String.valueOf( location.getLatitude());
            Lon = String.valueOf(location.getLongitude());
            System.out.println("coordenada actual: "+Lat);
            progressDialog.dismiss();
            btnRest.setEnabled(true);
            btnMarket.setEnabled(true);
            btnOthers.setEnabled(true);
            btnPharm.setEnabled(true);
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


}
