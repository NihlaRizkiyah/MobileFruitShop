package com.project.pointofsaleproject.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.adapter.RecyclerAdapterKategori;
import com.project.pointofsaleproject.api.ApiRest;
import com.project.pointofsaleproject.api.UtilsApi;
import com.project.pointofsaleproject.model.Kategori;
import com.project.pointofsaleproject.screen.ProdukActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeDashboardFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerAdapterKategori recyclerAdapter;
    ProgressDialog loading;
    Context context;
    EditText etSearch;
    ImageView imgvNotFound;
    List<Kategori> dataList;
    ApiRest mApiClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_dashboard, container, false);
        context = getContext();
        etSearch = root.findViewById(R.id.et_search);
        recyclerView = root.findViewById(R.id.rv_data);
        imgvNotFound = root.findViewById(R.id.imgv_not_found);

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
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        dataList = new ArrayList<>();

        getData();
        return root;
    }

    public void initData(){
        recyclerAdapter = new RecyclerAdapterKategori(dataList, context);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnclickCallback(new RecyclerAdapterKategori.OnItemClickCallback() {
            @Override
            public void onItemClick(Kategori item, int position) {
                Intent intent = new Intent(context, ProdukActivity.class);
                intent.putExtra("id_kategori", item.getId());
                intent.putExtra("nama_kategori", item.getNama());
                startActivity(intent);
            }
        });
    }

    public void getData(){
        loading = ProgressDialog.show(context, null, "Harap Tunggu...", true, false);
        dataList.clear();
        mApiClient = UtilsApi.getAPIService();
        mApiClient.getKategori()
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if(response.isSuccessful()){
                            if(response.body() == null){
                                Toast.makeText(getContext(), "Data Tidak Tersedia", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            } else {
                                for(int i=0;i<response.body().size();i++) {
                                    String mJsonString = response.body().get(i).toString();
                                    JsonParser parser = new JsonParser();
                                    JsonElement mJson =  parser.parse(mJsonString);
                                    Gson gson = new Gson();
                                    Kategori object = gson.fromJson(mJson, Kategori.class);
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
                            Toast.makeText(getContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.i("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(getContext(), "Keneksi Error", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }
}