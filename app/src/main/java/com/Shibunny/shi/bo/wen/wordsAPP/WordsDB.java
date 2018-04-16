package com.Shibunny.shi.bo.wen.wordsAPP;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.Shibunny.shi.bo.wen.wordsAPP.wordcontract.Words;

/**
 * 增加新单词及其含义等
 * 删单词
 * 改单词
 * 查单词
 */
public class WordsDB {
    private static final String TAG = "myTag";

    private static WordsDBHelper mDbHelper;

    //采用单例模式
    private static WordsDB instance=new WordsDB();
    public static WordsDB getWordsDB(){
        return WordsDB.instance;
    }

    private WordsDB() {
        if (mDbHelper == null) {
            mDbHelper = new WordsDBHelper(WordsApplication.getContext());
        }
    }


    public void close() {
        if (mDbHelper != null)
            mDbHelper.close();
    }

    //获得单个单词的全部信息
    public Words.WordDescription getSingleWord(String id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //sql查询语句select * from 表名 where 筛选条件（此处条件为_ID）
        String sql = "select * from words where _ID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            ;
            Words.WordDescription item = new Words.WordDescription(
                    //获取各个表段信息
                    cursor.getString(cursor.getColumnIndex(Words.Word._ID)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)),
                    cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)));
            return item;
        }
        return null;

    }

    //得到全部单词列表
    public ArrayList<Map<String, String>> getAllWords() {
        if (mDbHelper == null) {
            Log.v(TAG, "WordsDB::getAllWords()");
            return null;
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD
        };

        //排序
        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " DESC";


        Cursor c = db.query(
                Words.Word.TABLE_NAME,  //要查询的表
                projection,                               //要返回的列
                null,                                // 返回where子句的列
                null,                            // 返回where子句的值
                null,                                     // 不要分组行
                null,                                     // 不按行组过滤
                sortOrder                                 // 排序顺序
        );

        return ConvertCursor2WordList(c);
    }



    //将游标转化为单词列表
    private ArrayList<Map<String, String>> ConvertCursor2WordList(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getString(cursor.getColumnIndex(Words.Word._ID))));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)));
            result.add(map);
        }
        return result;
    }

    //使用Sql语句插入单词
    public void InsertUserSql(String strWord, String strMeaning, String strSample) {
        //insert语句插入数据格式如下insert into  表名(_id,word,meaning,sample) values值(?,?,?,?)
        String sql = "insert into  words(_id,word,meaning,sample) values(?,?,?,?)";

        ///以写入模式获取数据存储库
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql, new String[]{GUID.getGUID(),strWord, strMeaning, strSample});
    }

    //使用insert方法增加单词
    public void Insert(String strWord, String strMeaning, String strSample) {

        //以写入模式获取数据存储库。
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //创建一个新的值映射，其中列名是键
        ContentValues values = new ContentValues();
        values.put(Words.Word._ID, GUID.getGUID());
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

        //插入新行，返回新行的主键值。
        long newRowId;
        newRowId = db.insert(
                Words.Word.TABLE_NAME,
                null,
                values);
    }


    //使用Sql语句删除单词
    public void DeleteUseSql(String strId) {
        String sql = "delete from words where _id='" + strId + "'";

        //以写入模式获取数据存储库。
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }

    //删除单词
    public void Delete(String strId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        String selection = Words.Word._ID + " = ?";

        // 指定占位符对应的实际参数
        String[] selectionArgs = {strId};

        // 发出的SQL语句
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);
    }


    //使用Sql语句更新单词
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord,strMeaning, strSample, strId});
    }

    //使用方法更新
    public void Update(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};

        int count = db.update(
                Words.Word.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    //使用Sql语句查找
    public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "select * from words where word like ? order by word desc";
        Cursor c = db.rawQuery(sql, new String[]{"%" + strWordSearch + "%"});

        return ConvertCursor2WordList(c);
    }

    //使用query方法查找
    public ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD
        };

        String sortOrder =
                Words.Word._ID + " DESC";

        String selection = Words.Word._ID + " LIKE ?";
        String[] selectionArgs = {"%" + strWordSearch + "%"};

        Cursor c = db.query(
                Words.Word.TABLE_NAME,  // 要查询的表
                projection,                               // 要返回的列
                selection,                                // WHERE子句的列
                selectionArgs,                            // WHERE子句的值
                null,                                     // 不分组行
                null,                                     // 不按照行组进行过滤
                sortOrder                                 // 排序
        );

        return ConvertCursor2WordList(c);
    }


}
