package com.project.pointofsaleproject.screen;

import static com.project.pointofsaleproject.api.UtilsApi.BASE_URL_API_IMAGE_PRODUK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.databinding.ActivityProdukDetailBinding;

public class ProdukDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityProdukDetailBinding binding;
    String image = "";
    String id = "";
    String harga = "";
    Integer stok = 0;
    String nama;
    String satuan;
    Integer totalBeli = 1;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProdukDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = ProdukDetailActivity.this;
        id = getIntent().getStringExtra("id");
        image = getIntent().getStringExtra("image");
        harga = getIntent().getStringExtra("harga");
        stok = getIntent().getIntExtra( "stok", 0);
        nama = getIntent().getStringExtra( "nama");
        satuan = getIntent().getStringExtra( "satuan");

        Glide.with(this).load(BASE_URL_API_IMAGE_PRODUK + image).placeholder(R.drawable.ic_photo).into(binding.imgvImage);

        binding.tvTitle.setText(nama);
        binding.tvStok.setText("Stok : "+ stok + " " + satuan);

        binding.btnBack.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnRemove.setOnClickListener(this);
        binding.btnBuy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_add){
            if(totalBeli < stok){
                totalBeli += 1;
                binding.etQty.setText(totalBeli + "");
            }else {
                Toast.makeText(this, "Melebihi Stok", Toast.LENGTH_SHORT).show();
            }
        } else if(view.getId() == R.id.btn_remove){
            if(totalBeli > 1){
                totalBeli -= 1;
                binding.etQty.setText(totalBeli + "");
            }else {
                Toast.makeText(this, "Minimal beli satu", Toast.LENGTH_SHORT).show();
            }
        }else if(view.getId() == R.id.btn_buy) {
            if(stok > 0){
                Intent intent = new Intent(context, ProdukCheckoutActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("image", image);
                intent.putExtra("harga", harga);
                intent.putExtra("nama", nama);
                intent.putExtra("satuan", satuan);
                intent.putExtra("qty", binding.etQty.getText().toString());
                startActivity(intent);
            }else{
                Toast.makeText(this, "Stok tidak tersedia", Toast.LENGTH_SHORT).show();
            }
        }else if(view.getId() == R.id.btn_back){
            finish();
        }
    }
}