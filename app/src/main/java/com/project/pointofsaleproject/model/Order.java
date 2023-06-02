package com.project.pointofsaleproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    String id;
    String id_user;
    String photo;
    String id_barang;
    String nama_user;
    String nama_barang;
    Integer qty;
    String metode_bayar;
    Integer total;
    Integer status;

    public Order(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getMetode_bayar() {
        return metode_bayar;
    }

    public void setMetode_bayar(String metode_bayar) {
        this.metode_bayar = metode_bayar;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    protected Order(Parcel in) {
        id = in.readString();
        id_user = in.readString();
        photo = in.readString();
        id_barang = in.readString();
        nama_user = in.readString();
        nama_barang = in.readString();
        if (in.readByte() == 0) {
            qty = null;
        } else {
            qty = in.readInt();
        }
        metode_bayar = in.readString();
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readInt();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_user);
        dest.writeString(photo);
        dest.writeString(id_barang);
        dest.writeString(nama_user);
        dest.writeString(nama_barang);
        if (qty == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(qty);
        }
        dest.writeString(metode_bayar);
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(total);
        }
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
