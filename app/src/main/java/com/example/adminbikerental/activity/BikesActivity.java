package com.example.adminbikerental.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.adminbikerental.R;
import com.example.adminbikerental.adapter.BikeAdapter;
import com.example.adminbikerental.adapter.CustomerAdapter;
import com.example.adminbikerental.model.Bike;
import com.example.adminbikerental.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.adminbikerental.activity.LoginActivity.BASE_URL;

public class BikesActivity extends AppCompatActivity {
    public static final String TAG = BikesActivity.class.getSimpleName();
    private ArrayList<Bike> list = new ArrayList<>();
    private RecyclerView rvBikes;
    public BikeAdapter bikeAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikes);

        this.setTitle("List Bike");

        rvBikes = findViewById(R.id.rv_bikes);
        fab = findViewById(R.id.fab);

        rvBikes.setHasFixedSize(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BikesActivity.this, AddBikeActivity.class);
                startActivity(intent);
            }
        });

        getBikes();
        showRecycler();
    }

    private void getBikes() {
        AndroidNetworking.get(BASE_URL + "/list_bikes.php")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                list.clear();
                                JSONArray result = response.getJSONArray("result");

                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject item = result.getJSONObject(i);

                                    Bike bike = new Bike();
                                    bike.setId(item.getString("id"));
                                    bike.setColor(item.getString("color"));
                                    bike.setMerk(item.getString("merk"));
                                    bike.setPrice(item.getString("price"));
                                    bike.setCode(item.getString("code"));
                                    bike.setImage(BASE_URL + "/upload/" + item.getString("image"));
                                    list.add(bike);
                                }

                                bikeAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getLocalizedMessage());
                    }
                });
    }

    private void showRecycler() {
        rvBikes.setLayoutManager(new LinearLayoutManager(this));
        bikeAdapter = new BikeAdapter(list);
        rvBikes.setAdapter(bikeAdapter);

        bikeAdapter.setOnItemClickCallback(new BikeAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Bike data) {
                showSelectedBike(data);
            }
        });
    }

    private void showSelectedBike(Bike bike) {
        Intent intent = new Intent(this, DetailCustomerActivity.class);
//        intent.putExtra("Item Data", bike);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBikes();
    }
}