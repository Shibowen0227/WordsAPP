package com.Shibunny.shi.bo.wen.wordsAPP;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import cn.edu.bistu.cs.se.mywordsapp.R;

public class English extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //输出到名为English的xml界面里面
        setContentView(R.layout.english);

        WebView webView=(WebView)findViewById(R.id.view);
        //为WebView赋值为English.xml中ID为view的webview
        webView.getSettings().setJavaScriptEnabled(true);
        //设置访问页面支持JavaScript脚本

        webView.setWebChromeClient(new WebChromeClient());
        //新闻页面加载的网址
        webView.loadUrl("http://news.baidu.com");


    }
}
