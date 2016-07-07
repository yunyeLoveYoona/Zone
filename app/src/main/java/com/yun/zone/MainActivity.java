package com.yun.zone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yun.zone.core.Zone;
import com.yun.zone.core.Zone.WhereCondition;

import java.util.Date;
import java.util.List;

import static com.yun.zone.core.Zone.where;

public class MainActivity extends AppCompatActivity {

    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestModel testModel = new TestModel();
                testModel.age = i * 10;
                testModel.name = "test";
                testModel.birthday = new Date();
                i = i + 1;
                try {
                    testModel.saveOrUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel> testModels = null;
                try {
                    testModels = (List<TestModel>) Zone.findAll(TestModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "当前数据量：" + testModels.size(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel> testModels = null;
                try {
                    testModels = (List<TestModel>) Zone.findAll(TestModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (testModels.size() > 0) {
                    testModels.get(testModels.size() - 1).name = "updateModel";
                    try {
                        testModels.get(testModels.size() - 1).saveOrUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                }
            }
        });


        findViewById(R.id.where).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel> testModels = null;
                try {
                    testModels = (List<TestModel>) where(WhereCondition.MORE_THAN, TestModel.class, "age", 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (testModels != null) {
                    Toast.makeText(MainActivity.this, "查询到的数据量：" + testModels.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel> testModels = null;
                try {
                    testModels = (List<TestModel>) Zone.orderBy(TestModel.class, "age", "DESC");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (testModels != null) {
                    Toast.makeText(MainActivity.this, "查询到的数据量：" + testModels.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
