package com.custom.custombasebezier.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.custom.custombasebezier.R;
import com.custom.custombasebezier.customview.WaveView;

public class WaveviewActivity extends AppCompatActivity {
    private Button mBtn;
    private WaveView waveView;

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
        setContentView(R.layout.waveview);
        mBtn = findViewById(R.id.waveview_btn);
        waveView = findViewById(R.id.waveView);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveView.setOnStart(new WaveView.onStartListener() {
                    @Override
                    public void onIsStart(boolean isStart) {
                        if (isStart) {
                            mBtn.setText("停止动画");
                            Log.e("isStart",isStart+"");
                        } else {
                            mBtn.setText("开始动画");
                            Log.e("isStart",isStart+"");
                        }
                    }
                });
                waveView.isStart();
            }
        });
    }
}
