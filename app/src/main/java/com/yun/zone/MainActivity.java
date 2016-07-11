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
                TestModel2 testModel = new TestModel2();
                testModel.age = i * 10;
                testModel.name = "test";
                testModel.birthday = new Date();
                testModel.test1 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashf,jkkjasfjajs:kfhaskfhka";
                testModel.test2 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test3 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test4 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test5 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test6 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test7 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test8 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test9 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test10 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test11 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test12 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test13 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test14 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test15 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test16 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";
                testModel.test17 = "adafskfjahfasjkfhafjajfhkasfhashfhsjafhashfjkkjasfjajskfhaskfhka";


                i = i + 1;
                try {
                    testModel.saveOrUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel2> testModels = null;
                try {
                    testModels = (List<TestModel2>) Zone.findAll(TestModel2.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (testModels != null && testModels.size() > 0) {
                    testModels.get(testModels.size() - 1).delete();
                }
                try {
                    testModels = (List<TestModel2>) Zone.findAll(TestModel2.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "删除成功，当前数据量：" + testModels.size(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel2> testModels = null;
                try {
                    testModels = (List<TestModel2>) Zone.findAll(TestModel2.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "当前数据量：" + testModels.size(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel2> testModels = null;
                try {
                    testModels = (List<TestModel2>) Zone.findAll(TestModel2.class);
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
                List<TestModel2> testModels = null;
                try {
                    testModels = (List<TestModel2>) where(WhereCondition.MORE_THAN, TestModel2.class, "age", 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (testModels != null) {
                    Toast.makeText(MainActivity.this, "查询到的数据量：" + testModels.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.changeUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Zone.init(MainActivity.this, "default");
                Toast.makeText(MainActivity.this, "切换到用户：test", Toast.LENGTH_SHORT).show();

            }
        });


        findViewById(R.id.sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel2> testModels = null;
                try {
                    testModels = (List<TestModel2>) Zone.orderBy(TestModel2.class, "age", "DESC");
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
