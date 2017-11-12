package com.tads.eaj.ufrn.mcpd;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button mBtn_login;
    EditText ed_login;
    EditText ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();
        setListeners();
    }

    private void bindViews() {
        mBtn_login = (Button) findViewById(R.id.btn_login);
        ed_login = (EditText) findViewById(R.id.ed_login);
        ed_password = (EditText) findViewById(R.id.ed_password);
    }

    private void setListeners() {
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = ed_login.getText().toString();
                String password = ed_password.getText().toString();

                //if(login.equals("admin") && password.equals("admin")){
                    Intent intent = new Intent(LoginActivity.this,RegistroActivity.class );;
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

              //  }else if(!login.equals("admin")){
                    Snackbar.make((View)v.getParent(), "Login inválido", Snackbar.LENGTH_SHORT).show();
              //  }else if(!password.equals("admin")){
                    Snackbar.make((View)v.getParent(), "Senha inválida", Snackbar.LENGTH_SHORT).show();
             //   }*/


            }
        });
    }
}
