package com.project.pointofsaleproject.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Kategori implements Parcelable {
    String id;
    String photo;
    String nama;

    public Kategori(){

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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    protected Kategori(Parcel in) {
        id = in.readString();
        photo = in.readString();
        nama = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(photo);
        dest.writeString(nama);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kategori> CREATOR = new Creator<Kategori>() {
        @Override
        public Kategori createFromParcel(Parcel in) {
            return new Kategori(in);
        }

        @Override
        public Kategori[] newArray(int size) {
            return new Kategori[size];
        }
    };
}
