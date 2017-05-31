package com.example.test.cashcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_Name, et_Price;
    Spinner sp_Category;
    Button btn_Send;

    final int ADD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        initView();
    }

    void initView(){
        et_Name= (EditText) findViewById(R.id.et_Name);
        et_Price= (EditText) findViewById(R.id.et_Price);
        sp_Category = (Spinner) findViewById(R.id.sp_Category);
        btn_Send = (Button) findViewById(R.id.btn_Send);
        btn_Send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        intent.putExtra("Name", et_Name.getText().toString());
        intent.putExtra("Price", et_Price.getText().toString());
        intent.putExtra("Category", sp_Category.getSelectedItem().toString());
        setResult(ADD, intent);
        finish();
    }
}
