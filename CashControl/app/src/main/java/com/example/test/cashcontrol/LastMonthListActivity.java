package com.example.test.cashcontrol;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.cashcontrol.DB.DB;
import com.example.test.cashcontrol.Model.Spending;
import com.example.test.cashcontrol.Model.SpendingAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by NotePad.by on 09.05.2017.
 */

public class LastMonthListActivity extends AppCompatActivity {

    private Cursor cursor;
    private DB db;
    private ArrayList<Spending> spendingArray = new ArrayList<Spending>();
    private LayoutInflater layoutInflater;
    private Button btnAdd;
    ListView lv_Spending;
    private TextView tv_Price;
    private View view_footer;
    private SpendingAdapter spAdapter;
    //final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lastmonth_activity);
        initDB();
        initView();
        createMyListView();
    }

    private void initDB() {
        db = new DB(this);
        db.open();
    }

    private int getPriceSumFromArray(ArrayList<Spending> arr){
        int sum = 0;
        for (Spending sp: arr) {
            sum += sp.getPrice();
        }
        return sum;
    }
    private void createMyListView() {
        fillData();
        spAdapter = new SpendingAdapter(this, spendingArray);
        layoutInflater = LayoutInflater.from(this);

        view_footer = layoutInflater.inflate(R.layout.footer, null);

        btnAdd = (Button) view_footer.findViewById(R.id.btn_Add);
        btnAdd.setVisibility(View.INVISIBLE);
        TextView txv = (TextView)view_footer.findViewById(R.id.tv_Limit);
        txv.setVisibility(View.INVISIBLE);
        TextView limCount = (TextView)view_footer.findViewById(R.id.textView4);
        limCount.setVisibility(View.INVISIBLE);

        lv_Spending.addFooterView(view_footer);

        lv_Spending.setAdapter(spAdapter);

        tv_Price = (TextView) view_footer.findViewById(R.id.tv_Spended);

        tv_Price.setText(String.valueOf(getPriceSumFromArray(spendingArray)));

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
    }

    void initView(){
        lv_Spending = (ListView)findViewById(R.id.lv_Spending);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Вывести текущий месяц");
        menu.add(0, 2, 0, "Статистика");
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 1:{
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case 2:{
                Intent intent = new Intent(this, GraphicActivity.class);
                startActivity(intent);
                break;
            }
        }


        return super.onOptionsItemSelected(item);

    }

}
