package app.elingeniero.atupuerta.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class Rest_Activity extends AppCompatActivity {

    TextView txtdir,txthor,txtbuscar,label_dir,label_hor;
    ImageView foto,foto2;
    Button btnOmitir,btnAceptar;
    private static String place_name;
    List place_types;

    public ViewImageExtended viewImageExtended;
    public Bitmap bitmap,bitmap2 = null; // El bitmap de la imagen
    public FragmentActivity activity;
    public static final String cod_categoria="categoria";
    private static String cod_motivo;

    public static String getPlaceName() {
        return place_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest__activicty);

        txtdir=(TextView) this.findViewById(R.id.txtDireccion);
        label_dir=(TextView) this.findViewById(R.id.label_dir);
        txthor=(TextView) this.findViewById(R.id.txthorario);
        label_hor=(TextView) this.findViewById(R.id.label_hor);
        foto=(ImageView) this.findViewById(R.id.foto);
        foto2=(ImageView) this.findViewById(R.id.foto2);
        btnOmitir=(Button) this.findViewById(R.id.btnOmitir);
        btnAceptar=(Button) this.findViewById(R.id.btnAceptar);
        txtbuscar=(TextView) this.findViewById(R.id.txtBuscar);
        cod_motivo=getIntent().getStringExtra("categoria");
        if(cod_motivo.equals("1")){txtbuscar.setText("Buscar restaurante");}
        if(cod_motivo.equals("2")){txtbuscar.setText("Buscar supermercado");}
        if(cod_motivo.equals("3")){txtbuscar.setText("Buscar lugar");}
        if(cod_motivo.equals("4")){txtbuscar.setText("Buscar lugar");}
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.OPENING_HOURS,Place.Field.PHOTO_METADATAS,Place.Field.TYPES));
        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        final List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId()+", "+place.getAddress()+", "+place.getOpeningHours()+", "+place.getTypes());
                label_dir.setVisibility(View.VISIBLE);
                txtdir.setText(place.getAddress());
                System.out.println(place.getTypes());
                Iterator<Place.Type> it = place.getTypes().iterator();
                while (it.hasNext()) {
                    System.out.println(it.next().toString());

                }
                place_types=place.getTypes();
                place_name=place.getName();
                label_hor.setVisibility(View.VISIBLE);
                if(place.getOpeningHours()!=null) {
                    String ArrayData = "";
                    for (String textItem : place.getOpeningHours().getWeekdayText()) {
                        ArrayData = ArrayData + "\n" + textItem;
                    }
                    String horario=ArrayData.replace("Monday","Lunes");
                    horario=horario.replace("Tuesday","Martes");
                    horario=horario.replace("Wednesday","Miercoles");
                    horario=horario.replace("Thursday","Jueves");
                    horario=horario.replace("Friday","Viernes");
                    horario=horario.replace("Saturday","Sabado");
                    horario=horario.replace("Sunday","Domingo");
                    txthor.setText(horario);
                }
                show_photo(place.getId(),place);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
        }

        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(
                new LatLng(-1.0719134958945062, -80.49044542069339),
                new LatLng(-1.0115052348729847, -80.37744911220072)));

        //autocompleteFragment.setTypeFilter(TypeFilter.);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewImageExtended == null || viewImageExtended.getDialog() == null || !viewImageExtended.getDialog().isShowing()){

                    // Si estas en un activity
                    FragmentManager fm = getSupportFragmentManager();
                    // Si estas en un fragment y pasaste el activity en el constructor
                    //FragmentManager fm = this.activity.getSupportFragmentManager();

                    Bundle arguments = new Bundle();

                    // Aqui le pasas el bitmap de la imagen
                    arguments.putParcelable("PICTURE_SELECTED", bitmap);
                    viewImageExtended = ViewImageExtended.newInstance(arguments);
                    viewImageExtended.show(fm, "ViewImageExtended");
                }
            }
        });
        foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewImageExtended == null || viewImageExtended.getDialog() == null || !viewImageExtended.getDialog().isShowing()){

                    // Si estas en un activity
                    FragmentManager fm = getSupportFragmentManager();
                    // Si estas en un fragment y pasaste el activity en el constructor
                    //FragmentManager fm = this.activity.getSupportFragmentManager();

                    Bundle arguments = new Bundle();

                    // Aqui le pasas el bitmap de la imagen
                    arguments.putParcelable("PICTURE_SELECTED", bitmap2);
                    viewImageExtended = ViewImageExtended.newInstance(arguments);
                    viewImageExtended.show(fm, "ViewImageExtended");
                }
            }
        });

        btnOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_name="";
                Intent intencion=new Intent(getApplicationContext(),RegistrarOrden.class);
                startActivity(intencion);
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c=0;
                String tipo;
                System.out.println("motivo "+cod_motivo);
                String observacion="";
                if(cod_motivo.equals("1")){observacion="comida/restaurante";}
                if(cod_motivo.equals("2")){observacion="supermercado";}
                if(cod_motivo.equals("3")){observacion="farmacia";}

                Iterator<Place.Type> it = place_types.iterator();
                while (it.hasNext()) {
                    //System.out.println(it.next().toString());
                    tipo=it.next().toString();
                    System.out.println(tipo);
                    if(cod_motivo.equals("1") && (tipo=="RESTAURANTE" || tipo=="FOOD")){
                        c=1;
                    }
                    if(cod_motivo.equals("2") && (tipo=="SUPERMARKET" || tipo=="GROCERY_OR_SUPERMARKET")){
                        c=1;
                    }
                    if(cod_motivo.equals("3") && (tipo=="PHARMACY")){
                        c=1;
                    }
                    if(cod_motivo.equals("4")){
                        c=1;
                    }
                }
                if(c==1){
                    Intent intencion=new Intent(getApplicationContext(), RegistrarOrden.class);
                    intencion.putExtra(RegistrarOrden.place_name,place_name);
                    startActivity(intencion);
                }else{
                    Toast.makeText(Rest_Activity.this, "Seleccione un lugar de tipo "+observacion+".", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private  void show_photo(String placeId,Place place){

        foto.setImageDrawable(null);
        foto2.setImageDrawable(null);
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {


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
                            //.setMaxWidth(500) // Optional.
                            //.setMaxHeight(300) // Optional.
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
                if(photoMetadata2!=null) {
                    FetchPhotoRequest photoRequest2 = FetchPhotoRequest.builder(photoMetadata2)
                            //.setMaxWidth(1000) // Optional.
                            //.setMaxHeight(600) // Optional.
                            .build();
                    placesClient.fetchPhoto(photoRequest2).addOnSuccessListener((fetchPhotoResponse) -> {
                        bitmap2 = fetchPhotoResponse.getBitmap();
                        foto2.setImageBitmap(bitmap2);
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
    }


}
