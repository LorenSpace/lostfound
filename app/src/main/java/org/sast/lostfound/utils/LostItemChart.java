package org.sast.lostfound.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.sast.lostfound.model.LostItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LostItemChart {
    public static void showCategoryChart(List<LostItem> items, PieChart chart,
                                         String[] categories) {
        Map<String, Integer> categoryMap = new HashMap<>();
        for (LostItem item : items) {
            String category = item.getCategory();
            if (categoryMap.containsKey(category)) {
                categoryMap.put(category, categoryMap.get(category) + 1);
            } else {
                categoryMap.put(category, 1);
            }
        }
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.setCenterText("Categories");
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(Color.BLACK);
        // 显示每个部分的文字
        chart.setDrawEntryLabels(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
    }

    // 创建柱状图
    public static void showLostAndFoundChart(List<LostItem> items, BarChart chart) {
        Map<String, Integer> lostItemsMap = new HashMap<>();
        Map<String, Integer> foundItemsMap = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        for (LostItem item : items) {
            String month = dateFormat.format(item.getTime());
            if (item.getStatus().equals("未认领")) {
                if (lostItemsMap.containsKey(month)) {
                    lostItemsMap.put(month, lostItemsMap.getOrDefault(month, 0) + 1);
                } else {
                    lostItemsMap.put(month, 1);
                }
            } else if (item.getStatus().equals("已认领")) {
                if (foundItemsMap.containsKey(month)) {
                    foundItemsMap.put(month, foundItemsMap.get(month) + 1);
                } else {
                    foundItemsMap.put(month, 1);
                }
            }
        }
        List<BarEntry> lostEntries = new ArrayList<>();
        List<BarEntry> foundEntries = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : lostItemsMap.entrySet()) {
            lostEntries.add(new BarEntry(i, entry.getValue()));
            i++;
        }
        i = 0;
        for (Map.Entry<String, Integer> entry : foundItemsMap.entrySet()) {
            foundEntries.add(new BarEntry(i, entry.getValue()));
            i++;
        }
        BarDataSet lostDataSet = new BarDataSet(lostEntries, "Lost Items");
        lostDataSet.setColor(Color.RED);
        BarDataSet foundDataSet = new BarDataSet(foundEntries, "Found Items");
        foundDataSet.setColor(Color.GREEN);
        BarData data = new BarData(lostDataSet, foundDataSet);
        data.setBarWidth(0.3f);
        chart.setData(data);
        chart.setFitBars(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter((value, axis) -> {
            String month = "";
            if (value >= 0 && value < lostItemsMap.size()) {
                month = new ArrayList<>(lostItemsMap.keySet()).get((int) value);
            } else if (value >= lostItemsMap.size() && value < lostItemsMap.size() + foundItemsMap.size()) {
                month = new ArrayList<>(foundItemsMap.keySet()).get((int) value - lostItemsMap.size());
            }
            return month;
        });
        chart.invalidate();
    }
}
