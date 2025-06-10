package com.example.translater_erbol20.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.translater_erbol20.databinding.ItemDualBinding;
import com.example.translater_erbol20.models.DualModels;
import com.example.translater_erbol20.room.AppDatabase;
import com.example.translater_erbol20.room.LingvoDao;

import java.util.ArrayList;
import java.util.List;

public class DualAdapter extends RecyclerView.Adapter<DualAdapter.ViewHolder> {

    ItemDualBinding binding;
    List<DualModels> dual_list = new ArrayList<>();

    LingvoDao lingvoDao;

    public void setDual_list(List<DualModels> dual_list) {
        this.dual_list = dual_list;
    }

    @NonNull
    @Override
    public DualAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemDualBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DualAdapter.ViewHolder holder, int position) {
        lingvoDao = Room.databaseBuilder(
                holder.binding.getRoot().getContext(),
                AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build()
                .lingvoDao();
        DualModels models = dual_list.get(position);
        holder.onBind(dual_list.get(position));

    }

    @Override
    public int getItemCount() {
        return dual_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDualBinding binding;
        public ViewHolder(@NonNull ItemDualBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void onBind(DualModels dualModels) {
            binding.fromText.setText(dualModels.getFromText());
            binding.resultText.setText(dualModels.getResultText());
        }
    }
}
