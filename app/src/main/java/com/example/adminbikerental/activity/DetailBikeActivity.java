package com.example.adminbikerental.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.example.adminbikerental.R;
import com.example.adminbikerental.model.Bike;
import com.example.adminbikerental.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.example.adminbikerental.activity.LoginActivity.BASE_URL;

public class DetailBikeActivity extends AppCompatActivity {
    public static final String TAG = DetailBikeActivity.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private EditText edtColor, edtMerk, edtPrice, edtCode;
    private Button btnChooseImage, btnUpdate;
    private ImageView ivPreview, ivBike;
    private Uri imageUri;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bike);

        this.setTitle("Detail Bike");

        btnChooseImage = findViewById(R.id.btn_choose_image);
        btnUpdate = findViewById(R.id.btn_update);
        ivPreview = findViewById(R.id.iv_preview);
        edtColor = findViewById(R.id.edt_color);
        edtMerk = findViewById(R.id.edt_merk);
        edtPrice = findViewById(R.id.edt_price);
        edtCode = findViewById(R.id.edt_code);
        ivBike = findViewById(R.id.iv_bike);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bike itemData = intent.getParcelableExtra("Item Data");
        id = itemData.getId();
        edtColor.setText(itemData.getColor());
        edtMerk.setText(itemData.getMerk());
        edtPrice.setText(itemData.getPrice());
        edtCode.setText(itemData.getCode());

        Glide.with(this).load(itemData.getImage()).into(ivBike);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String color = edtColor.getText().toString().trim();
                String merk = edtMerk.getText().toString().trim();
                String price = edtPrice.getText().toString().trim();
                String code = edtCode.getText().toString().trim();

                boolean isEmpty = false;

                if (color.isEmpty()) {
                    isEmpty = true;
                    edtColor.setError("Warna harus diisi");
                }

                if (merk.isEmpty()) {
                    isEmpty = true;
                    edtMerk.setError("Merek harus diisi");
                }

                if (price.isEmpty()) {
                    isEmpty = true;
                    edtPrice.setError("Harga harus diisi");
                }

                if (code.isEmpty()) {
                    isEmpty = true;
                    edtCode.setError("Kode harus diisi");
                }

                if (!isEmpty && imageUri != null) {
                    File file = new File(getPath(imageUri));

                    AndroidNetworking.upload(BASE_URL + "/update_bike.php")
                            .addMultipartFile("image", file)
                            .addMultipartParameter("id", id)
                            .addMultipartParameter("color", color)
                            .addMultipartParameter("merk", merk)
                            .addMultipartParameter("price", price)
                            .addMultipartParameter("code", code)
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long bytesUploaded, long totalBytes) {
                                    // do anything with progress
                                }
                            })
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");

                                        if(status.equals("success")) {
                                            Intent intent = new Intent(DetailBikeActivity.this, BikesActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    } catch(JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.e(TAG, "onError: " + anError.getLocalizedMessage());
                                }
                            });
                } else {
                    AndroidNetworking.upload(BASE_URL + "/update_bike.php")
                            .addMultipartParameter("id", id)
                            .addMultipartParameter("color", color)
                            .addMultipartParameter("merk", merk)
                            .addMultipartParameter("price", price)
                            .addMultipartParameter("code", code)
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener(new UploadProgressListener() {
                                @Override
                                public void onProgress(long bytesUploaded, long totalBytes) {
                                    // do anything with progress
                                }
                            })
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");

                                        if(status.equals("success")) {
                                            Intent intent = new Intent(DetailBikeActivity.this, BikesActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    } catch(JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.e(TAG, "onError: " + anError.getLocalizedMessage());
                                }
                            });
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Log.d(TAG, "onDebug: " + getPath(imageUri));
            Glide.with(this).load(imageUri).into(ivPreview);
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}