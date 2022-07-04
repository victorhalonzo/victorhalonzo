package app.elingeniero.atupuerta.delivery;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.elingeniero.atupuerta.delivery.R;

public class Destino_Pedido extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {
    private GoogleMap mapa;
    Button btnAceptar,btnPunto;
    private static String direccion;
    private static Double lat,lon;
    public static final String id_user="usuario";
    public static final String lat_act="lat_act";
    public static final String lon_act="lon_act";
    private static String id_usuario;
    private static String latitud;
    private static String longitud;
    private static String txtlugarpedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino__pedido);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(Destino_Pedido.this);
        btnAceptar=(Button) this.findViewById(R.id.btnAceptar);

        id_usuario=getIntent().getStringExtra("usuario");
        txtlugarpedido=RegistrarOrden.getLugarPedido();

        latitud=Opciones_Pedido.getLat();
        longitud=Opciones_Pedido.getLon();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceptar_ubicacion();
            }
        });

        lat=null;
        lon=null;

    }
    //@Override
    public void onMapReady(final GoogleMap googleMap) {
        mapa= googleMap;
        LatLng punto;
        if (latitud!= null && longitud!=null) {
            punto = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        }else{
            punto=new LatLng(-1.054217361635608,-80.45754510658372);
        }
        System.out.println(punto);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
        }
        mapa.setOnMyLocationButtonClickListener(this);
        //mapa.setOnMyLocationClickListener(this);
        Location location1=null;
        //this.onMyLocationClick(location1);
        mapa.getUiSettings().setMyLocationButtonEnabled(true);*/

        mapa.moveCamera(CameraUpdateFactory.newLatLng(punto ));
        mapa.animateCamera(CameraUpdateFactory.zoomTo(15));
        if (latitud!= null && longitud!=null) {
            mapa.addMarker(new MarkerOptions().position(punto)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        mapa.setOnMapClickListener(this);

    }
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location.getLatitude(), Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                mapa.getCameraPosition().target));
    }
    //@Override
    public void onMapClick(LatLng puntoPulsado) {


        System.out.println(puntoPulsado.latitude);
        System.out.println(puntoPulsado.longitude);
        lat=puntoPulsado.latitude;
        lon=puntoPulsado.longitude;
        mapa.clear();
        mapa.addMarker(new MarkerOptions().position(puntoPulsado)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        /*Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Get a list of possible addresses from this location, but only take the first one
        try {
            List<Address> addresses = geocoder.getFromLocation(puntoPulsado.latitude, puntoPulsado.longitude, 1);
            //List<Address> addresses2 = geocoder.getFromLocation();
            System.out.println(addresses);
            if (addresses.size() > 0) {
                // ...get the address
                Address address = addresses.get(0);

                String locality = address.getLocality(); // e.g. "San Francisco"
                String adminArea = address.getAdminArea(); // e.g. "California"
                String country = address.getCountryName(); // e.g. "United States"
                direccion=address.getAddressLine(0);
                System.out.println(direccion);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    private void aceptar_ubicacion(){

        if(lat==null && lon==null) {
            if (latitud!= null && longitud!=null) {
                lat = Double.parseDouble(latitud);
                lon = Double.parseDouble(longitud);
            }
        }
        if(lat!=null && lon!=null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            // Get a list of possible addresses from this location, but only take the first one
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                //List<Address> addresses2 = geocoder.getFromLocation();
                System.out.println(addresses);
                if (addresses.size() > 0) {
                    // ...get the address
                    Address address = addresses.get(0);

                    String locality = address.getLocality(); // e.g. "San Francisco"
                    String adminArea = address.getAdminArea(); // e.g. "California"
                    String country = address.getCountryName(); // e.g. "United States"
                    direccion = address.getAddressLine(0);
                    System.out.println(direccion);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intencion = new Intent(this, RegistrarOrden.class);
            intencion.putExtra(RegistrarOrden.id_user, id_usuario);
            intencion.putExtra(RegistrarOrden.Dir, direccion);
            intencion.putExtra(RegistrarOrden.place_name,txtlugarpedido);
            intencion.putExtra(RegistrarOrden.Lat2, lat.toString());
            intencion.putExtra(RegistrarOrden.Lon2, lon.toString());
            startActivity(intencion);
        }
        else{
            Toast.makeText(getApplicationContext(),"Seleccione una ubicación.",Toast.LENGTH_SHORT).show();
        }
    }

}
