package com.example.test.cashcontrol;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.cashcontrol.DB.DB;
import com.example.test.cashcontrol.Model.Spending;
import com.example.test.cashcontrol.Model.SpendingAdapter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Cursor cursor;
    private DB db;
    private ArrayList<Spending> spendingArray = new ArrayList<Spending>();
    private ArrayList<Spending> currentMonthSpArr = new ArrayList<Spending>();
    private LayoutInflater layoutInflater;
    private Button btnAdd;
    private TextView tv_Price, tv_Limit;
    ListView lv_Spending;
    private View view_footer;
    private SpendingAdapter spAdapter;
    final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private SharedPreferences sPref;

    final int ADD = 1;
    final int EDIT = 2;

    private static final int NOTIFY_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();
        initView();
        createMyListView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }

    private void fillData() {
        int idColIndex;
        int nameColIndex;
        int priceColIndex;
        int categoryColIndex;
        int dateColIndex;
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
                        cursor.getString(categoryColIndex));
                spendingArray.add(spending);

            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    void initView() {
        lv_Spending = (ListView) findViewById(R.id.lv_Spending);
        registerForContextMenu(lv_Spending);
    }

    private void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("Limit", tv_Limit.getText().toString());
        editor.commit();
    }

    private void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String limit = sPref.getString("Limit", "1000");
        tv_Limit.setText(limit);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    private void initDB() {
        db = new DB(this);
        db.open();
        //db.deleteAllRecords();
        /// ЭТО ЗАКОМЕНТИТЬ
        ///
        ////
        /*db.addRecord("Кеды", 30, "03/04/2017" ,"Непредвиденные");
        db.addRecord("Билеты", 300, "03/04/2017" ,"Развлечение");
        db.addRecord("Лошадь", 271, "05/04/2017" ,"Развлечение");
        db.addRecord("Очки", 30, "11/04/2017" ,"Проживание");
        db.addRecord("Крем для ног", 30, "13/04/2017" ,"Проживание");
        db.addRecord("Футбольный мяч", 30, "14/04/2017" ,"Развлечение");*/
        /////
        ////
        ////ВОТ ДО СЮДА! А В ТЕКУЩИЙ МЕСЯЦ ДОАВИШЬ РУКАМИ

    }

    private void createMyListView() {
        fillData();
        getCurrentMonthArray(spendingArray);
        spAdapter = new SpendingAdapter(this, currentMonthSpArr);
        layoutInflater = LayoutInflater.from(this);

        view_footer = layoutInflater.inflate(R.layout.footer, null);
        btnAdd = (Button) view_footer.findViewById(R.id.btn_Add);
        btnAdd.setOnClickListener(this);
        tv_Price = (TextView) view_footer.findViewById(R.id.tv_Spended);

        tv_Price.setText(String.valueOf(getPriceSumFromArray(currentMonthSpArr)));
        tv_Limit = (TextView) view_footer.findViewById(R.id.tv_Limit);


        //listView.addHeaderView(view_header);
        lv_Spending.addFooterView(view_footer);

        lv_Spending.setAdapter(spAdapter);
        loadText();
        Notification();
    }

    public void getCurrentMonthArray(ArrayList<Spending> arr) {
        for (Spending sp : arr) {

            if (sp.getDate().get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
                currentMonthSpArr.add(sp);
            }
        }
    }

    public void getLastMonthArray(ArrayList<Spending> arr) {
        for (Spending sp : arr) {
            int month = sp.getDate().get(Calendar.MONTH) + 1;
            int curMonth = Calendar.getInstance().get(Calendar.MONTH);
            if (month == curMonth) {
                currentMonthSpArr.add(sp);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == ADD) {
            String name = data.getStringExtra("Name");
            int price = Integer.parseInt(data.getStringExtra("Price"));
            String category = data.getStringExtra("Category");
            Date curDate = new java.util.Date();
            String curDateString = DATE_FORMAT.format(curDate);
            db.addRecord(name, price, curDateString, category);
        }

        if (resultCode == EDIT) {
            String name = data.getStringExtra("Name");
            String category = data.getStringExtra("Category");
            int price = Integer.parseInt(data.getStringExtra("Price"));
            int id = Integer.parseInt(data.getStringExtra("Id"));
            String date = data.getStringExtra("Date");
            for (Spending sp : spendingArray) {
                if (sp.getId() == id) {
                    sp.setName(name);
                    sp.setPrice(price);
                    sp.setCategory(category);
                }
            }
            db.update(id, name, price, date, category);
            spAdapter = new SpendingAdapter(this, spendingArray);
        }
        updateListView();
    }

    private void updateListView() {
        spendingArray.removeAll(spendingArray);
        currentMonthSpArr.removeAll(currentMonthSpArr);
        fillData();
        getCurrentMonthArray(spendingArray);
        spAdapter = new SpendingAdapter(this, currentMonthSpArr);
        spAdapter.notifyDataSetChanged();
        lv_Spending.setAdapter(spAdapter);
        tv_Price.setText(String.valueOf(getPriceSumFromArray(currentMonthSpArr)));
        Notification();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit: {
                Spending sp = currentMonthSpArr.get(info.position);
                String name = sp.getName();
                String date = DATE_FORMAT.format(sp.getDate().getTime());
                String category = sp.getCategory();
                String price = String.valueOf(sp.getPrice());
                String id = String.valueOf(sp.getId());
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("Id", id);
                intent.putExtra("Name", name);
                intent.putExtra("Price", price);
                intent.putExtra("Category", category);
                intent.putExtra("Date", date);

                startActivityForResult(intent, 1);

                return true;
            }
            case R.id.delete: {
                Spending sp = currentMonthSpArr.get(info.position);
                db.deleteRecord(Integer.valueOf(sp.getId()));
                updateListView();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private int getPriceSumFromArray(ArrayList<Spending> arr) {
        int sum = 0;
        for (Spending sp : arr) {
            sum += sp.getPrice();
        }
        return sum;
    }

    @Override
    public void onClick(View v) {

        Intent intentAddGood = new Intent(this, AddActivity.class);
        startActivityForResult(intentAddGood, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Вывести текущий месяц");
        menu.add(0, 2, 0, "Вывести прошлый месяц");
        menu.add(0, 3, 0, "Статистика прошлого");
        menu.add(0, 4, 0, "Статистика текущего");
        menu.add(0, 5, 0, "Установить лимит");
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 2: {
                Intent intent = new Intent(this, LastMonthListActivity.class);
                startActivity(intent);
                break;
            }
            case 3: {
                Intent intent = new Intent(this, GraphicActivity.class);
                startActivity(intent);
                break;
            }
        }

        return super.onOptionsItemSelected(item);

    }

    void Notification() {

        int limit = Integer.parseInt(tv_Limit.getText().toString());
        int price = Integer.parseInt(tv_Price.getText().toString());
        if(limit <= price || limit*0.9 <= price) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // Создаем экземпляр менеджера уведомлений
            int icon = android.R.drawable.btn_star_big_on;
            CharSequence tickerText = "Не забывайте о своём лимите на месяц! Вы израсходовали:"
                    + String.valueOf(price) + " при лимите"
                    + String.valueOf(limit)+ "!!! ";

            long when = System.currentTimeMillis();

            Intent notificationIntent = new Intent(this, MainActivity.class);

            Notification notification = new Notification(icon, tickerText, when); // Создаем экземпляр уведомления, и передаем ему наши параметры
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0); // Подробное описание смотреть в UPD к статье
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification); // Создаем экземпляр RemoteViews указывая использовать разметку нашего уведомления
            contentView.setImageViewResource(R.id.image, R.drawable.cash); // Привязываем нашу картинку к ImageView в разметке уведомления
            contentView.setTextViewText(R.id.text, "Не забывайте о своём лимите на месяц!"); // Привязываем текст к TextView в нашей разметке
            notification.contentIntent = contentIntent; // Присваиваем contentIntent нашему уведомлению
            notification.contentView = contentView; // Присваиваем contentView нашему уведомлению
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            mNotificationManager.notify(NOTIFY_ID, notification);
        }// Выводим уведомление в строку
    }

}
