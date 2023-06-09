
package org.sast.lostfound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.sast.lostfound.adapter.LostItemAdapter;
import org.sast.lostfound.model.LostItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LostItemAdapter adapter;
    private List<LostItem> itemList = new ArrayList<>();
    private Spinner categorySpinner;
    private Spinner monthSpinner;
    private Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LostItemAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        // 初始化失物类别Spinner
        categorySpinner = findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.category_array, android.R.layout.simple_spinner_item);
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
        monthSpinner = findViewById(R.id.month_spinner);
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.month_array, android.R.layout.simple_spinner_item);



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
        statusSpinner = findViewById(R.id.status_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this, R.array.status_array, android.R.layout.simple_spinner_item);
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