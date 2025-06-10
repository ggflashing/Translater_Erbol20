package com.example.translater_erbol20.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.translater_erbol20.R;
import com.example.translater_erbol20.databinding.FragmentDashboardBinding;
import com.example.translater_erbol20.models.DualModels;
import com.example.translater_erbol20.room.AppDatabase;
import com.example.translater_erbol20.room.LingvoDao;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    FragmentDashboardBinding binding;

    AppDatabase appDatabase;

     DualAdapter dualAdapter;

    LingvoDao lingvoDao;

    ArrayList<DualModels> dual_list = new ArrayList<>();

    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater,container, false);
        View root = binding.getRoot();


         dualAdapter = new DualAdapter();
        binding.rvMain.setAdapter(dualAdapter);

        appDatabase = Room.databaseBuilder(
                binding.getRoot().getContext(),
                AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        lingvoDao = appDatabase.lingvoDao();
        dualAdapter.setDual_list(lingvoDao.getAll());

        return root;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v1 -> {

            navController = Navigation.findNavController(
                    requireActivity(), R.id.nav_host_fragment_activity_main);

            navController.navigate(R.id.action_navigation_dashboard_to_navigation_home);


        });
    }
}