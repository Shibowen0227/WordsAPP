package com.Shibunny.shi.bo.wen.wordsAPP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

//import java.util.logging.Handler;

import com.Shibunny.shi.bo.wen.wordsAPP.wordcontract.Words;

import cn.edu.bistu.cs.se.mywordsapp.R;

public class MainActivity extends AppCompatActivity implements WordItemFragment.OnFragmentInteractionListener, WordDetailFragment.OnFragmentInteractionListener {

    private static final String TAG = "myTag";
    //private -  只能在当前类访问的
    //static  -  静态全局变量
    //final   -  一旦赋值不可更改
    //String  -  字符串类型
    //TAG     -  标签
    //将TAG赋值为 myTag


    private TextView textView=null;
    //将Textview 赋值为空


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //调用父类
        setContentView(R.layout.activity_main);
        //显示activity_main界面

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar 为界面上ID为toolbar的按钮

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //找到xml界面上Id为fab的按钮也就是主页面上的加号

        fab.setOnClickListener(new View.OnClickListener() {
            //当fab被点击触发的事件
            @Override
            public void onClick(View view) {
                //新增单词
                InsertDialog();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        WordsDB wordsDB=WordsDB.getWordsDB();
        //如果wordsDB不为空则wordsDB关闭
        if (wordsDB != null)
            wordsDB.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 展开菜单；如果菜单出现，则将项添加到动作栏中。
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        //返回值为真
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       //在指定的xml页面上,点击这里处理动作栏 动作栏将处理在对应键上
        int id = item.getItemId();
        //判断选择的哪个按钮
        switch (id) {
            case R.id.action_search:
                //选择的是查找按钮
                SearchDialog();
                //调用SearchDialog
                return true;
            case R.id.action_insert:
                //选择的是新增单词按钮
                InsertDialog();
                //使用InsertDialog
                return true;
            //返回值为真
            case R.id.action_traslate:
                //选择的是翻译按钮
                Intent intent=new Intent(MainActivity.this,MainActivity_2.class);
                //转为翻译的页面并且调用,MainActivity_2类
                startActivity(intent);
                break;
            case R.id.action_we:
                //选择的是新闻按钮
                Intent intentNews = new Intent(MainActivity.this,English.class);
                //转为新闻页面调用English类
                startActivity(intentNews);
                return true;
                //返回值为真
        }
        return super.onOptionsItemSelected(item);
    }


 //更新单词的列表
    private void RefreshWordItemFragment() {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList();
    }

//更新单词的列表
    private void RefreshWordItemFragment(String strWord) {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList(strWord);
    }

    //新增对话框
    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        //弹框
        new AlertDialog.Builder(this)
                .setTitle("新增单词")//标题
                .setView(tableLayout)//设置视图
                        //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        //strWord赋值为对话框中输入框txtWord的值
                        String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        //strMeaning赋值为对话框中输入框txtMeaning的值
                        String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        //strSample赋值为为对话框中输入框txtSample的值


                        //将弹框中的内容插入数据库，既可以使用Sql语句插入，也可以使用使用insert方法插入
                        // insert格式为:InsertUserSql(strWord, strMeaning, strSample);
                        WordsDB wordsDB=WordsDB.getWordsDB();
                        wordsDB.Insert(strWord, strMeaning, strSample);

                        //单词已经插入到数据库，更新显示列表
                        RefreshWordItemFragment();


                    }
                })
                        //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }

    //删除框
    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //既可以使用Sql语句删除，也可以使用使用delete方法删除
                WordsDB wordsDB=WordsDB.getWordsDB();
                wordsDB.DeleteUseSql(strId);

                //单词已经删除，更新显示列表
                RefreshWordItemFragment();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }


    //修改对话框
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        //显示出之前添加单词的样式
        ((EditText) tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        //单词
        ((EditText) tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        //含义
        ((EditText) tableLayout.findViewById(R.id.txtSample)).setText(strSample);
        //例子
        new AlertDialog.Builder(this)
                .setTitle("修改单词")//标题
                .setView(tableLayout)//设置视图
                        //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        //赋值为新的txtWord
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        //赋值为新的txtMeaning
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        //赋值为新的txtSample

                        //更新数据库中对应的单词,既可以使用Sql语句更新，也可以使用使用update方法更新
                        WordsDB wordsDB=WordsDB.getWordsDB();
                        wordsDB.UpdateUseSql(strId, strWord, strNewMeaning, strNewSample);

                        //单词已经更新，更新显示列表
                        RefreshWordItemFragment();
                    }
                })
                        //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }


    //查找对话框
    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.searchterm, null);
        new AlertDialog.Builder(this)
                .setTitle("查找单词")//标题
                .setView(tableLayout)//设置视图
                        //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord = ((EditText) tableLayout.findViewById(R.id.txtSearchWord)).getText().toString();
                        //txtSearchWord 赋值为 在txtSearchWord输出的单词
                        //单词已经插入到数据库，更新显示列表
                        RefreshWordItemFragment(txtSearchWord);
                    }
                })
                        //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框

    }
     //当用户在单词详细Fragment中单击时回调此函数
    @Override
    public void onWordDetailClick(Uri uri) {

    }
     //在单词列表Fragment中单击某个单词时回调此函数
     //判断如果横屏的话，则需要在右侧单词详细Fragment中显示
    @Override
    public void onWordItemClick(String id) {

        if(isLand()) {
            //横屏的话则在右侧的WordDetailFragment中显示单词详细信息
            ChangeWordDetailFragment(id);
        }else{
            Intent intent = new Intent(MainActivity.this,WordDetailActivity.class);
            intent.putExtra(WordDetailFragment.ARG_ID, id);
            startActivity(intent);
        }

    }

    //是否是横屏
    private boolean isLand(){

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
            //读取系统资源函数获取屏幕方向
            //Configuration.ORIENTATION_LANDSCAPE 表示横屏
            //Configuration.ORIENTATION_PORTRAIT表示竖屏
            return true;
            //如果为横屏返回true
        return false;
        //反之返回false
    }
        //前面调用的ChangeWordDetailFragment
    private void ChangeWordDetailFragment(String id){
        Bundle arguments = new Bundle();
        arguments.putString(WordDetailFragment.ARG_ID, id);
        Log.v(TAG, id);
        WordDetailFragment fragment = new WordDetailFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction().replace(R.id.worddetail, fragment).commit();
    }

    @Override
    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
    }

    @Override
    public void onUpdateDialog(String strId) {
        WordsDB wordsDB=WordsDB.getWordsDB();
        if (wordsDB != null && strId != null) {
        //如果wordsDB不为空且strID也不为空

            Words.WordDescription item = wordsDB.getSingleWord(strId);
            if (item != null) {
                UpdateDialog(strId, item.word, item.meaning, item.sample);
                //更新对话框的值strID，word，meaning，sample
            }

        }

    }







}
