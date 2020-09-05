package com.example.adminbikerental.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.adminbikerental.R;
import com.example.adminbikerental.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.adminbikerental.activity.SplashscreenActivity.ID;
import static com.example.adminbikerental.activity.SplashscreenActivity.SHARED_PREFS;

public class DetailCustomerActivity extends AppCompatActivity {
    public static final String TAG = DetailCustomerActivity.class.getSimpleName();
    private EditText edtName, edtNoktp, edtEmail, edtPhone, edtAddress;
    private Button btnUpdate;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer);

        edtName = findViewById(R.id.edt_name);
        edtNoktp = findViewById(R.id.edt_noktp);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        btnUpdate = findViewById(R.id.btn_update);

        Intent intent = getIntent();
        Customer itemData = intent.getParcelableExtra("Item Data");

        id = itemData.getId();
        edtName.setText(itemData.getName());
        edtNoktp.setText(itemData.getNoktp());
        edtEmail.setText(itemData.getEmail());
        edtPhone.setText(itemData.getPhone());
        edtAddress.setText(itemData.getAddress());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputName= edtName.getText().toString().trim();
                String inputNoktp= edtNoktp.getText().toString().trim();
                String inputEmail = edtEmail.getText().toString().trim();
                String inputPhone = edtPhone.getText().toString().trim();
                String inputAddress = edtAddress.getText().toString().trim();

                boolean isEmpty = false;

                if (inputName.isEmpty()) {
                    isEmpty = true;
                    edtName.setError("Nama lengkap harus diisi");
                }

                if (inputNoktp.isEmpty()) {
                    isEmpty = true;
                    edtNoktp.setError("No KTP harus diisi");
                }

                if (inputEmail.isEmpty()) {
                    isEmpty = true;
                    edtEmail.setError("Email harus diisi");
                }

                if (inputPhone.isEmpty()) {
                    isEmpty = true;
                    edtPhone.setError("Nomor telepon harus diisi");
                }

                if (inputAddress.isEmpty()) {
                    isEmpty = true;
                    edtAddress.setError("Alamat Number harus diisi");
                }

                if (!isEmpty) {
                    AndroidNetworking.post("http://192.168.43.21/bike_rental/update_customer.php")
                            .addBodyParameter("id", id)
                            .addBodyParameter("name", inputName)
                            .addBodyParameter("noktp", inputNoktp)
                            .addBodyParameter("email", inputEmail)
                            .addBodyParameter("phone", inputPhone)
                            .addBodyParameter("address", inputAddress)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");
                                        String message = response.getString("message");

                                        if (status.equals("success")) {
                                            Toast.makeText(DetailCustomerActivity.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(DetailCustomerActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                    Log.e(TAG, "onError: " + error.getLocalizedMessage());
                                }
                            });
                }
            }
        });
    }
}