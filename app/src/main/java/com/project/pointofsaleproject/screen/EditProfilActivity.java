package com.project.pointofsaleproject.screen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.SharedPrefManager;
import com.project.pointofsaleproject.api.ApiRest;
import com.project.pointofsaleproject.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class EditProfilActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btnEdit, btnVisibility, btnBack;
    Button btnLogout;
    EditText etUsername, etNama, etPassword, etNomor, etEmail, etAlamat;
    Boolean editStatus = false;
    Boolean showPassword = false;
    Button btnUpdate;
    Context context;
    ProgressDialog loading;
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        context = EditProfilActivity.this;
        btnEdit = findViewById(R.id.btn_edit);
        btnLogout = findViewById(R.id.btn_logout);
        btnVisibility = findViewById(R.id.btn_visibility);
        etUsername = findViewById(R.id.et_username);
        etNama = findViewById(R.id.et_nama);
        etEmail = findViewById(R.id.et_email);
        etAlamat = findViewById(R.id.et_alamat);
        etPassword = findViewById(R.id.et_password);
        etNomor = findViewById(R.id.et_nomor);
        btnUpdate = findViewById(R.id.btn_update);
        btnBack = findViewById(R.id.btn_back);

        btnEdit.setOnClickListener(this);
        btnVisibility.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        id_user = SharedPrefManager.getInstance(context).getAkun(SharedPrefManager.TAG_ID);
        etUsername.setText(SharedPrefManager.getInstance(context).getAkun(SharedPrefManager.TAG_USERNAME));
        etNama.setText(SharedPrefManager.getInstance(context).getAkun(SharedPrefManager.TAG_NAMA));
        etNomor.setText(SharedPrefManager.getInstance(context).getAkun(SharedPrefManager.TAG_NOMOR));
        etEmail.setText(SharedPrefManager.getInstance(context).getAkun(SharedPrefManager.TAG_EMAIL));
        etAlamat.setText(SharedPrefManager.getInstance(context).getAkun(SharedPrefManager.TAG_ALAMAT));
    }

    private void saveData() {
        loading = ProgressDialog.show(context, null, "Harap Tunggu...", true, false);

        String username = etUsername.getText().toString();
        String nomor = etNomor.getText().toString();
        String password = etPassword.getText().toString();
        String nama = etNama.getText().toString();
        String alamat = etAlamat.getText().toString();
        String email = etEmail.getText().toString();
        if(username.length() == 0){
            Toast.makeText(EditProfilActivity.this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        if(nama.length() == 0){
            Toast.makeText(EditProfilActivity.this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        if(nomor.length() == 0){
            Toast.makeText(EditProfilActivity.this, "Nomor tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        if(email.length() == 0){
            Toast.makeText(EditProfilActivity.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        if(password.length() < 5 && password.length() > 0){
            Toast.makeText(EditProfilActivity.this, "Password Minimal 5 Karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiRest mApiRest = UtilsApi.getAPIService();
        mApiRest.updateProfil(id_user, username, nama, alamat, email, nomor, password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if(response.isSuccessful()) {
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
                        Toast.makeText(context, "Berhasil Update Profil", Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(context).setAkun(SharedPrefManager.TAG_USERNAME, username);
                        SharedPrefManager.getInstance(context).setAkun(SharedPrefManager.TAG_NOMOR, nomor);
                        SharedPrefManager.getInstance(context).setAkun(SharedPrefManager.TAG_EMAIL, email);
                        SharedPrefManager.getInstance(context).setAkun(SharedPrefManager.TAG_NAMA, nama);
                        SharedPrefManager.getInstance(context).setAkun(SharedPrefManager.TAG_ALAMAT, alamat);
                    } else {
                        Toast.makeText(context, "Silahkan Periksa Data Input", Toast.LENGTH_SHORT).show();
                    }
                    loading.dismiss();
                }else {
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


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_edit) {
            if (!editStatus) {
                btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear));
                editStatus = true;
                btnUpdate.setVisibility(View.VISIBLE);
                etUsername.setEnabled(true);
                etPassword.setEnabled(true);
                etNama.setEnabled(true);
                etAlamat.setEnabled(true);
                etEmail.setEnabled(true);
                etNomor.setEnabled(true);
            } else {
                btnEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
                editStatus = false;
                btnUpdate.setVisibility(View.GONE);
                etUsername.setEnabled(false);
                etPassword.setEnabled(false);
                etNama.setEnabled(false);
                etAlamat.setEnabled(false);
                etEmail.setEnabled(false);
                etNomor.setEnabled(false);
            }
        } else if (view.getId() == R.id.btn_visibility) {
            if (showPassword) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnVisibility.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off));
                showPassword = false;
            } else {
                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnVisibility.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility));
                showPassword = true;
            }
        } else if (view.getId() == R.id.btn_update) {
            saveData();
        } else if (view.getId() == R.id.btn_back) {
            finish();
        }else if(view.getId() == R.id.btn_logout){
            SharedPrefManager.getInstance(context).reset();
            Intent intent = new Intent(EditProfilActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}