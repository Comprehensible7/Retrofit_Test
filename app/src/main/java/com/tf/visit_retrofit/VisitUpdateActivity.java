package com.tf.visit_retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import myutil.MyUtil;
import vo.VisitVo;

public class VisitUpdateActivity extends AppCompatActivity {

    EditText et_name_update;
    EditText et_content_update;
    EditText et_pwd_update;

    VisitVo vo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("방명록 수정하기");
        setContentView(R.layout.activity_visit_update);
        //컨트롤 참조값
        et_name_update      = findViewById(R.id.et_name_update);
        et_content_update   = findViewById(R.id.et_content_update);
        et_pwd_update       = findViewById(R.id.et_pwd_update);
        //수정할 정보 얻기
        vo  = (VisitVo)getIntent().getSerializableExtra("vo");

        //원본데이터 넣기
        et_name_update.setText(vo.getName());
        String content = vo.getContent().replaceAll("<br>","\n");
        et_content_update.setText(content);
        et_pwd_update.setText(vo.getPwd());
    }

    public void onClickOk(View view){
        //입력값 받아오기
        String name     = et_name_update.getText().toString().trim();
        String content  = et_content_update.getText().toString().trim();
        String pwd      = et_pwd_update.getText().toString().trim();

        if(name.isEmpty()){
            MyUtil.showMessageDialog(this,"","이름을 입력하세요");
            et_name_update.setText("");   //값지우기
            et_name_update.requestFocus();//포커스지정
            return;
        }

        if(content.isEmpty()){
            MyUtil.showMessageDialog(this,"","내용을 입력하세요");
            et_content_update.setText("");   //값지우기
            et_content_update.requestFocus();//포커스지정
            return;
        }

        if(pwd.isEmpty()){
            MyUtil.showMessageDialog(this,"","비밀번호를 입력하세요");
            et_pwd_update.setText("");   //값지우기
            et_pwd_update.requestFocus();//포커스지정
            return;
        }

        //수정값 VO셋팅
        vo.setName(name);
        vo.setContent(content);
        vo.setPwd(pwd);


        //반환데이터를 넣어줄 Intent
        Intent intent = new Intent();
        intent.putExtra("vo",vo);

        //결과반환
        setResult(RESULT_OK,intent);

        finish();
    }

    public void onClickCancel(View view){

        setResult(RESULT_CANCELED);
        finish();
    }



}
