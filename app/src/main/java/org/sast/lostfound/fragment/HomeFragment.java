package org.sast.lostfound.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.sast.lostfound.R;
import org.sast.lostfound.adapter.LostItemAdapter;
import org.sast.lostfound.model.LostItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private LostItemAdapter adapter;
    private final List<LostItem> itemList;

    public HomeFragment() {
        itemList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // 初始化RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new LostItemAdapter(view.getContext(), itemList);
        recyclerView.setAdapter(adapter);

        // 初始化失物类别Spinner
        Spinner categorySpinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根据选中的类别筛选失物
                String category = parent.getItemAtPosition(position).toString();
                filterItems(category, null, null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 初始化失物月份Spinner
        Spinner monthSpinner = view.findViewById(R.id.month_spinner);
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.month_array, android.R.layout.simple_spinner_item);



        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根据选中的月份筛选失物
                String month = parent.getItemAtPosition(position).toString();
                filterItems(null, month, null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 初始化失物认领状态Spinner
        Spinner statusSpinner = view.findViewById(R.id.status_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根据选中的认领状态筛选失物
                String status = parent.getItemAtPosition(position).toString();
                filterItems(null, null, status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void filterItems(String category, String month, String status) {
        // 根据类别、月份和认领状态筛选失物
        List<LostItem> filteredList = new ArrayList<>();
        for (LostItem item : itemList) {
            if (category == null || item.getCategory().equals(category)) {
                if (month == null || item.getTime().equals(month)) {
                    if (status == null || item.getStatus().equals(status)) {
                        filteredList.add(item);
                    }
                }
            }
        }
        adapter.filterList(filteredList);
    }
}