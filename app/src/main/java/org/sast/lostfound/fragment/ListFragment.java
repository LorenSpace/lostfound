package org.sast.lostfound.fragment;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.sast.lostfound.R;
import org.sast.lostfound.dao.LostItemDAO;
import org.sast.lostfound.model.LostItem;
import org.sast.lostfound.utils.LostItemChart;

import java.util.List;

public class ListFragment extends Fragment {

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Resources res = getResources();
        String[] categories = res.getStringArray(R.array.category_array);

        LostItemDAO dao = new LostItemDAO(view.getContext());
        List<LostItem> items = dao.getLostItems();
        BarChart barChart = view.findViewById(R.id.bar_chart);
        PieChart pieChart = view.findViewById(R.id.pie_chart);
        LostItemChart.showLostAndFoundChart(items, barChart);
        LostItemChart.showCategoryChart(items, pieChart, categories);
    }
}