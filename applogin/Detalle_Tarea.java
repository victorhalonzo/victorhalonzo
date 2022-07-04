package com.example.usuario.applogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Detalle_Tarea extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String id="id_orden";
    public static final String obs="observacion";
    public static final String cli="cliente";
    public static final String lug_ped="lugar_pedido";
    public static final String lug_ent="lugar_entrega";
    public static final String num_cont="numero_contacto";
    public static final String lat="latitud";
    public static final String lon="longitud";
    public static final String usu="usuario";
    RequestQueue rq;
    JsonRequest jrq;
    TextView id_orden,observacion,cliente,lugar_pedido,lugar_entrega,numero_contacto;
    ImageButton btnMap;
    Button confirm;
    String usuario="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__tarea);
        rq = Volley.newRequestQueue(Detalle_Tarea.this);
        usuario=getIntent().getStringExtra("usuario");
        final String id_orden=getIntent().getStringExtra("id_orden");
        String cl=getIntent().getStringExtra("cliente");
        cliente=(TextView) this.findViewById(R.id.txtCliente);
        cliente.setText(cl);
        String lp=getIntent().getStringExtra("lugar_pedido");
        lugar_pedido=(TextView) this.findViewById(R.id.txtLugarPedido);
        lugar_pedido.setText(lp);
        String le=getIntent().getStringExtra("lugar_entrega");
        lugar_entrega=(TextView) this.findViewById(R.id.txtLugarEntrega);
        lugar_entrega.setText(le);
        String ob=getIntent().getStringExtra("observacion");
        observacion=(TextView) this.findViewById(R.id.txtObs);
        observacion.setText(ob);
        String numero=getIntent().getStringExtra("numero_contacto");
        numero_contacto=(TextView) this.findViewById(R.id.txtNumeroContacto);
        numero_contacto.setText(numero);
        final String lat = getIntent().getStringExtra("latitud");
        final String lon=getIntent().getStringExtra("longitud");
        btnMap=(ImageButton) this.findViewById(R.id.ubicacion);
        btnMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intencion= new Intent(Detalle_Tarea.this, DestinoPedido.class);
                intencion.putExtra(DestinoPedido.lat,lat);
                intencion.putExtra(DestinoPedido.lon,lon);
                startActivity(intencion);
            }
        });
        confirm=(Button) this.findViewById(R.id.btnConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Detalle_Tarea.this);
                builder.setCancelable(true);
                builder.setTitle("A Tu Puerta");
                builder.setMessage("Desea confirmar la atención de esta orden?");
                builder.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "http://elingeniero.com.ec/atupuerta/webservice/actualizar_estado.php?id_orden="+id_orden+"&est=P";
                                System.out.println(url);
                                jrq = new JsonObjectRequest(Request.Method.GET, url, null, Detalle_Tarea.this, Detalle_Tarea.this);
                                rq.add(jrq);

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public void onErrorResponse(VolleyError error) {

    }


    public void onResponse(JSONObject response) {
        String url2="http://elingeniero.com.ec/atupuerta/webservice/listar_ordenes.php?id_usuario="+usuario;
        System.out.println(url2);
        Intent intencion = new Intent(Detalle_Tarea.this, Tareas.class);
        intencion.putExtra(Tareas.url, url2);
        intencion.putExtra(Tareas.usu,usuario);
        startActivity(intencion);
    }
}
