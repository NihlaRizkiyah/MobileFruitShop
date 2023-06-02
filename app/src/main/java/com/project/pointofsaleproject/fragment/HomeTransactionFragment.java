package com.project.pointofsaleproject.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.SharedPrefManager;
import com.project.pointofsaleproject.adapter.RecyclerAdapterTrasaction;
import com.project.pointofsaleproject.api.ApiRest;
import com.project.pointofsaleproject.api.UtilsApi;
import com.project.pointofsaleproject.model.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeTransactionFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerAdapterTrasaction recyclerAdapter;
    ProgressDialog loading;
    Context context;
    ImageView imgvNotFound;
    List<Order> dataList;
    ApiRest mApiClient;
    SharedPrefManager sharedPrefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_transaction, container, false);
        context = getContext();
        recyclerView = root.findViewById(R.id.rv_data);
        imgvNotFound = root.findViewById(R.id.imgv_not_found);
        dataList = new ArrayList<>();

        sharedPrefManager = SharedPrefManager.getInstance(context);

        getData();
        return root;
    }

    public void initData(){
        recyclerAdapter = new RecyclerAdapterTrasaction(dataList, context);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnclickCallback(new RecyclerAdapterTrasaction.OnItemClickCallback() {
            @Override
            public void onItemClick(Order item, int position) {

            }
        });
    }

    public void getData(){
        loading = ProgressDialog.show(context, null, "Harap Tunggu...", true, false);
        String idUser = sharedPrefManager.getAkun(SharedPrefManager.TAG_ID);

        dataList.clear();
        mApiClient = UtilsApi.getAPIService();
        mApiClient.getPesanan(idUser)
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
                                    Order object = gson.fromJson(mJson, Order.class);
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
}