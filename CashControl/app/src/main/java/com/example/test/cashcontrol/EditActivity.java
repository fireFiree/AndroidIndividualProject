package com.example.test.cashcontrol;

/**
 * Created by NotePad.by on 03.05.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;


public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    final int EDIT = 2;

    EditText et_Name, et_Price;
    Spinner sp_Category;
    Button btn_Send;
    String id;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        initView();
    }

    private void initView() {

        et_Name = (EditText) findViewById(R.id.et_Name);
        et_Price = (EditText) findViewById(R.id.et_Price);
        btn_Send = (Button) findViewById(R.id.btn_Send);
        btn_Send.setOnClickListener(this);
        sp_Category = (Spinner) findViewById(R.id.sp_Category);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String price = intent.getStringExtra("Price");
        id = intent.getStringExtra("Id");
        date = intent.getStringExtra("Date");
        et_Name.setText(name);
        et_Price.setText(price);

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        intent.putExtra("Id", id);
        intent.putExtra("Date", date);
        intent.putExtra("Name", et_Name.getText().toString());
        intent.putExtra("Price", et_Price.getText().toString());
        intent.putExtra("Category", sp_Category.getSelectedItem().toString());
        setResult(EDIT, intent);
        finish();
    }
}