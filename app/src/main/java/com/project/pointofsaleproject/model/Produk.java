package com.project.pointofsaleproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Produk implements Parcelable {
    String id;
    String photo;
    String satuan;
    String nama;
    Integer stok;
    Integer id_kategori;
    String nama_kategori;
    String harga;

    public Produk(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public Integer getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(Integer id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    protected Produk(Parcel in) {
        id = in.readString();
        photo = in.readString();
        satuan = in.readString();
        nama = in.readString();
        if (in.readByte() == 0) {
            stok = null;
        } else {
            stok = in.readInt();
        }
        if (in.readByte() == 0) {
            id_kategori = null;
        } else {
            id_kategori = in.readInt();
        }
        nama_kategori = in.readString();
        harga = in.readString();
    }

    public static final Creator<Produk> CREATOR = new Creator<Produk>() {
        @Override
        public Produk createFromParcel(Parcel in) {
            return new Produk(in);
        }

        @Override
        public Produk[] newArray(int size) {
            return new Produk[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(photo);
        parcel.writeString(satuan);
        parcel.writeString(nama);
        if (stok == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(stok);
        }
        if (id_kategori == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id_kategori);
        }
        parcel.writeString(nama_kategori);
        parcel.writeString(harga);
    }
}
