package com.custom.custombasebezier.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.custom.custombasebezier.R;
import com.custom.custombasebezier.customview.BaseBezier3;

public class BaseBezier3Activity extends AppCompatActivity {
    private Button mBezier3LeftBtn;
    private Button mBezier3RightBtn;
    private BaseBezier3 baseBezier3;
    private Button mBezier3SupBtn;

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
        setContentView(R.layout.basebezier3);
        baseBezier3 = findViewById(R.id.basebezie3);
        mBezier3SupBtn = findViewById(R.id.basebezier3_btn_sup);
        mBezier3LeftBtn = findViewById(R.id.basebezier3_btn_left);
        mBezier3RightBtn = findViewById(R.id.basebezier3_btn_right);
        mBezier3LeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBezier3.moveLeft();
            }
        });
        mBezier3RightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBezier3.moveRight();
            }
        });
        mBezier3SupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBezier3.setOnIsCleanSubListener(new BaseBezier3.onIsCleanSubListener() {
                    @Override
                    public void onCleanSub(Boolean isclean) {
                        if(isclean){
                            mBezier3SupBtn.setText("去除辅助线");
                        }else{
                            mBezier3SupBtn.setText("添加辅助线");
                        }

                    }
                });
                baseBezier3.iscleanSub();
            }
        });
    }
}
