package com.gaolei.dagger_one;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gaolei.dagger2.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_01)
    Button btn01;

    @Inject
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //新添代码
        DaggerSampleComponent.builder()
                .sampleModule(new SampleModule())
                .build()
                .inject(this);
    }

    @OnClick(R.id.btn_01)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_01:
                Toast.makeText(this, student.toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
