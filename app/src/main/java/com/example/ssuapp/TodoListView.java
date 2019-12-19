package com.example.ssuapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TodoListView extends RelativeLayout {

    LinearLayout detailTextView;
    LinearLayout detailEditText;

    TextView totalTodoTextView;
    TextView detail_1_TodoTextView;
    TextView detail_2_TodoTextView;
    TextView detail_3_TodoTextView;

    EditText totalTodoEditText;
    EditText detail_1_TodoEditText;
    EditText detail_2_TodoEditText;
    EditText detail_3_TodoEditText;

    ImageButton todoAddBtn;
    ImageButton todoEditBtn;
    ImageButton todoDeleteBtn;
    ImageButton todoEditCheckBtn;
    ImageButton todoEditCalncleBtn;

    CheckBox progressCheckBox;
    RelativeLayout entireLayout;
    RelativeLayout titleLayout;

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

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);
        View v = inflater.inflate(R.layout.view_list_todo, this, false);
        addView(v);

        detailTextView = findViewById(R.id.detailTextView);
        detailEditText = findViewById(R.id.detailEditText);

        totalTodoTextView = findViewById(R.id.totalTodoTextView);
        detail_1_TodoTextView = findViewById(R.id.detail_1_TodoTextView);
        detail_2_TodoTextView = findViewById(R.id.detail_2_TodoTextView);
        detail_3_TodoTextView = findViewById(R.id.detail_3_TodoTextView);

        totalTodoEditText = findViewById(R.id.totalTodoEditText);
        detail_1_TodoEditText = findViewById(R.id.detail_1_TodoEditText);
        detail_2_TodoEditText = findViewById(R.id.detail_2_TodoEditText);
        detail_3_TodoEditText = findViewById(R.id.detail_3_TodoEditText);

        todoAddBtn = findViewById(R.id.todoAddBtn);
        todoEditBtn = findViewById(R.id.todoEditBtn);
        todoDeleteBtn = findViewById(R.id.todoDeleteBtn);
        todoEditCheckBtn = findViewById(R.id.todoEditCheckBtn);
        todoEditCalncleBtn = findViewById(R.id.todoEditCancleBtn);

        progressCheckBox = findViewById(R.id.progress_check);
        entireLayout = findViewById(R.id.entireLayout);
        titleLayout = findViewById(R.id.titleMenuBar);

        //편집버튼      그리고 누를때는 EditText로 바꾸고 textview값을 그대로 가져옴
        todoEditBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 visibility 관리
                todoEditBtn.setVisibility(View.INVISIBLE);
                todoDeleteBtn.setVisibility(View.INVISIBLE);
                todoEditCalncleBtn.setVisibility(View.VISIBLE);
                todoEditCheckBtn.setVisibility(View.VISIBLE);

                //TextVIew - EditText 관리
                String temp;

                temp = totalTodoTextView.getText().toString();
                totalTodoEditText.setText(temp);
                temp = detail_1_TodoTextView.getText().toString();
                detail_1_TodoEditText.setText(temp);
                temp = detail_2_TodoTextView.getText().toString();
                detail_2_TodoEditText.setText(temp);
                temp = detail_3_TodoTextView.getText().toString();
                detail_3_TodoEditText.setText(temp);

                totalTodoTextView.setVisibility(View.GONE);
                totalTodoEditText.setVisibility(View.VISIBLE);
                detailTextView.setVisibility(View.GONE);
                detailEditText.setVisibility(View.VISIBLE);
            }
        });
        //편집 취소 버튼 누를시 DB에 값 바꾸지 않고 다시 편집전의 Text로 돌아감
        todoEditCalncleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼 visibility 관리
                todoEditBtn.setVisibility(View.VISIBLE);
                todoDeleteBtn.setVisibility(View.VISIBLE);
                todoEditCalncleBtn.setVisibility(View.INVISIBLE);
                todoEditCheckBtn.setVisibility(View.INVISIBLE);

                totalTodoTextView.setVisibility(View.VISIBLE);
                totalTodoEditText.setVisibility(View.GONE);

                detailTextView.setVisibility(View.VISIBLE);
                detailEditText.setVisibility(View.GONE);
            }
        });
        //체크박스 선택될때 뷰 색깔 바꾸기
        progressCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (progressCheckBox.isChecked()) {
                    entireLayout.setBackgroundColor(getResources().getColor(R.color.gray_2));
                    titleLayout.setBackgroundColor(getResources().getColor(R.color.gray_1));
                    todoDeleteBtn.setBackgroundColor(getResources().getColor(R.color.gray_2));
                    todoEditBtn.setBackgroundColor(getResources().getColor(R.color.gray_2));
                    todoEditCalncleBtn.setBackgroundColor(getResources().getColor(R.color.gray_2));
                    todoEditCheckBtn.setBackgroundColor(getResources().getColor(R.color.gray_2));

                } else if (!progressCheckBox.isChecked()) {
                    entireLayout.setBackgroundColor(getResources().getColor(R.color.blue_1));
                    titleLayout.setBackgroundColor(getResources().getColor(R.color.blue_2));
                    todoDeleteBtn.setBackgroundColor(getResources().getColor(R.color.blue_1));
                    todoEditBtn.setBackgroundColor(getResources().getColor(R.color.blue_1));
                    todoEditCalncleBtn.setBackgroundColor(getResources().getColor(R.color.blue_1));
                    todoEditCheckBtn.setBackgroundColor(getResources().getColor(R.color.blue_1));
                }
            }
        });
    }

    //각 버튼이 클릭될 때 View 바꾸기
    public void button_clicked(String btnName) {
        String temp;
        switch (btnName) {
            case "editCheck":
                //버튼 visibility 관리
                todoEditBtn.setVisibility(View.VISIBLE);
                todoDeleteBtn.setVisibility(View.VISIBLE);
                todoEditCalncleBtn.setVisibility(View.INVISIBLE);
                todoEditCheckBtn.setVisibility(View.INVISIBLE);

                //TextVIew - EditText 관리
                temp = totalTodoEditText.getText().toString();
                totalTodoTextView.setText(temp);
                temp = detail_1_TodoEditText.getText().toString();
                detail_1_TodoTextView.setText(temp);
                temp = detail_2_TodoEditText.getText().toString();
                detail_2_TodoTextView.setText(temp);
                temp = detail_3_TodoEditText.getText().toString();
                detail_3_TodoTextView.setText(temp);

                totalTodoTextView.setVisibility(View.VISIBLE);
                totalTodoEditText.setVisibility(View.GONE);

                detailTextView.setVisibility(View.VISIBLE);
                detailEditText.setVisibility(View.GONE);
                break;
        }
    }

    public void changeMode(String mode) {
        switch (mode) {
            case "add":
                String temp;

                temp = totalTodoTextView.getText().toString();
                totalTodoEditText.setText(temp);
                temp = detail_1_TodoTextView.getText().toString();
                detail_1_TodoEditText.setText(temp);
                temp = detail_2_TodoTextView.getText().toString();
                detail_2_TodoEditText.setText(temp);
                temp = detail_3_TodoTextView.getText().toString();
                detail_3_TodoEditText.setText(temp);

                totalTodoTextView.setVisibility(View.GONE);
                totalTodoEditText.setVisibility(View.VISIBLE);
                detailTextView.setVisibility(View.GONE);
                detailEditText.setVisibility(View.VISIBLE);

                progressCheckBox.setVisibility(View.GONE);
                break;
            case "view":
                todoAddBtn.setVisibility(View.GONE);
                todoEditBtn.setVisibility(View.VISIBLE);
                todoDeleteBtn.setVisibility(View.VISIBLE);
                progressCheckBox.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void setTotalTodoText(String input) {
        totalTodoTextView.setText(input);
    }

    public void setDetail_1_TodoTextView(String input) {
        detail_1_TodoTextView.setText(input);
    }

    public void setDetail_2_TodoTextView(String input) {
        detail_2_TodoTextView.setText(input);
    }

    public void setDetail_3_TodoTextView(String input) {
        detail_3_TodoTextView.setText(input);
    }

    public String getTotalTodoText() {
        return totalTodoTextView.getText().toString();
    }

    public String getDetail_1_TodoTextView() {
        return detail_1_TodoTextView.getText().toString();
    }

    public String getDetail_2_TodoTextView() {
        return detail_2_TodoTextView.getText().toString();
    }

    public String getDetail_3_TodoTextView() {
        return detail_3_TodoTextView.getText().toString();
    }

    public String getTotalEditText() {
        return totalTodoEditText.getText().toString().trim();
    }

    public String getDetail_1_TodoEditText() {
        return detail_1_TodoEditText.getText().toString().trim();
    }

    public String getDetail_2_TodoEditText() {
        return detail_2_TodoEditText.getText().toString().trim();
    }

    public String getDetail_3_TodoEditText() {
        return detail_3_TodoEditText.getText().toString().trim();
    }

    public boolean validTotalTodo() {
        return totalTodoEditText.getText().toString().trim() == null || "".equals(totalTodoEditText.getText().toString().trim());
    }
}