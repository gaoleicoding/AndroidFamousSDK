package com.gaolei.greendao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gaolei.greendao.db.DBManager;
import com.gaolei.greendao.db.SearchRecord;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DBManager dbManager = DBManager.getInstance(MainActivity.this);
        for (int i = 0; i < 10; i++) {
            SearchRecord searchRecord = new SearchRecord();
            searchRecord.setName("测试数据" + i);
            dbManager.insertUser(searchRecord);
        }

        List<SearchRecord> recordList = DBManager.getInstance(this).queryUserList();

        Toast.makeText(this, "数据条数：" + recordList.size(), Toast.LENGTH_SHORT).show();
        ;
    }
}
