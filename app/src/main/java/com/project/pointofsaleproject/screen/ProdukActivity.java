package com.project.pointofsaleproject.screen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.adapter.GridSpacingItemDecoration;
import com.project.pointofsaleproject.adapter.RecyclerAdapterProduk;
import com.project.pointofsaleproject.api.ApiRest;
import com.project.pointofsaleproject.api.UtilsApi;
import com.project.pointofsaleproject.model.Produk;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btnBack;
    RecyclerView recyclerView;
    RecyclerAdapterProduk recyclerAdapter;
    ProgressDialog loading;
    Context context;
    EditText etSearch;
    TextView tvTitle;
    ImageView imgvNotFound;
    List<Produk> dataList;
    ApiRest mApiClient;
    String id_kategori = "0";
    String nama_kategori = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);

        id_kategori = getIntent().getStringExtra("id_kategori");
        nama_kategori = getIntent().getStringExtra("nama_kategori");

        context = ProdukActivity.this;
        etSearch = findViewById(R.id.et_search);
        recyclerView = findViewById(R.id.rv_data);
        imgvNotFound = findViewById(R.id.imgv_not_found);
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText(nama_kategori);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    recyclerAdapter.getFilter().filter(v.getText(),new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int i) {
                            if(recyclerAdapter.getItemCount() == 0){
                                imgvNotFound.setVisibility(View.VISIBLE);
                            }else {
                                imgvNotFound.setVisibility(View.GONE);
                            }
                        }
                    });
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        dataList = new ArrayList<>();
        getData();

        int spacingInPixels = 50;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, false, 0));

        btnBack.setOnClickListener(this);

    }

    public void initData(){
        recyclerAdapter = new RecyclerAdapterProduk(dataList, context);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerAdapter.setOnclickCallback(new RecyclerAdapterProduk.OnItemClickCallback() {
            @Override
            public void onItemClick(Produk item, int position) {
                Intent intent = new Intent(context, ProdukDetailActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("image", item.getPhoto());
                intent.putExtra("nama", item.getNama());
                intent.putExtra("stok", item.getStok());
                intent.putExtra("harga", item.getHarga());
                intent.putExtra("satuan", item.getSatuan());
                startActivity(intent);
            }

        });
    }

    public void getData(){
        loading = ProgressDialog.show(context, null, "Harap Tunggu...", true, false);
        dataList.clear();
        mApiClient = UtilsApi.getAPIService();
        mApiClient.getProduk(id_kategori.toString())
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if(response.isSuccessful()){
                            if(response.body() == null){
                                Toast.makeText(context, "Data Tidak Tersedia", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            } else {
                                for(int i=0;i<response.body().size();i++) {
                                    String mJsonString = response.body().get(i).toString();
                                    JsonParser parser = new JsonParser();
                                    JsonElement mJson =  parser.parse(mJsonString);
                                    Gson gson = new Gson();
                                    Produk object = gson.fromJson(mJson, Produk.class);
                                    dataList.add(object);
                                }
                                initData();
                                if(dataList.size() == 0){
                                    imgvNotFound.setVisibility(View.VISIBLE);
                                }else {
                                    imgvNotFound.setVisibility(View.GONE);
                                }
                                loading.dismiss();
                            }
                        }
                        else {
                            loading.dismiss();
                            Toast.makeText(context, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.i("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(context, "Keneksi Error", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back){
            finish();
        }
    }
}