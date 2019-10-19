package com.fhict.proep.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fhict.proep.client.R;
import com.fhict.proep.client.models.Client;
import com.fhict.proep.client.retrofit.APIService;
import com.fhict.proep.client.retrofit.APIUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cioata on 16/11/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private APIService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Button btn = findViewById(R.id.button_login_l);
        btn.setOnClickListener(this);
        btn = findViewById(R.id.button_register_l);
        btn.setOnClickListener(this);
        service = APIUtils.getAPIService();
    }

    private void logIn(String email, String pass) {
        service.logIn(email,pass).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(@NonNull Call<Client> call, @NonNull Response<Client> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Client client = response.body();
                    intent.putExtra("user",client);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Client> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Service not reachable",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_register_l){
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.button_login_l) {

//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//            Toast.makeText(getApplicationContext(),"You are logged in!",Toast.LENGTH_SHORT).show();
            String email = ((EditText)findViewById(R.id.email_field_l)).getText().toString();
            String pass = ((EditText)findViewById(R.id.password_field_l)).getText().toString();
            logIn(email,pass);
        }
    }
}
