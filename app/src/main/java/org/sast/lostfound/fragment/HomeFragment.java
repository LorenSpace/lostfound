package org.sast.lostfound.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sast.lostfound.R;
import org.sast.lostfound.adapter.LostItemAdapter;
import org.sast.lostfound.dao.LostItemDAO;
import org.sast.lostfound.model.LostItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private static final String DEFAULT_SELECTED = "全部";
    private LostItemAdapter adapter;
    private LostItemDAO lostItemDAO;
    private String category;
    private int month;
    private String status;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lostItemDAO = new LostItemDAO(view.getContext());
        // 初始化RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        List<LostItem> itemList = lostItemDAO.getLostItems();

        adapter = new LostItemAdapter(view.getContext(), itemList);
        recyclerView.setAdapter(adapter);

        // 初始化失物类别 Spinner
        Spinner categorySpinner = view.findViewById(R.id.register_category_spinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根据选中的类别筛选失物
                category = parent.getItemAtPosition(position).toString();
                filterItems(category, month, status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // 初始化失物月份 Spinner
        Spinner monthSpinner = view.findViewById(R.id.month_spinner);
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.month_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 根据选中的月份筛选失物
                month = position;
                filterItems(category, position, status);
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
                status = parent.getItemAtPosition(position).toString();
                filterItems(category, month, status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterItems(String category, int month, String status) {
        // 根据类别、月份和认领状态筛选失物
        // 构造查询条件
        String selection = null;
        List<String> selectionArgs = new ArrayList<>();
        if (category != null && !category.equals(DEFAULT_SELECTED)) {
            selection = "category=?";
            selectionArgs.add(category);
        }
        // Java 中的时间为 unix 单位，为毫秒
        if (month > 0) {
            String monthStr = String.format(Locale.US, "%02d", month);
            if (selection == null) {
                selection = "strftime('%m', datetime(time/1000, 'unixepoch'))=?";
            } else {
                selection += " AND strftime('%m', datetime(time/1000, 'unixepoch'))=?";
            }
            selectionArgs.add(monthStr);
        }
        if (status != null && !status.equals(DEFAULT_SELECTED)) {
            if (selection == null) {
                selection = "status=?";
            } else {
                selection += " AND status=?";
            }
            selectionArgs.add(status);
        }
        List<LostItem> filteredList = lostItemDAO
                .getLostItemByFilter(selection, selectionArgs);
        adapter.filterList(filteredList);
        adapter.notifyDataSetChanged();
    }
}