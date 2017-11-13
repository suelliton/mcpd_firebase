package com.tads.eaj.ufrn.mcpd;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tads.eaj.ufrn.mcpd.adapters.CulturaAdapter;
import com.tads.eaj.ufrn.mcpd.adapters.PermissionUtils;
import com.tads.eaj.ufrn.mcpd.adapters.PragaAdapter;
import com.tads.eaj.ufrn.mcpd.model.Cultura;
import com.tads.eaj.ufrn.mcpd.model.Praga;
import com.tads.eaj.ufrn.mcpd.model.Registro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static com.tads.eaj.ufrn.mcpd.ConsultaRegistrosActivity.RESULT_EDIT;
import static com.tads.eaj.ufrn.mcpd.ConsultaRegistrosActivity.RESULT_EXIT;


public class RegistroActivity extends AppCompatActivity {
    private FirebaseDatabase database ;
    private DatabaseReference culturaReference;
    private DatabaseReference pragaReference;
    private ChildEventListener childEventCultura;
    private ChildEventListener childEventPraga;

    CulturaAdapter adaptador_cultura;
    PragaAdapter adaptador_praga;
    Registro registroAtual;
    private EditText tratamento;
    private RadioGroup radioGroup_escala,radioGroup_tratamento;
    private final static int REQUEST_CONSULT = 1;
    private Button btnConsulta, btn_Gravar, btn_Sair, btn_Apontamentos, btn_Cancelar;
    private Spinner spinner_cultura, spinner_praga;
    private int ID_EDIT;
    private Long ID_PROPRIEDADE;
    private int contGps=0;
    GPSTracker  gps = new GPSTracker(RegistroActivity.this);
    final String[] permissoes = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        recebePropriedade();
        database = FirebaseDatabase.getInstance();
        culturaReference = database.getReference().child("Cultura");
        pragaReference = database.getReference().child("Praga");
        registroAtual = new Registro();

        bindViews();
        setListeners();
        //controlaBotoes(false);

    }

    public void recebePropriedade() {
        Bundle bundle = getIntent().getExtras();
        ID_PROPRIEDADE = bundle.getLong("idPropriedade");
        Toast.makeText(this, "id propriedade = " + ID_PROPRIEDADE, Toast.LENGTH_SHORT).show();
    }


    public void bindViews() {
        btnConsulta = (Button) findViewById(R.id.btn_consultar);
        btn_Gravar = (Button) findViewById(R.id.btn_gravar);
        btn_Apontamentos = (Button) findViewById(R.id.btn_apontamentos);
        btn_Sair = (Button) findViewById(R.id.btn_sair);
        btn_Cancelar = (Button) findViewById(R.id.btn_cancelar);
        spinner_cultura = (Spinner) findViewById(R.id.spinner_cultura);
        spinner_praga = (Spinner) findViewById(R.id.spinner_praga_dano);
        radioGroup_escala = (RadioGroup) findViewById(R.id.radioGroup_escala);
        radioGroup_tratamento= (RadioGroup) findViewById(R.id.radioGroup_tratamento);
        tratamento = (EditText) findViewById(R.id.tratamento);

    }

    public void setListeners() {

        final List<Cultura> listaCulturas= new ArrayList<>();
        //joga as culturas pro adapter
         adaptador_cultura = new CulturaAdapter(this, listaCulturas);
        //seta o adapter no spinner
        spinner_cultura.setAdapter(adaptador_cultura);
        //recupera todas as culturas do banco
         childEventCultura = culturaReference.child("nome").orderByChild("nome").addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             Cultura cultura =  dataSnapshot.getValue(Cultura.class);
                 listaCulturas.add(cultura);
                 adaptador_cultura.notifyDataSetChanged();
                 //Log.i("firebase",cultura.toString());
             }

             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {

             }

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
        culturaReference.addChildEventListener(childEventCultura);


        final List<Praga> listaPragas= new ArrayList<>();
        //joga as culturas pro adapter
        adaptador_praga = new PragaAdapter(this, listaPragas);
        //seta o adapter no spinner
        spinner_praga.setAdapter(adaptador_praga);

        //trata o clique na cultura do spinner
        spinner_cultura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                childEventPraga =  pragaReference.orderByChild("nome").addChildEventListener(new ChildEventListener(){
                     @Override
                     public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                         Praga praga =  dataSnapshot.getValue(Praga.class);
                         Cultura culturaClicada = (Cultura) adaptador_cultura.getItem(i);//cultura clicada
                         listaPragas.add(praga);
                         adaptador_praga.notifyDataSetChanged();
                     }

                     @Override
                     public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                     }

                     @Override
                     public void onChildRemoved(DataSnapshot dataSnapshot) {

                     }

                     @Override
                     public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
                pragaReference.addChildEventListener(childEventPraga);


                spinner_praga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        registroAtual.setPragaId(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }) ;



        //radiobuttons tratamento
        RadioButton radio_tratamento_sim = (RadioButton) findViewById(R.id.registro_sim);
        radio_tratamento_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratamento.setVisibility(View.VISIBLE);
                registroAtual.setTratamento(true);
            }
        });
        RadioButton radio_tratamento_nao = (RadioButton) findViewById(R.id.registro_nao);
        radio_tratamento_nao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tratamento.setVisibility(View.GONE);
                registroAtual.setTratamento(false);
            }
        });
        //define estado inicial edit escondido e botao nao setado
        registroAtual.setTratamento(false);
        tratamento.setVisibility(View.GONE);
        radio_tratamento_nao.toggle();


        //radiobuttons escala

        //define estado padrao inicial
        RadioButton radio_3 = (RadioButton) findViewById(R.id.sev_03);
        radio_3.toggle();
        //registroAtual.setEscala(3);

        switch (radioGroup_escala.getCheckedRadioButtonId()){

            case R.id.sev_01:
                    registroAtual.setEscala(1);
                break;
            case R.id.sev_02:
                registroAtual.setEscala(2);
                break;
            case R.id.sev_03:
                registroAtual.setEscala(3);
                break;
            case R.id.sev_04:
                registroAtual.setEscala(4);
                break;
            case R.id.sev_05:
                registroAtual.setEscala(5);
                break;
            default:
        }












        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
               // bundle.putCharSequenceArrayList("list", Nomes);
                Intent intent = new Intent(RegistroActivity.this, ConsultaRegistrosActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CONSULT);
            }
        });

        btn_Gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarRegistro();
            }
        });
        btn_Apontamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aqui pode-se pegar todos os elementos da tela e apagar tudo ou nao fazer nada
            }
        });
        btn_Sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegistroActivity.this, "Operação cancelada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistroActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void controlaBotoes(boolean estado) {
        Button btn_excluir = (Button) findViewById(R.id.btn_excluir);
        if (!estado) {
            btn_excluir.setVisibility(View.GONE);
        } else {
            btn_excluir.setVisibility(View.VISIBLE);
        }
    }

    public void setupGps(){
        gps.getLocation();
        if(!gps.isGPSEnabled){
            if(contGps == 2){
                finish();
            }else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Toast.makeText(RegistroActivity.this, "Ative o GPS do dispositivo", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                contGps ++;
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result : grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                alertAndFinish();
                return;
            }
        }
    }
    private void alertAndFinish(){
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.");
            // Add the buttons
            builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    /*public void editaRegistro(int id_edit){
        List<Registro> listaRegistros = bancoHelper.findAllRegistros();
        registroAtual = listaRegistros.get(id_edit);
        tratamento.setText(registroAtual.getTipo());
        RadioButton radio;
        Log.i("escala",registroAtual.getEscala()+"");

        switch (registroAtual.getEscala()){

            case 1:
                radio = (RadioButton) findViewById(R.id.sev_01);
                radio.toggle();
                break;
            case 2:
                radio = (RadioButton) findViewById(R.id.sev_02);
                radio.toggle();
                break;
            case 3:
                radio = (RadioButton) findViewById(R.id.sev_03);
                radio.toggle();
                break;
            case 4:
                radio = (RadioButton) findViewById(R.id.sev_04);
                radio.toggle();
                break;
            case 5:
                radio = (RadioButton) findViewById(R.id.sev_05);
                radio.toggle();
                break;
            default:
                Toast.makeText(this, "Escala inexistente", Toast.LENGTH_SHORT).show();
        }




    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONSULT) {
            if (resultCode == RESULT_EDIT) {
                Bundle bundle = data.getExtras();
                int id_edit = bundle.getInt("id_edit",0);
                Log.i("id_edit",id_edit+"");
               // editaRegistro(id_edit);
            } else if (resultCode == RESULT_EXIT) {
                finish();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(RegistroActivity.this, "Operação cancelada", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtils.validate(this, 0, permissoes);
        //setupGps();
    }
    public String[] converteCultura(List<Cultura> lista){
        String[] listaConvertida = new String[lista.size()];
        int i=0;
        for (Cultura c: lista){
            listaConvertida[i] = c.getNome();
            i++;
        }

       return listaConvertida;
    }
    public String[] convertePraga(List<Praga> lista){
        String[] listaConvertida = new String[lista.size()];
        int i=0;
        for (Praga p: lista){
            listaConvertida[i] = p.getNome();
            i++;
        }

        return listaConvertida;
    }

   public String getDataAtual(){
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-\n HH:mm:ss");
       // OU
       SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

       Date data = new Date();

       Calendar cal = Calendar.getInstance();
       cal.setTime(data);
       Date data_atual = cal.getTime();

       String data_completa = dateFormat.format(data_atual);

       String hora_atual = dateFormat_hora.format(data_atual);

       Log.i("data_completa", data_completa);
       Log.i("data_atual", data_atual.toString());
       Log.i("hora_atual", hora_atual); // Esse é o que você quer

       return data_completa;
   }
    public double getLatitude(){
        GPSTracker gps = new GPSTracker(this);
        return gps.getLatitude();
    }
    public double getLongitude(){
        GPSTracker gps = new GPSTracker(this);
        Log.i("latitude",gps.getLatitude()+"");
        return gps.getLongitude();
    }


   public void  salvarRegistro(){


       EditText observacoes = (EditText) findViewById(R.id.observacoes);
       registroAtual.setObs(observacoes.getText().toString());

       EditText tratamento = (EditText) findViewById(R.id.tratamento);
       registroAtual.setTipo(tratamento.getText().toString());


       registroAtual.setDataRegistro(getDataAtual());
       registroAtual.setDataTratamento(getDataAtual());



       registroAtual.setLatitude(getLatitude());
       registroAtual.setLongitude(getLongitude());

       registroAtual.setUsuarioId(1);

       Toast.makeText(this, "Dados salvos :"+registroAtual.toString(),  Toast.LENGTH_SHORT).show();
   }

}
