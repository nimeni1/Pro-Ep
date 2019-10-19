package com.fhict.proep.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fhict.proep.client.R;
import com.fhict.proep.client.models.Address;
import com.fhict.proep.client.models.Client;
import com.fhict.proep.client.models.Name;
import com.fhict.proep.client.retrofit.APIService;
import com.fhict.proep.client.retrofit.APIUtils;

import okhttp3.ResponseBody;
import okhttp3.internal.http.StatusLine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cioata on 16/11/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    private APIService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        service = APIUtils.getAPIService();
        Button btn = findViewById(R.id.buttor_register_r);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client client = createClient();
                registerClient(client);
            }
        });
    }

    private Client createClient() {
        try {
            String firstName = ((EditText)findViewById(R.id.field_fname_r)).getText().toString();
            String lastName = ((EditText)findViewById(R.id.field_lname_r)).getText().toString();
           // String city = ((EditText)findViewById(R.id.field_city_r)).getText().toString();
            String email = ((EditText)findViewById(R.id.field_email_r)).getText().toString();
           // String house = ((EditText)findViewById(R.id.field_housenr_r)).getText().toString();
            String password = ((EditText)findViewById(R.id.field_password_r)).getText().toString();
            String phone = ((EditText)findViewById(R.id.field_phone_r)).getText().toString();
           // String postCode = ((EditText)findViewById(R.id.field_postcode_r)).getText().toString();
           // String street = ((EditText)findViewById(R.id.field_street_r)).getText().toString();
            return new Client(new Name(firstName,lastName),email,password,phone);
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
    }

    private void registerClient(final Client client) {
        service.register(client).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(),"Register successful!",Toast.LENGTH_SHORT).show();
                    Intent main = new Intent(getApplicationContext(),MainActivity.class);
                    main.putExtra("user",client);
                    startActivity(main);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to Register!",Toast.LENGTH_LONG).show();
            }
        });
    }
}
