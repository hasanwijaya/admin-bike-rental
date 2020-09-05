package com.example.adminbikerental.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.adminbikerental.R;
import com.example.adminbikerental.adapter.CustomerAdapter;
import com.example.adminbikerental.model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.adminbikerental.activity.SplashscreenActivity.ID;
import static com.example.adminbikerental.activity.SplashscreenActivity.SHARED_PREFS;

public class CustomersActivity extends AppCompatActivity {
    private ArrayList<Customer> list = new ArrayList<>();
    private RecyclerView rvCustomer;
    private CustomerAdapter customerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);


        rvCustomer = findViewById(R.id.rv_customers);
        rvCustomer.setHasFixedSize(true);

        getCustomers();
        showRecycler();
    }

    private void getCustomers() {
        AndroidNetworking.get("http://192.168.43.21/bike_rental/list_customer.php")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                JSONArray result = response.getJSONArray("result");

                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);

                                    Customer customer = new Customer();
                                    customer.setId(item.getString("id"));
                                    customer.setName(item.getString("name"));
                                    customer.setPhone(item.getString("phone"));
                                    customer.setNoktp(item.getString("no_ktp"));
                                    customer.setEmail(item.getString("email"));
                                    customer.setAddress(item.getString("address"));
                                    list.add(customer);
                                }

                                customerAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void showRecycler() {
        rvCustomer.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter(list);
        rvCustomer.setAdapter(customerAdapter);

        customerAdapter.setOnItemClickCallback(new CustomerAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Customer data) {
                showSelectedCustomer(data);
            }
        });
    }

    private void showSelectedCustomer(Customer customer) {
        Intent intent = new Intent(this, DetailCustomerActivity.class);
        intent.putExtra("Item Data", customer);
        startActivity(intent);
    }
}