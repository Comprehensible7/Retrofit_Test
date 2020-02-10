package com.tf.visit_retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import myutil.MyUtil;
import vo.VisitVo;

public class VisitInsertActivity extends AppCompatActivity {

    EditText et_name_input;
    EditText et_content_input;
    EditText et_pwd_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("방명록 글쓰기");
        setContentView(R.layout.activity_visit_insert);


        //각컨트롤의 참조값
        et_name_input       =   findViewById(R.id.et_name_input);
        et_content_input    =   findViewById(R.id.et_content_input);
        et_pwd_input        =   findViewById(R.id.et_pwd_input);




    }

    //등록
    public void onClickOk(View view){

        //입력값 받아오기
        String name     = et_name_input.getText().toString().trim();
        String content  = et_content_input.getText().toString().trim();
        String pwd      = et_pwd_input.getText().toString().trim();

        if(name.isEmpty()){
            MyUtil.showMessageDialog(this,"","이름을 입력하세요");
            et_name_input.setText("");   //값지우기
            et_name_input.requestFocus();//포커스지정
            return;
        }

        if(content.isEmpty()){
            MyUtil.showMessageDialog(this,"","내용을 입력하세요");
            et_content_input.setText("");   //값지우기
            et_content_input.requestFocus();//포커스지정
            return;
        }

        if(pwd.isEmpty()){
            MyUtil.showMessageDialog(this,"","비밀번호를 입력하세요");
            et_pwd_input.setText("");   //값지우기
            et_pwd_input.requestFocus();//포커스지정
            return;
        }

        //입력값 VO포장
        VisitVo vo = new VisitVo(name,content,pwd);

        //반환데이터를 넣어줄 Intent
        Intent intent = new Intent();
        intent.putExtra("vo",vo);

        //결과반환
        setResult(RESULT_OK,intent);

        finish();

    }


    //취소
    public void onClickCancel(View view){
        finish();
    }
}
