package app.elingeniero.atupuerta.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
//import com.example.usuario.delivery.R;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    SignInButton signInButton;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG="SignInActivity";
    private static final int RC_SIGN_IN=9001;
    RequestQueue rq;
    JsonRequest jrq;
    Button signInButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rq = Volley.newRequestQueue(this);


        //FragmentManager fm= getSupportFragmentManager();
        //fm.beginTransaction().replace(R.id.login,new Login()).commit();

        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        System.out.println("googlesignoptions "+gso.getAccount());
        System.out.println("mgoogleapiclient "+mGoogleApiClient);
        System.out.println("create");



    }


    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null) {
            String id=account.getId();
            String nombres=account.getGivenName().replace(" ", "%20");
            String apellidos=account.getFamilyName().replace(" ","%20");
            String email=account.getEmail();

            iniciar_sesion(id,nombres,apellidos,email);
        }else {
            setContentView(R.layout.activity_main);
            signInButton = (SignInButton) findViewById(R.id.sign_button);
            signInButton.setOnClickListener(this);
            signInButton2=(Button) findViewById(R.id.sign_button2);
            signInButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intencion=new Intent(getApplicationContext(),LoginApp.class);
                    startActivity(intencion);
                }
            });
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_button:
                signIn();
                break;
        }
    }



    @Override public void onBackPressed() { }

    private void signIn() {
        //Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //startActivityForResult(signInIntent,1);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSingInResult"+result.isSuccess());
        if(result.isSuccess()){

            GoogleSignInAccount acct= result.getSignInAccount();
            System.out.println("Hello "+ acct.getDisplayName() + " " + acct.getEmail()+ " " + acct.getAccount() );
            System.out.println(acct.getId()+" "+ acct.getFamilyName()+" "+acct.getGivenName());
            String id=acct.getId();
            String nombres=acct.getGivenName().replace(" ", "%20");
            String apellidos=acct.getFamilyName().replace(" ","%20");
            String email=acct.getEmail();
            iniciar_sesion(id,nombres,apellidos,email);

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG,"onConnectionFailed"+connectionResult);
    }

    private void iniciar_sesion(String id,String nombres,String apellidos,String email){
        //String url="http://35.239.252.182/delivery/webservice/validar_usuario_google.php?user="+id+
          //      "&nombres="+nombres+"&apellidos="+apellidos+"&email="+email;
        String url="http://elingeniero.com.ec/atupuerta/webservice/validar_usuario_google.php?user="+id+
                "&nombres="+nombres+"&apellidos="+apellidos+"&email="+email;
        System.out.println(url);
        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    public void onResponse(JSONObject response) {

        User usuario= new User();

        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        try{
            jsonObject=jsonArray.getJSONObject(0);
            usuario.setUser(jsonObject.optString("id_cliente"));
            System.out.println("el usuario es: "+usuario.getUser());
            //usuario.setPwd(jsonObject.optString("clave"));
            //usuario.setNames(jsonObject.optString("nombres"));
            //Toast.makeText(this,"Se ha encontrado el usuario "+ usuario.getNames(),Toast.LENGTH_SHORT).show();
            Intent intencion = new Intent(this, Menu.class);
            intencion.putExtra(Menu.id_user, usuario.getUser());
            startActivity(intencion);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se pudo registrar el usuario." + error.toString(), Toast.LENGTH_LONG).show();
    }
}
