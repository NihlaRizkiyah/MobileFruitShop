package com.project.pointofsaleproject.adapter;

import static com.project.pointofsaleproject.api.UtilsApi.BASE_URL_API_IMAGE_KATEGORI;
import static com.project.pointofsaleproject.api.UtilsApi.BASE_URL_API_IMAGE_PRODUK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.pointofsaleproject.R;
import com.project.pointofsaleproject.model.Order;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterTrasaction extends RecyclerView.Adapter<RecyclerAdapterTrasaction.ViewHolder> implements Filterable {
    public List<Order> dataList;
    public List<Order> dataListAll;
    private RecyclerAdapterTrasaction.OnItemClickCallback onItemClickCallback;
    Context ctx;

    public void setOnclickCallback(RecyclerAdapterTrasaction.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public RecyclerAdapterTrasaction(List<Order> list, Context context) {
        this.dataList = list;
        dataListAll = new ArrayList<>(list);
        this.ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_transaction, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nama.setText(dataList.get(position).getNama_barang());
        holder.total.setText("Rp. " + dataList.get(position).getTotal());
        Glide.with(ctx).load(BASE_URL_API_IMAGE_PRODUK + dataList.get(position).getPhoto()).placeholder(R.drawable.ic_photo).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClick(dataList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Order> filteredList = new ArrayList<>();
            if(charSequence.length() == 0 || charSequence == null){
                filteredList.addAll(dataListAll);
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values  = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList.clear();
            dataList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView nama, total;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tv_nama);
            total = itemView.findViewById(R.id.tv_total);
            image = itemView.findViewById(R.id.imgv_img);
        }
    }

    public interface OnItemClickCallback {
        void onItemClick(Order item, int position);
    }
}
