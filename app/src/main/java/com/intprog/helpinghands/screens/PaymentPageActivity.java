package com.intprog.helpinghands.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.intprog.helpinghands.HomePageActivity;
import com.intprog.helpinghands.ProfilePageActivity;
import com.intprog.helpinghands.R;
import com.intprog.helpinghands.screens.DonationCampaign.DonationOptionPageActivity;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class PaymentPageActivity extends AppCompatActivity {

    Button stripeButton;
    EditText amountEditText;

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret, amount;
    PaymentSheet.CustomerConfiguration customerConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        stripeButton = findViewById(R.id.stripeButton);
        amountEditText = findViewById(R.id.amountEditText);

        stripeButton.setOnClickListener(view -> {
            if (TextUtils.isEmpty(amountEditText.getText()
                    .toString())){
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                amount = amountEditText.getText().toString() + "00";
                getDetails();
            }
        });

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

    }

    void getDetails(){
        Fuel.INSTANCE.post("https://us-central1-helpinghands-96b47.cloudfunctions.net/helloWorld?amt=" + amount, null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showStripePaymentSheet();
                        }
                    });



                } catch (JSONException e){
                    Toast.makeText(PaymentPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) {

            }
        });
    }

    void showStripePaymentSheet(){
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("HelpingHands")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );

    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult){

        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, ((PaymentSheetResult.Failed) paymentSheetResult).getError().toString(), Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        }

        ImageButton backTop = findViewById(R.id.backTop);
        backTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentPageActivity.this, DonationOptionPageActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        ImageButton homeImageButton = findViewById(R.id.homeImageButton);
        homeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentPageActivity.this, HomePageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        ImageButton profileImageButton = findViewById(R.id.profileImageButton);
        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentPageActivity.this, ProfilePageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }
}