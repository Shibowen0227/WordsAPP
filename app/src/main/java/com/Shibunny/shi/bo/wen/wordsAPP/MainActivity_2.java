package com.Shibunny.shi.bo.wen.wordsAPP;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.bistu.cs.se.mywordsapp.R;

public class MainActivity_2 extends AppCompatActivity {
    private TextView tips;
    private EditText editText;
    private WebView webView;
    private Handler tHandler = new Handler();
    private Button b_return;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webView=(WebView)findViewById(R.id.btview);
        //为webView赋值为图形中id为btview的控件
        final Button submit=(Button)findViewById(R.id.bttr);
        //为submit赋值为图形中id为bttr的控件
        editText=(EditText)findViewById(R.id.btput);
        //为editText赋值为图形中id为的控件
        b_return = (Button)findViewById(R.id.btr);
        //为b_return赋值为图形中id为btr的控件

        b_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_2.this, MainActivity.class);
                //传递给ManiActivity_2以及MainActivity类
                startActivity(intent);
            }
        });
        //为网页做的设置
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //允许JavaScript使用
        webSettings.setSaveFormData(false);
        //不允许保存表单数据
        webSettings.setSupportZoom(false);
        //不允许支持缩放
        submit.setOnClickListener(new OnClickListener() {
            @Override
            //翻译用的接口
            public void onClick(View v) {
                webView.setVisibility(WebView.VISIBLE);
                tHandler.post(new Runnable(){
                    public void run(){
                        if (editText.getText().toString() != ""){
                            webView.loadUrl("http://dict.cn/mini.php?q="+ editText.getText().toString());
                        }
                    }
                });
            }
        });
    }
}