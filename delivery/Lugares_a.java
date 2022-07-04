package app.elingeniero.atupuerta.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class Lugares_a extends AppCompatActivity  implements Response.Listener<JSONObject>, Response.ErrorListener {
    RequestQueue rq;
    public Bitmap bitmap,bitmap2 = null;
    TextView txt_lugares_a;
    ScrollView sv;
    public static final String cod_categoria="categoria";
    private static String cod_motivo;
    Button btnOmitir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout0 = new LinearLayout(Lugares_a.this);

        //setContentView(linearLayout0);

        setContentView(R.layout.activity_lugares_a);
        sv=(ScrollView) this.findViewById(R.id.scrollView);
        btnOmitir=(Button) this.findViewById(R.id.btnOmitir);

        cod_motivo=getIntent().getStringExtra("categoria");

        btnOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion=new Intent(Lugares_a.this, RegistrarOrden.class);
                startActivity(intencion);
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
        }
        String lat=Opciones_Pedido.getLat();
        String lon=Opciones_Pedido.getLon();
        if(lat==null || lon==null){
            lat=Opciones_Pedido.getLat2();
            lon=Opciones_Pedido.getLon2();
        }
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius=1500&type="+cod_motivo+"&key=AIzaSyB1euIdQX9-svMPVaWyDXIh7UqRbslg0_c";
        System.out.println(url);
        //JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                final TextView txt_lugares_a=new TextView(Lugares_a.this);
                txt_lugares_a.setText("Sugerencia de restaurantes");
                txt_lugares_a.setTypeface(null, Typeface.BOLD);

                LinearLayout linearLayout = new LinearLayout(Lugares_a.this);
                //ScrollView sv=new ScrollView(Lugares_a.this);

                //linearLayout.addView(txt_lugares_a);

                sv.addView(linearLayout);

                linearLayout.setOrientation(LinearLayout.VERTICAL);
                Button mapa=new Button(Lugares_a.this);
                //for (int i = 0; i < response.length(); i++) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    int numero=jsonArray.length()> 4 ? 5 : jsonArray.length();
                    System.out.println(jsonArray.length());
                    for (int i = 0; i < numero; i++) {
                        System.out.println(i);
                        JSONObject lugar = jsonArray.getJSONObject(i);
                        String nombre = lugar.getString("name");

                        final TextView txt_lugar=new TextView(Lugares_a.this);
                        txt_lugar.setText(nombre);
                        txt_lugar.setTypeface(null, Typeface.BOLD);
                        txt_lugar.setPadding(10,10,10,10);
                        System.out.println("restaurante: "+nombre);
                        linearLayout.addView(txt_lugar);

                        final ImageView foto=new ImageView(Lugares_a.this);
                        foto.setImageDrawable(null);
                        // Create a new Places client instance.
                        PlacesClient placesClient = Places.createClient(Lugares_a.this);
                        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
                        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);
                        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
                        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(lugar.getString("place_id"), fields);
                        //Place place=lugar;
                        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response2) -> {

                            Place place = response2.getPlace();
                            // Get the photo metadata.

                            if(place.getPhotoMetadatas()!=null) {
                                Integer n_fotos= place.getPhotoMetadatas().size();
                                PhotoMetadata photoMetadata,photoMetadata2=null;
                                if(n_fotos>1) {
                                    photoMetadata = place.getPhotoMetadatas().get(0);
                                    photoMetadata2 = place.getPhotoMetadatas().get(1);
                                }else{
                                    photoMetadata = place.getPhotoMetadatas().get(0);
                                    //photoMetadata2 = place.getPhotoMetadatas().get(1);
                                }
                                if(photoMetadata!=null) {
                                    // Get the attribution text.
                                    String attributions = photoMetadata.getAttributions();

                                    // Create a FetchPhotoRequest.
                                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                            .setMaxWidth(500) // Optional.
                                            .setMaxHeight(300) // Optional.
                                            .build();
                                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                                        //Bitmap bitmap = fetchPhotoResponse.getBitmap();
                                        bitmap = fetchPhotoResponse.getBitmap();
                                        foto.setImageBitmap(bitmap);
                                    }).addOnFailureListener((exception) -> {
                                        if (exception instanceof ApiException) {
                                            ApiException apiException = (ApiException) exception;
                                            int statusCode = apiException.getStatusCode();
                                            // Handle error with given status code.
                                            Log.e(TAG, "Place not found: " + exception.getMessage());
                                        }
                                    });
                                }

                            }
                        });
                        linearLayout.addView(foto);
                        final Button btnlugar=new Button(Lugares_a.this);
                        btnlugar.setText("Seleccionar");
                        linearLayout.addView(btnlugar);
                        btnlugar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intencion=new Intent(Lugares_a.this, RegistrarOrden.class);
                                intencion.putExtra(RegistrarOrden.place_name,txt_lugar.getText());
                                startActivity(intencion);
                            }
                        });
                    }


                }
                    catch (JSONException e) {
                        System.out.println("json exception: "+e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                //}

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error: "+error.getMessage());
                Toast.makeText(getApplicationContext(),"error "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq= Volley.newRequestQueue(this);
        rq.add(request);

    }
    public void onErrorResponse(VolleyError error) {

    }


    public void onResponse(JSONObject response) {

    }

}
