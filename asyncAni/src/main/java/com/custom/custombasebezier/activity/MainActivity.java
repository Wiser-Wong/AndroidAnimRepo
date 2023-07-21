package com.custom.custombasebezier.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.custom.custombasebezier.R;

public class MainActivity extends AppCompatActivity {
    private Button mBaseBezier2Btn;
    private Button mBaseBezier3Btn;
    private Button mWaveViewBtn;
    private Button mPointBeatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//版本判断
            Window window = getWindow();
// Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置statusbar应用所占的屏幕扩大到全屏，但是最顶上会有背景透明的状态栏，它的文字可能会盖着你的应用的标题栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBaseBezier2Btn = findViewById(R.id.main_btn_basebezier2);
        mBaseBezier3Btn = findViewById(R.id.main_btn_basebezier3);
        mWaveViewBtn = findViewById(R.id.main_btn_waveview);
        mBaseBezier2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BaseBezier2Activity.class);
                startActivity(intent);

            }
        });
        mWaveViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WaveviewActivity.class);
                startActivity(intent);

            }
        });
        mBaseBezier3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BaseBezier3Activity.class);
                startActivity(intent);
            }
        });
        mPointBeatBtn = findViewById(R.id.main_btn_pointbeat);
        mPointBeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PointbeatActivity.class);
                startActivity(intent);
            }
        });
    }
}
