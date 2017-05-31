package com.example.test.cashcontrol.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by NotePad.by on 01.05.2017.
 */

public class Spending {
    private int id;
    private Calendar date;
    private String name;
    private int price;
    private String category;
    final  static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public Spending(int _id, String _name, int _price, String _date, String _category){
        id = _id;
        name = _name;

        date = Calendar.getInstance();
        try {
            date.setTime(DATE_FORMAT.parse(_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        price = _price;
        category = _category;
    }


    public Spending(int _id, String _name, int _price, String _category){
        id = _id;
        name = _name;
        Date curDate = new java.util.Date();
        date = Calendar.getInstance();
        date.setTime(curDate);

        price = _price;
        category = _category;
    }

    //getters
    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Calendar getDate() {
        return date;
    }


    //setters
    public void setId(int _id) {
        id = _id;
    }

    public void setPrice(int _price) {
        price = _price;
    }

    public void setName(String _name) {
        name = _name;
    }

    public void setDate(Calendar _date) {
        date = _date;
    }

    public void setCategory(String _category) {
        category = _category;
    }
}
