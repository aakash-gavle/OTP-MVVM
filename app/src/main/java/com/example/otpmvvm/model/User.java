package com.example.otpmvvm.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

public class User {
    private String name,number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isValid() {
        if (this.number.length()==10)
        {
            return true;
        }else
            return false;
    }

}
