package hu.bme.aut.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.shoppinglist.adapter.ShoppingAdapter;
import hu.bme.aut.shoppinglist.data.ShoppingItem;
import hu.bme.aut.shoppinglist.data.ShoppingItemDao;


public class StatActivity extends AppCompatActivity {

    int foodEx = 0;
    int electrEx = 0;
    int bookEx = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        setupPieChart();
        calculateSums();
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(foodEx, "kaja"));
        entries.add(new PieEntry(electrEx, "electrex"));
        entries.add(new PieEntry(bookEx, "könyv"));



        PieDataSet dataSet = new PieDataSet(entries, "Shopping Items");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        PieChart chart = findViewById(R.id.chart);
        chart.setData(data);
        chart.invalidate();
    }

    void calculateSums() {

        for (ShoppingItem s : l) {
            switch (s.category) {
                case FOOD: {
                    foodEx += s.sum;
                    break;
                }
                case ELECTRONIC: {
                    electrEx += s.sum;
                    break;
                }
                case BOOK: {
                    bookEx += s.sum;
                    break;
                }

            }
            break;
        }
    }
}