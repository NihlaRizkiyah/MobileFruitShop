package com.project.pointofsaleproject.screen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.SharedPrefManager;
import com.project.pointofsaleproject.api.ApiRest;
import com.project.pointofsaleproject.api.UtilsApi;
import com.project.pointofsaleproject.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRegisterBinding binding;
    ApiRest mApiClient;
    ProgressDialog loading;
    SharedPrefManager mSharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mSharedPrefManager = new SharedPrefManager(this);
        mApiClient = UtilsApi.getAPIService();

        binding.btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_register){
            if (TextUtils.isEmpty(binding.etUsername.getText().toString())) {
                binding.etUsername.setError("Nama pengguna harus diisi");
            } else if(TextUtils.isEmpty(binding.etPassword.getText().toString())) {
                binding.etPassword.setError("Kata sandi harus diisi");
            }
            else{
                loading = ProgressDialog.show(this, null, "Harap Tunggu...", true, false);
                register();
            }
        }else  if(view.getId() == R.id.btn_register){
            Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void register(){
        String name = binding.etName.getText().toString();
        String username = binding.etUsername.getText().toString();
        String email = binding.etEmail.getText().toString();
        String alamat = binding.etAddress.getText().toString();
        String wa = binding.etWa.getText().toString();
        String password = binding.etPassword.getText().toString();

        mApiClient.register(name, username, alamat, email, wa, password)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            JSONObject json = null;
                            String status = "", message = "Gagal";
                            try {
                                json = new JSONObject(response.body().toString());
                                message = json.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            assert json != null;
                            if(json.has("error")){
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }else {
                                Toast.makeText(RegisterActivity.this, "BERHASIL REGISTER", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                finish();
                            }

                        }
                        else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(RegisterActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

    }

}