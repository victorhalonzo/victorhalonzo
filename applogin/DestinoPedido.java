package com.example.usuario.applogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DestinoPedido extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mapa;
    public static String lat="latitud";
    public static String lon="longitud";
    String latitud,longitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino_pedido);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(DestinoPedido.this);
        latitud = getIntent().getStringExtra("latitud");
        longitud=getIntent().getStringExtra("longitud");
    }
    public void onMapReady(final GoogleMap googleMap) {
        mapa= googleMap;
        LatLng punto;
        if (latitud!= null && longitud!=null) {
            punto = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        }else{
            punto=new LatLng(-1.054217361635608,-80.45754510658372);
        }
        System.out.println(punto);



        mapa.moveCamera(CameraUpdateFactory.newLatLng(punto ));
        mapa.animateCamera(CameraUpdateFactory.zoomTo(15));
        if (latitud!= null && longitud!=null) {
            mapa.addMarker(new MarkerOptions().position(punto)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        //mapa.setOnMapClickListener(this);

    }

}
