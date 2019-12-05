package com.example.ssuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class TimetableMainActivity extends AppCompatActivity implements View.OnClickListener {

    Button dateBtn;
    ImageButton addBtn;
    ImageButton add2Btn;
    Button setBtn;
    Button cbnBtn;


    //이벤트 처리를 위해 dialog 객체를 멤버변수로 선언
    AlertDialog add_customDialog;
    AlertDialog searchDialog;
    AlertDialog set_customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_main);

        //View 객체 획득
        dateBtn = findViewById(R.id.btn_date);
        addBtn = findViewById(R.id.btn_add);
        add2Btn = findViewById(R.id.btn_add2);
        setBtn = findViewById(R.id.btn_set);
        cbnBtn = findViewById(R.id.btn_cbn);

        //버튼 이벤트 등록
        dateBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        add2Btn.setOnClickListener(this);
        setBtn.setOnClickListener(this);
        cbnBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == dateBtn) {
            DatePickerDialog mDatePickerDialog = null;

            DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view,
                                      int year,
                                      int monthOfYear,
                                      int dayOfMonth) {
                    Toast.makeText(TimetableMainActivity.this,
                            year + "년" +
                                    (monthOfYear + 1) + "월" +
                                    dayOfMonth + "일",
                            Toast.LENGTH_LONG).show();
                }
            };
            mDatePickerDialog = new DatePickerDialog(this,
                    callBack,
                    2000,
                    0,
                    1);
            mDatePickerDialog.show();
        } else if (view == addBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // custom dialog를 위한 layout xml 초기화
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View customDialogView = inflater.inflate(R.layout.add_dialog, null);
            builder.setView(customDialogView);
            builder.setPositiveButton("확인", dialogListener);
            builder.setNegativeButton("취소", null);
            add_customDialog = builder.create();
            add_customDialog.show();

        } else if (view == add2Btn) {
            new SimpleSearchDialogCompat(TimetableMainActivity.this, "팀원 찾기", "이름을 입력하세요",
                    null, initData(), new SearchResultListener<Searchable>() {
                @Override
                public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                    Toast.makeText(TimetableMainActivity.this, "" + searchable.getTitle(), Toast.LENGTH_SHORT).show();
                    baseSearchDialogCompat.dismiss();
                }
            }).show();

        } else if (view == setBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // custom dialog를 위한 layout xml 초기화
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View customDialogView = inflater.inflate(R.layout.set_dialog, null);
            builder.setView(customDialogView);
            builder.setPositiveButton("확인", dialogListener);
            builder.setNegativeButton("취소", null);
            set_customDialog = builder.create();
            set_customDialog.show();
        } else if (view == cbnBtn) {
            Intent intent = new Intent(getApplicationContext(), TimetableResultActivity.class);
            startActivity(intent);
        }
    }

    private ArrayList<SearchModel> initData() {
        ArrayList<SearchModel> items = new ArrayList<>();
        items.add(new SearchModel("박우영"));
        items.add(new SearchModel("박진우"));
        items.add(new SearchModel("이규현"));
        items.add(new SearchModel("지소유"));
        items.add(new SearchModel("차태현"));
        items.add(new SearchModel("황하나"));

        return items;
    }

    //매개변수의 문자열을 Toast로 띄우는 개발자 함수
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    //Dialog Button 이벤트 처리
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == add_customDialog && which == DialogInterface.BUTTON_POSITIVE) {
                showToast("custom dialog 확인 click....");
            } else if (dialog == add_customDialog && which == DialogInterface.BUTTON_POSITIVE) {
                showToast("custom dialog 확인 click....");
            }
        }
    };
}
