package com.project.pointofsaleproject.screen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.SharedPrefManager;
import com.project.pointofsaleproject.api.ApiRest;
import com.project.pointofsaleproject.api.UtilsApi;
import com.project.pointofsaleproject.databinding.ActivityProdukCheckoutBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ProdukCheckoutActivity extends AppCompatActivity {
    ActivityProdukCheckoutBinding binding;
    String id = "";
    String qty = "";
    String nama;
    String harga;
    String satuan;
    Context context;
    String pembelian = "";
    ProgressDialog loading;
    SharedPrefManager sharedPrefManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProdukCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ProdukCheckoutActivity.this;
        id = getIntent().getStringExtra("id");
        qty = getIntent().getStringExtra("qty");
        nama = getIntent().getStringExtra("nama");
        harga = getIntent().getStringExtra("harga");
        satuan = getIntent().getStringExtra("satuan");

        binding.tvName.setText(nama);
        binding.tvQty.setText(qty + " " + satuan);
        binding.tvTotal.setText("Rp " + (Integer.parseInt(qty) * Integer.parseInt(harga)));

        sharedPrefManager = SharedPrefManager.getInstance(this);

        binding.btnCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnTakeaway.setBackgroundColor(getResources().getColor(R.color.grey_500));
                binding.btnCod.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                pembelian = "COD";
            }
        });

        binding.btnTakeaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnTakeaway.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.btnCod.setBackgroundColor(getResources().getColor(R.color.grey_500));
                pembelian = "Takeaway";
            }
        });

        binding.btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(pembelian, "")) {
                    Toast.makeText(context, "Pilih metode pembelian", Toast.LENGTH_SHORT).show();
                } else {
                    saveData();
                }
            }
        });

    }

    private void saveData() {
        loading = ProgressDialog.show(context, null, "Harap Tunggu...", true, false);
        String idUser = sharedPrefManager.getAkun(SharedPrefManager.TAG_ID);

        ApiRest mApiRest = UtilsApi.getAPIService();
        mApiRest.saveOrder(idUser, id, String.valueOf(Integer.parseInt(qty) * Integer.parseInt(harga)), qty, pembelian).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JSONObject json = null;
                    String status = "", message = "", data = "";
                    try {
                        json = new JSONObject(response.body().toString());
                        status = json.getString("status");
                        message = json.getString("message");
                        data = json.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (response.body() == null) {
                        Toast.makeText(context, "Gagal Upload Data", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("error")) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("success")) {
                        Toast.makeText(context, "Berhasil Membuat Orderan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Silahkan Periksa Data Input", Toast.LENGTH_SHORT).show();
                    }
                    loading.dismiss();
                } else {
                    loading.dismiss();
                    Toast.makeText(context, "Gagal Upload Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, Throwable t) {
                loading.dismiss();
                Log.d("messageerror", t.getMessage());
                Toast.makeText(context, "Koneksi Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}