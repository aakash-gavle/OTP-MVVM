package com.example.otpmvvm.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.otpmvvm.MainActivity;
import com.example.otpmvvm.VerificationActivity;
import com.example.otpmvvm.model.User;

public class UserViewModel extends ViewModel {

    public MutableLiveData<String> name=new MutableLiveData<>();
    public MutableLiveData<String> number=new MutableLiveData<>();

    public User user;
    private Context context;

    public UserViewModel(Context context,User user){
        this.user=user;
        this.context=context;
    }

    public void getOTP(){
      // user.setName(name.getValue());
       user.setNumber(number.getValue());
       if (user.isValid()){
           Intent intent=new Intent(context, VerificationActivity.class);
           intent.putExtra("number",/*ccp.getFullNumberWithPlus()*/number.getValue());
           context.startActivity(intent);
       }else
           Toast.makeText(context, "Enter Correct number", Toast.LENGTH_SHORT).show();
       
    }
}
