package com.tads.eaj.ufrn.mcpd.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tads.eaj.ufrn.mcpd.R;
import com.tads.eaj.ufrn.mcpd.model.Cultura;
import com.tads.eaj.ufrn.mcpd.model.Praga;
import com.tads.eaj.ufrn.mcpd.model.Registro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.tads.eaj.ufrn.mcpd.RegistroActivity.listaCulturas;
import static com.tads.eaj.ufrn.mcpd.RegistroActivity.listaPragas;

/**
 * Created by suelitton on 14/10/2017.
 * Adapted by Jofrey on 17/07/2017.
 */

public class RegistroAdapter extends RecyclerView.Adapter {

    Context context;
    List<Registro> listaRegistros;

    public RegistroAdapter(Context c, List<Registro> listaRegistros){
        this.context = c;
        this.listaRegistros = listaRegistros;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.inflate_registros, parent, false);

        RegistroViewHolder holder = new RegistroViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RegistroViewHolder registroholder = (RegistroViewHolder) holder;
        Registro registroEscolhido = listaRegistros.get(position);

        //objetos apenas para testes esses valores vao ser pegados diretamente pelo objeto registro quando tudo estiver no banco

        Cultura c1 = listaCulturas.get(registroEscolhido.getCulturaId())  ;
        Praga p1 = listaPragas.get(registroEscolhido.getPragaId());
        if(position != 0){
            registroholder.label_esc.setVisibility(View.INVISIBLE);
            registroholder.label_dataHora.setVisibility(View.INVISIBLE);
            registroholder.label_cultura.setVisibility(View.INVISIBLE);
            registroholder.label_praga.setVisibility(View.INVISIBLE);
           // registroholder.label_infestacaoTratamento.setVisibility(View.INVISIBLE);
        }

        registroholder.text_esc.setText(""+registroEscolhido.getEscala());
        registroholder.text_dataHora.setText(""+registroEscolhido.getDataRegistro());
        registroholder.text_cultura.setText(""+c1.getNome());
        registroholder.text_praga.setText(""+p1.getNome());
        String tratamento;
        if(registroEscolhido.isTratamento()){
            tratamento = "Sim";
        }else{
            tratamento = "Não";
        }
       // registroholder.text_infestacaoTratamento.setText(""+tratamento+"-"+registroEscolhido.getTipo());


            registroholder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //TODO: A IMPLEMENTAR QUANDO FOR PRECISO
                }
            });


    }


    @Override
    public int getItemCount() {
        return  listaRegistros == null ? 0 :  listaRegistros.size();
    }

    public class RegistroViewHolder extends RecyclerView.ViewHolder {

        final TextView text_esc;
        final TextView text_dataHora;
        final TextView text_cultura;
        final TextView text_praga;
       // final TextView text_infestacaoTratamento;
        final LinearLayout row;

        final TextView label_esc;
        final TextView label_dataHora;
        final TextView label_cultura;
        final TextView label_praga;
      //  final TextView label_infestacaoTratamento;

        public RegistroViewHolder(View v) {
            super(v);
            text_esc = v.findViewById(R.id.reg_escala);
            text_dataHora = v.findViewById(R.id.reg_hr);
            text_cultura = v.findViewById(R.id.reg_cult);
            text_praga = v.findViewById(R.id.reg_praga);
           // text_infestacaoTratamento = v.findViewById(R.id.reg_inf_trat);

            label_esc = v.findViewById(R.id.label_escala);
            label_dataHora = v.findViewById(R.id.label_horaData);
            label_cultura = v.findViewById(R.id.label_cultura);
            label_praga = v.findViewById(R.id.label_praga);
            //label_infestacaoTratamento = v.findViewById(R.id.label_tratamento);

            row = v.findViewById(R.id.reg_row);



        }
    }

}