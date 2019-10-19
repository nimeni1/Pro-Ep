package proep.fhict.work.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import proep.fhict.work.driver.R;
import proep.fhict.work.driver.activity.MainActivity;
import proep.fhict.work.driver.model.Driver;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button btn = findViewById(R.id.button_confirm);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_confirm) {
            Driver driver = (Driver) getIntent().getSerializableExtra("driver");
            Intent intent = new Intent(this,FareActivity.class);
            intent.putExtra("driver",driver);
            startActivity(intent);
        }
    }
}
