package com.example.ssuapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TodoListView extends RelativeLayout {

    TextView totalTodoText;
    TextView detail_1_TodoText;
    TextView detail_2_TodoText;
    TextView detail_3_TodoText;

    public TodoListView(Context context) {
        super(context);
        initView();
    }

    public TodoListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TodoListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(infService);
        View v = inflater.inflate(R.layout.view_list_todo, this, false);
        addView(v);

        totalTodoText = findViewById(R.id.totalTodoText);
        detail_1_TodoText = findViewById(R.id.detail_1_TodoText);
        detail_2_TodoText = findViewById(R.id.detail_2_TodoText);
        detail_3_TodoText = findViewById(R.id.detail_3_TodoText);
    }

    public void setTotalTodoText(String input){
        totalTodoText.setText(input);
    }

    public void setDetail_1_TodoText(String input){
        detail_1_TodoText.setText(input);
    }
    public void setDetail_2_TodoText(String input){
        detail_2_TodoText.setText(input);
    }
    public void setDetail_3_TodoText(String input){
        detail_3_TodoText.setText(input);
    }
}
