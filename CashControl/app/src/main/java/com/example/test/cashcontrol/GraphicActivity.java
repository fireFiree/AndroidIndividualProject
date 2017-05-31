package com.example.test.cashcontrol;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.test.cashcontrol.DB.DB;
import com.example.test.cashcontrol.Model.Spending;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by NotePad.by on 09.05.2017.
 */

public class GraphicActivity  extends Activity {

    private Cursor cursor;
    private DB db;
    private ArrayList<Spending> spendingArray = new ArrayList<Spending>();
    TextView tv_Sum, tv_Entertain, tv_Live, tv_Unexpected;
    int entertain;
    int live;
    int unexpected;

    int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graphic);

        initDB();
        fillData();
        initGraphic();
        initView();

    }
    private int getPriceSumFromArray(ArrayList<Spending> arr){
        int sum = 0;
        for (Spending sp: arr) {
            sum += sp.getPrice();
        }
        return sum;
    }

    private void initDB() {
        db = new DB(this);
        db.open();
        //db.deleteAllRecords();
        //db.deleteRecord(13);

        /*db.addRecord("lala", 3, "03/04/2017" ,"Развлечение");
        db.addRecord("lala2", 3, "05/04/2017" ,"Развлечение");
        db.addRecord("lala2", 3, "07/04/2017" ,"Развлечение");*/
    }

    private void fillData() {
        int idColIndex;
        int nameColIndex;
        int priceColIndex;
        int categoryColIndex;
        int dateColIndex;
        ArrayList<Spending> temp_arr = new ArrayList<Spending>();
        cursor = db.getAllData();
        if (cursor.moveToFirst()) {
            idColIndex = cursor.getColumnIndex("id");
            nameColIndex = cursor.getColumnIndex("name");
            priceColIndex = cursor.getColumnIndex("price");
            dateColIndex = cursor.getColumnIndex("date");
            categoryColIndex = cursor.getColumnIndex("category");
            do {
                Spending spending = new Spending(cursor.getInt(idColIndex),
                        cursor.getString(nameColIndex),
                        cursor.getInt(priceColIndex),
                        cursor.getString(dateColIndex),
                        cursor.getString(categoryColIndex) );
                temp_arr.add(spending);

            } while (cursor.moveToNext());

        }
        cursor.close();
        for (Spending sp: temp_arr) {
            int month = sp.getDate().get(Calendar.MONTH) + 1;
            int curMonth = Calendar.getInstance().get(Calendar.MONTH );
            if(month  == curMonth)
            {
                spendingArray.add(sp);
            }
        }
        entertain = getSumEntertain();
        live = getSumLive();
        unexpected = getSumUnexpected();

        sum = getPriceSumFromArray(spendingArray);
    }

    void initView(){
        tv_Sum = (TextView) findViewById(R.id.tv_Sum);
        tv_Entertain = (TextView) findViewById(R.id.tv_Entertain);
        tv_Live = (TextView) findViewById(R.id.tv_Live);
        tv_Unexpected = (TextView) findViewById(R.id.tv_Unexpected);

        tv_Sum.setText(String.valueOf(sum) + " руб.");
        tv_Entertain.setText(String.valueOf(entertain) + " руб.");
        tv_Live.setText(String.valueOf(live) + " руб.");
        tv_Unexpected.setText(String.valueOf(unexpected) + " руб.");
}
    private int getSumEntertain(){
        int sum = 0;
        String str = "Развлечение";
        for (Spending sp: spendingArray) {
            if(sp.getCategory().equals(str))
            {
                sum += sp.getPrice();
            }
        }
        return sum;
    }
    private int getSumLive(){
        int sum = 0;
        String str = "Проживание";
        for (Spending sp: spendingArray) {
            if(sp.getCategory().equals(str))
            {
                sum += sp.getPrice();
            }
        }
        return sum;
    }
    private int getSumUnexpected(){
        int sum = 0;
        String str = "Непредвиденные";
        for (Spending sp: spendingArray) {
            if(sp.getCategory().equals(str))
            {
                sum += sp.getPrice();
            }
        }
        return sum;
    }

    void initGraphic(){
        GraphView graph = (GraphView) findViewById(R.id.graph);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, entertain*100/sum ),
                new DataPoint(2, live*100/sum),
                new DataPoint(3, unexpected*100/sum),
                new DataPoint(4, 0),
        });

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/3, (int) Math.abs(data.getY()*255/5), 100);
            }
        });
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        series.setSpacing(0);
        graph.setTitle("График расходов за прошлый месяц");
        graph.setBackgroundColor(Color.WHITE);
        graph.addSeries(series);
    }
}
