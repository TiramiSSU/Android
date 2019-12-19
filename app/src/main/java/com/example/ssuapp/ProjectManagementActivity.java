package com.example.ssuapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectManagementActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근
    AlertDialog myProjectDeleteDialog;  //프로젝트 삭제 다이얼로그
    AlertDialog myProjectJoinedPeopleDialog; //프로젝트 참여자 다이얼로그
    String projectName;

    String userid;
    String username;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_project_management);

        //Intent 로 프로젝트 이름 받아오기
        Intent intent = getIntent();
        projectName = intent.getExtras().getString("ProjectName");

        db.collection(projectName).document("Progress");

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        if(curUser != null){
            for(UserInfo profile:curUser.getProviderData()){
                userid = profile.getUid();
                username = profile.getDisplayName();
            }
        }

    }

    //프로젝트 삭제 및 참여자 버튼은 툴바에 바꿀것
    public void onClickProjectDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_warning);
        builder.setTitle("경고: 프로젝트 삭제");
        builder.setMessage("정말 삭제 하시겠습니까?");
        //확인버튼 취소버튼 아이콘 확인해보기
        builder.setPositiveButton("삭제", dialogListener);
        builder.setNegativeButton("취소", null);
        myProjectDeleteDialog = builder.create();
        myProjectDeleteDialog.show();
    }

    public void onClickJoinedPeople(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("참여자");
        myProjectJoinedPeopleDialog = builder.create();
        myProjectJoinedPeopleDialog.show();
    }

    //다이얼로그 리스너
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //프로젝트 삭제 다이얼로그
            if (dialog == myProjectDeleteDialog) {
                DocumentReference docRef = db.collection("UserID").document(userid);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int cnt;
                        String projectlist;
                        String[] eachProject;
                        DBUserInformation userInfo = documentSnapshot.toObject(DBUserInformation.class);
                        cnt = userInfo.getProjectcnt();
                        //삭제할 프로젝트가 없을때 예외처리
                        if (cnt == 0) {
                        }
                        //삭제할 프로젝트가 1개일때
                        else if (cnt == 1) {
                            //projectcnt 감소
                            cnt--;
                            userInfo.setProjectcnt(cnt);
                            projectlist = "";
                            userInfo.setProjectlist(projectlist);
                            db.collection("UserID").document(userid).set(userInfo);
                        }
                        //그외의 경우
                        else {
                            //구분자를 활용하여 '@'  String 을 사용 관리하여 가지고있는 프로젝트 리스트 관리
                            cnt--;
                            userInfo.setProjectcnt(cnt);
                            eachProject = documentSnapshot.getString("projectlist").split("@");
                            List<String> list = new ArrayList<>(Arrays.asList(eachProject));
                            list.remove(projectName);
                            projectlist = list.stream().collect(Collectors.joining("@"));
                            userInfo.setProjectlist(projectlist);
                            db.collection("UserID").document(userid).set(userInfo);
                        }
                    }
                });
                finish();   //삭제후 종료
            }
            //참여자 다이얼로그
            else if (dialog == myProjectJoinedPeopleDialog) {

            }
        }
    };

    //유동적으로 동적 뷰 관리
    @Override
    public void onResume() {

        //전체 진행도
        final LinearLayout totalAimLayout = findViewById(R.id.totalAimLinearLayout);

        DocumentReference docRef = db.collection(projectName).document("Progress").collection("TotalProgress").document("TotalList");

        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //전체 진행도 관리부분
                //데이터가 바뀔 때 마다 새로 그리기 위해 지움
                totalAimLayout.removeAllViews();

                //DB에서 전체 진행도 과정 받기
                final DBTodoList myTodoList = documentSnapshot.toObject(DBTodoList.class);

                //진행도 Custom View로 그리기
                String arr[] = myTodoList.getCountedStr().split("@");

                //DB에서 진행도 세부사항 받기
                final DBDetailList tempDetail = new DBDetailList();

                if (arr.length > 0 && !arr[0].equals("")) {
                    final ArrayList<String> list = new ArrayList<String>(Arrays.asList(arr));
                    final TodoListView[] myTodoListView = new TodoListView[arr.length];
                    TodoListView.LayoutParams myParams = new TodoListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    myParams.setMarginEnd(20);
                    for (int i = 0; i < arr.length; i++) {
                        myTodoListView[i] = new TodoListView(getApplicationContext());
                        myTodoListView[i].changeMode("view");
                        myTodoListView[i].setTotalTodoText(arr[i]);

                        DocumentReference tempDocRef = db.collection(projectName).document("Progress")
                                .collection("TotalProgress").document(arr[i]);

                        final int tempFinal = i;
                        tempDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot ds) {
                                DBDetailList tempDBDetialList;
                                String tempStr;
                                try {
                                    tempDBDetialList = ds.toObject(DBDetailList.class);
                                    tempStr = tempDBDetialList.getDetail1();
                                    tempDetail.setDetail1(tempStr);
                                    tempStr = tempDBDetialList.getDetail2();
                                    tempDetail.setDetail2(tempStr);
                                    tempStr = tempDBDetialList.getDetail3();
                                    tempDetail.setDetail3(tempStr);
                                    myTodoListView[tempFinal].setDetail_1_TodoTextView(tempDetail.getDetail1());
                                    myTodoListView[tempFinal].setDetail_2_TodoTextView(tempDetail.getDetail2());
                                    myTodoListView[tempFinal].setDetail_3_TodoTextView(tempDetail.getDetail3());
                                } catch (NullPointerException e) {
                                    Log.d("jinwoo/", "널객체!");
                                }
                            }
                        });

                        //편집부분 클릭이벤트 DB에 반영
                        myTodoListView[i].todoEditCheckBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String saved_String, saving_String;
                                TodoListView tempTodoListView = (TodoListView) view.getParent().getParent().getParent().getParent();
                                //수정할때 title 입력이 안되어있다.
                                if (tempTodoListView.validTotalTodo()) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "수정할 목표의 Title을 입력해 주세요.", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    //DB 전체 진행도에서 관리
                                    saved_String = tempTodoListView.getTotalTodoText();
                                    tempTodoListView.button_clicked("editCheck");
                                    saving_String = tempTodoListView.getTotalEditText();

                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).equals(saved_String)) {
                                            list.set(i, saving_String);
                                        }
                                    }

                                    String temp = list.stream().collect(Collectors.joining("@"));
                                    myTodoList.setCountedStr(temp);
                                    db.collection(projectName).document("Progress").
                                            collection("TotalProgress").document("TotalList").
                                            set(myTodoList);

                                    //DB 전체 진행도에서 세부사항 부분
                                    //세부진행도에 아무거나 바꿀때마다 저장후 삭제
                                    //데이터 저장 부분
                                    DocumentReference dataSaveDR = db.collection(projectName).document("Progress")
                                            .collection("TotalProgress").document(saved_String);
                                    dataSaveDR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot ds) {
                                            String tempStr;
                                            tempStr = myTodoListView[tempFinal].getDetail_1_TodoEditText();
                                            tempDetail.setDetail1(tempStr);
                                            tempStr = myTodoListView[tempFinal].getDetail_2_TodoEditText();
                                            tempDetail.setDetail2(tempStr);
                                            tempStr = myTodoListView[tempFinal].getDetail_3_TodoEditText();
                                            tempDetail.setDetail3(tempStr);
                                            db.collection(projectName).document("Progress").collection("TotalProgress").document(saving_String).set(tempDetail);
                                        }
                                    });
                                    //삭제부분
                                    if (!saved_String.equals(saving_String)) {
                                        db.collection(projectName).document("Progress").
                                                collection("TotalProgress").document(saved_String).
                                                delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("jinwoo/", "문서 삭제 성공");
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("jinwoo/", "문서 삭제 실패", e);
                                                    }
                                                });
                                    }

                                }
                            }
                        });
                        //삭제부분 클릭이벤트 DB에 반영 다이얼로그 띄운뒤 확인이 들어왔을때
                        myTodoListView[i].todoDeleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TodoListView tempTodoListView = (TodoListView) view.getParent().getParent().getParent().getParent();
                                final int tempCnt = myTodoList.getCnt();
                                final String deleteString = tempTodoListView.getTotalTodoText();

                                //다이얼로그 확인부분
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectManagementActivity.this);
                                builder.setTitle("경고: 정말 삭제하시겠습니까?");
                                builder.setMessage("삭제할 내용 : " + deleteString);

                                //확인버튼 취소버튼 아이콘 확인해보기
                                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        //DB에서 진행도부분 삭제
                                        int tmp = tempCnt;
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).equals(deleteString)) {
                                                list.remove(deleteString);
                                            }
                                        }
                                        String temp = list.stream().collect(Collectors.joining("@"));
                                        myTodoList.setCountedStr(temp);
                                        tmp--;
                                        myTodoList.setCnt(tmp);
                                        db.collection(projectName).document("Progress").
                                                collection("TotalProgress").document("TotalList").
                                                set(myTodoList);
                                        //DB에서 세부 진행도 삭제
                                        db.collection(projectName).document("Progress").
                                                collection("TotalProgress").document(deleteString).
                                                delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("jinwoo/", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("jinwoo/", "Error deleting document", e);
                                                    }
                                                });
                                    }
                                });
                                builder.setNegativeButton("취소", null);
                                builder.show();

                            }
                        });
                        totalAimLayout.addView(myTodoListView[i], myParams);
                    }
                }

                //전체 진행도 추가부분 DB에 반영
                final TodoListView addTodoListView = new TodoListView(getApplicationContext());
                //커스텀뷰의 버튼, TextVIew - EditText 전환
                addTodoListView.changeMode("add");
                addTodoListView.todoAddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //세부진행도 추가할부분 default값 = ""
                        String totalTemp = "";
                        String detail_1 = "", detail_2 = "", detail_3 = "";
                        DBDetailList dbDetailList = new DBDetailList();

                        //진행도 추가부분 Title 이 입력이 안되었을때
                        if (addTodoListView.validTotalTodo()) {
                            Toast toast = Toast.makeText(getApplicationContext(), "추가할 목표의 Title을 입력해 주세요.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            int tempCnt = myTodoList.getCnt();
                            String tempString = myTodoList.getCountedStr();

                            //추가하는 진행도 Title
                            totalTemp = addTodoListView.getTotalEditText();
                            //추가하는 데이터 넣어주기
                            //추가전 목표개수가 0개
                            if (tempCnt == 0) {
                                tempCnt++;
                                myTodoList.setCnt(tempCnt);
                                myTodoList.setCountedStr(totalTemp);
                                db.collection(projectName).document("Progress").
                                        collection("TotalProgress").document("TotalList").
                                        set(myTodoList);
                            }
                            //추가전 목표개수가 1개이상
                            else {
                                tempCnt++;
                                myTodoList.setCnt(tempCnt);
                                tempString = tempString.concat("@" + totalTemp).trim();
                                myTodoList.setCountedStr(tempString);
                                db.collection(projectName).document("Progress").
                                        collection("TotalProgress").document("TotalList").
                                        set(myTodoList);
                            }
                            detail_1 = addTodoListView.getDetail_1_TodoEditText();
                            detail_2 = addTodoListView.getDetail_2_TodoEditText();
                            detail_3 = addTodoListView.getDetail_3_TodoEditText();
                            dbDetailList.setDetail1(detail_1);
                            dbDetailList.setDetail2(detail_2);
                            dbDetailList.setDetail3(detail_3);
                            db.collection(projectName).document("Progress")
                                    .collection("TotalProgress").document(totalTemp).set(dbDetailList);
                        }
                    }
                });
                totalAimLayout.addView(addTodoListView);

            }
        });

        super.onResume();
    }
}
