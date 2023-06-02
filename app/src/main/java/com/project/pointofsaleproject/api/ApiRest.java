package com.project.pointofsaleproject.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRest {

    @FormUrlEncoded
    @POST("api/login")
    Call<JsonObject> login(@Field("username") String username,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("api/register")
    Call<JsonObject> register(@Field("nama") String nama,
                              @Field("username") String username,
                              @Field("alamat") String alamat,
                              @Field("email") String email,
                              @Field("nomor_wa") String nomor_wa,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST("api/updateprofil")
    Call<JsonObject> updateProfil(@Field("id") String id,
                                  @Field("username") String username,
                                  @Field("nama") String nama,
                                  @Field("alamat") String alamat,
                                  @Field("email") String email,
                                  @Field("nomor_wa") String nomor_wa,
                                  @Field("password") String password);

    @GET("api/kategori")
    Call<JsonArray> getKategori();

    @GET("api/barang")
    Call<JsonArray> getProduk(@Query("id_kategori") String id_kategori);


    @GET("api/pesanan")
    Call<JsonArray> getPesanan(@Query("id_user") String id_user);

    @FormUrlEncoded
    @POST("api/addpesanan")
    Call<JsonObject> saveOrder(@Field("id_user") String id_customer, @Field("id_barang") String id_barang,
                               @Field("total") String total, @Field("qty") String qty,
                               @Field("metode_bayar") String metode_bayar);


}
