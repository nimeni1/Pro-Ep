package com.fhict.proep.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fhict.proep.client.R;
import com.fhict.proep.client.models.Client;

public class CompletedActivity extends AppCompatActivity implements View.OnClickListener{

    private Client client;
    private TextView totalPrice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fare_completed_page);
        client = (Client) getIntent().getSerializableExtra("user");
        double price = getIntent().getDoubleExtra("price", 0);
        totalPrice = findViewById(R.id.actual_price_label);
        totalPrice.setText(Double.toString(price));
        Button backToMainMenuButton = findViewById(R.id.button_back_to_main_menu);
        backToMainMenuButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button_back_to_main_menu){
            Intent intent = new Intent(CompletedActivity.this, MainActivity.class);
            intent.putExtra("user",client);
            startActivity(intent);
        }
    }
}
