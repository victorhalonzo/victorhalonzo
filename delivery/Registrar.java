package app.elingeniero.atupuerta.delivery;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
//import com.example.usuario.delivery.R;



public class Registrar extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    RequestQueue rq;
    JsonRequest jrq;
    EditText txtCliente, txtDir, txtObs, txt_cont;
    TextView txtLat,txtLong,txt_lugar_pedido;
    Button  btnRegistrar;
    private RadioGroup radiogr;
    ImageButton btnMap;
    String id_cliente;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_registrar, container, false);
        View vista1 = inflater.inflate(R.layout.activity_registrar_orden, container, false);

        txtDir=(EditText) vista.findViewById(R.id.txtDir);
        txtObs=(EditText) vista.findViewById(R.id.txtObs);
        txt_cont=(EditText) vista.findViewById(R.id.txtTel);
        btnRegistrar = (Button) vista.findViewById(R.id.btnRegistrar);
        txt_lugar_pedido=(TextView) vista.findViewById(R.id.txt_lugar_pedido);
        //radiogr=(RadioGroup) vista.findViewById(R.id.rgMotivo);
        btnMap=(ImageButton) vista.findViewById(R.id.ubicacion);

        //autocompleteFragment = (AutocompleteSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        //autocompleteFragment = (AutocompleteSupportFragment) vista.getActivity().getSupportFragmentManager().findViewById(R.id.autocomplete_fragment);


        rq = Volley.newRequestQueue(getContext());
        //String lat = getIntent().getExtras().getString("lat");
        //String lon = getIntent().getExtras().getString("lon");
        System.out.println(RegistrarOrden.getLat());
        System.out.println("la direccion del mapa es "+RegistrarOrden.getDir());
        String dir=RegistrarOrden.getDir();
        txtDir.setText(dir);
        System.out.println(RegistrarOrden.getNumero());
        txt_cont.setText(RegistrarOrden.getNumero());
        //txt_cont.setEllipsize(RegistrarOrden.getPho);
        //txt_lugar_pedido.setText(Rest_Activity.getPlaceName());
        txt_lugar_pedido.setText(RegistrarOrden.getLugarPedido());

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String direccion=txtDir.getText().toString();
                String observacion=txtObs.getText().toString();
                String telefono=txt_cont.getText().toString();

                    if(direccion.equals("")){
                        Toast.makeText(getContext(), "Ingrese el lugar de destino.", Toast.LENGTH_LONG).show();
                    }else {
                        if(observacion.equals("")){
                            Toast.makeText(getContext(), "Ingrese el detalle de la solicitud.", Toast.LENGTH_LONG).show();
                        }else {
                            if(telefono.equals("")){
                                Toast.makeText(getContext(), "Ingrese un número de teléfono por si necesitamos contactarlo.", Toast.LENGTH_LONG).show();
                            }else {
                                registrar_orden();
                            }
                        }
                    }

            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion= new Intent(getContext(), Destino_Pedido.class);
                intencion.putExtra(Destino_Pedido.id_user,RegistrarOrden.getUser());
                intencion.putExtra(Destino_Pedido.lat_act,Opciones_Pedido.getLat());
                intencion.putExtra(Destino_Pedido.lon_act,Opciones_Pedido.getLon());
                startActivity(intencion);
            }
        });

        //String lat=Menu.getLat();
        //String lon=Menu.getLon();
        String lat=Opciones_Pedido.getLat();
        String lon=Opciones_Pedido.getLon();
        String direccion="";
        if(lat!=null && lon!=null && dir==null) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            // Get a list of possible addresses from this location, but only take the first one
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat),Double.parseDouble(lon), 1);
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
            txtDir.setText(direccion);

        }

        // Inflate the layout for this fragment
        return vista;
    }


    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se pudo registrar el pedido." +error.toString(), Toast.LENGTH_LONG).show();
}


    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Se ha registrado el pedido con éxito.", Toast.LENGTH_SHORT).show();

        Intent intencion=new Intent(getContext(), Menu.class);
        intencion.putExtra(Menu.id_user, id_cliente);
        startActivity(intencion);
    }



    void registrar_orden(){

        String lat=RegistrarOrden.getLat2();
        String lon=RegistrarOrden.getLon2();
        if (lat==null || lon==null){
            lat=Opciones_Pedido.getLat();
            lon=Opciones_Pedido.getLon();
        }
        System.out.println(RegistrarOrden.getLat2());
        System.out.println(Menu.getUser());
        String direccion=txtDir.getText().toString().replace(" ", "%20");
        direccion=direccion.replace("&","%26");
        direccion=direccion.replaceAll("(\n|\r)", "%20");
        direccion=direccion.replace(",%20Ecuador","");
        String lugar_pedido=txt_lugar_pedido.getText().toString().replace(" ", "%20");
        lugar_pedido=lugar_pedido.replace("&","%26");
        lugar_pedido=lugar_pedido.replaceAll("(\n|\r)", "%20");
        String observacion=txtObs.getText().toString().replace(" ", "%20");
        observacion=observacion.replaceAll("(\n|\r)", "%20");
        id_cliente=Menu.getUser();
        //String url = "http://192.168.0.101/delivery/webservice/registrar_orden.php?id_cliente="+id_cliente+
          //      "&dir="+direccion+"&obs="+observacion+"&lat="+lat+"&lon="+lon+"&motivo="+1+"&lugar_pedido="+lugar_pedido;
        String url = "http://elingeniero.com.ec/atupuerta/webservice/registrar_orden.php?id_cliente="+id_cliente+
                "&dir="+direccion+"&tel="+txt_cont.getText()+"&obs="+observacion+"&lat="+lat+"&lon="+lon+"&motivo="+1+"&lugar_pedido="+lugar_pedido;
        System.out.println(url);

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
        txt_lugar_pedido.setText("");
        txtDir.setText("");
        txtObs.setText("");
        txt_cont.setText("");
    }



    //@Override public void onBackPressed() { }

}
