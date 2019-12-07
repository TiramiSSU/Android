package com.example.ssuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ProjectManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_project_management);

        Intent intent = getIntent();

        String projectName = intent.getExtras().getString("ProjectName");

        Log.d("jinwoo/","받은 이름: "+projectName);
    }
//
//    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근
//    Button[] totalBtnAry;   //전체목표 버튼
//    int[] totalBtnId;       //전체목표 버튼ID 저장
//    int total0btnId;          //전체목표0 버튼ID저장
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_project_management);
//        final String TAG_JW = getString(R.string.TAG_JW);
////        //전체 진행도 관리
////        Log.d(TAG_JW, "프로젝트 진행도 관리 액티비티 실행");
////        DocumentReference docRef = db.
////                collection("PatraSSU").
////                document("TeamJw").
////                collection("ProjectManagement").
////                document("TotalAim");
////        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////            @Override
////            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                Log.d(TAG_JW+"project_management", "프로젝트 진행도 db에서 받아오기");
////                //값 설정
////                int totalAimcnt = toIntExact(documentSnapshot.getLong("Aimcnt"));
////                String index, totalAim;
////
////                //세팅할 동적 버튼 설정
////                totalBtnAry = new Button[totalAimcnt];
////
////                //전체목표 버튼을 생성할 스크롤뷰 안의 레이아웃
////                LinearLayout totalAimLayout = (LinearLayout) findViewById(R.id.totalAimLinearLayout);
////                //전체 목표가 없을 시
////                if (totalAimcnt == 0) {
////                    Log.d(TAG_JW+"project_management", "전체 목표 개수: 0");
////                    Button total0Btn = new Button(getBaseContext());
////                    total0Btn.setId(View.generateViewId());
////                    total0Btn.setHeight(400);
////                    total0Btn.setWidth(400);
////                    total0Btn.setText("전체 목표 추가");
////                    //추가버튼 클릭시 추가하는 액티비티로 전환
////                    total0Btn.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////                            Intent intent = new Intent(getApplicationContext(), ProjectAimAddActivity.class);
////                            startActivity(intent);
////                        }
////                    });
////                    totalAimLayout.addView(total0Btn);
////                }
////                //전체 목표가 있을시 버튼 추가
////                else {
////                    Log.d(TAG_JW+"project_management", "전체 목표 개수: " + totalAimcnt);
////                    for (int i = 0; i < totalAimcnt; i++) {
////                        totalBtnAry[i] = new Button(getBaseContext());
////                        index = Integer.toString(i);
////                        totalAim = documentSnapshot.getString(index);
////
////                        totalBtnAry[i].setId(View.generateViewId());
////                        totalBtnAry[i].setHeight(400);
////                        totalBtnAry[i].setWidth(400);
////                        totalBtnAry[i].setText(totalAim);
////                        totalAimLayout.addView(totalBtnAry[i]);
////                    }
////                }
////            }
////        });
//////전체 진행도 관리
////개인 진행도 관리
////개인 진행도 관리
//
////참고자료 관리
////참고자료 관리
//    }
//
//    @Override
//    public void onResume() {
//        final String TAG_JW = getString(R.string.TAG_JW);
//        //데이터 변경되었을때
//        final DocumentReference docRef = db.
//                collection("PatraSSU").
//                document("TeamJw").
//                collection("ProjectManagement").
//                document("TotalAim");
//
//        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                Log.d("jinwoo/", "데이터가 변경되었음");
//                Log.d("jinwoo/", documentSnapshot.getId() + "=>" + documentSnapshot.getData());
//
//                LinearLayout linearLayout = findViewById(R.id.totalAimLinearLayout);
//                linearLayout.removeAllViews();
////전체 진행도 관리
//                Log.d(TAG_JW, "프로젝트 진행도 관리 액티비티 실행");
//
//
//                Log.d(TAG_JW + "project_management", "프로젝트 진행도 db에서 받아오기");
//                //값 설정
//                int totalAimcnt = toIntExact(documentSnapshot.getLong("Aimcnt"));
//                String index, totalAim;
//
//                //세팅할 동적 버튼 설정
//                totalBtnAry = new Button[totalAimcnt];
//
//                //전체목표 버튼을 생성할 스크롤뷰 안의 레이아웃
//                LinearLayout totalAimLayout = findViewById(R.id.totalAimLinearLayout);
//                //전체 목표가 없을 시
//                if (totalAimcnt == 0) {
//                    Log.d(TAG_JW + "project_management", "전체 목표 개수: 0");
//                    Button total0Btn = new Button(getBaseContext());
//                    total0Btn.setId(View.generateViewId());
//                    total0Btn.setHeight(400);
//                    total0Btn.setWidth(400);
//                    total0Btn.setText("전체 목표 추가");
//                    //추가버튼 클릭시 추가하는 액티비티로 전환
//                    total0Btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getApplicationContext(), ProjectAimAddActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    totalAimLayout.addView(total0Btn);
//                }
//                //전체 목표가 있을시 버튼 추가
//                else {
//                    Log.d(TAG_JW + "project_management", "전체 목표 개수: " + totalAimcnt);
//                    for (int i = 0; i < totalAimcnt; i++) {
//                        totalBtnAry[i] = new Button(getBaseContext());
//                        index = Integer.toString(i);
//                        totalAim = documentSnapshot.getString(index);
//
//                        totalBtnAry[i].setId(View.generateViewId());
//                        totalBtnAry[i].setHeight(400);
//                        totalBtnAry[i].setWidth(400);
//                        totalBtnAry[i].setText(totalAim);
//                        totalAimLayout.addView(totalBtnAry[i]);
//                    }
//                }
////전체 진행도 관리
//            }
//        });
//        super.onResume();
//    }
}
