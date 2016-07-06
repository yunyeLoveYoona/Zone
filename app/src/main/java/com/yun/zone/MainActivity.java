package com.yun.zone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yun.zone.core.Zone;

import java.util.Date;
import java.util.List;

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
                testModel.save();
            }
        });

        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TestModel> testModels = (List<TestModel>) Zone.findAll(TestModel.class);
                Toast.makeText(MainActivity.this, "当前数据量：" + testModels.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
