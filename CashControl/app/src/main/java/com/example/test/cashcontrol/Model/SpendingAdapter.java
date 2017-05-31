package com.example.test.cashcontrol.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test.cashcontrol.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SpendingAdapter extends BaseAdapter {

    private ArrayList<Spending> spendingArray = new ArrayList<Spending>();
    private Context context;
    final  static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    //private ArrayList<Good> arr_goods_adapter = new ArrayList<Good>();
    private LayoutInflater layoutInflater;
    public SpendingAdapter(Context context, ArrayList<Spending> spArray) {
        this.context = context;
        this.spendingArray = spArray;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return spendingArray.size();
    }

    @Override
    public Object getItem(int position) {
        return spendingArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item, null, false);
        }

        Spending good_temp = spendingArray.get(position);

       /* TextView tv_goodId = (TextView) view.findViewById(R.id.tv_Number);
        tv_goodId.setText(Integer.toString(good_temp.getId()));*/

        TextView tv_goodName = (TextView) view.findViewById(R.id.tv_Name);
        String curDate = DATE_FORMAT.format(good_temp.getDate().getTime());
        tv_goodName.setText(good_temp.getName() + curDate);

        TextView goodPrice = (TextView) view.findViewById(R.id.tv_Price);
        goodPrice.setText(Integer.toString(good_temp.getPrice()));

        TextView goodCategory = (TextView) view.findViewById(R.id.tv_Category);
        goodCategory.setText(good_temp.getCategory());

        return view;
    }
    public ArrayList<Spending> getSpending(){
        return spendingArray;
    }

    public Spending findSpendingById(int id)
    {
        for (Spending spending: spendingArray)
        {
            if(spending.getId() == id)
            {
                return spending;
            }
        }
        return null;
    }
}
