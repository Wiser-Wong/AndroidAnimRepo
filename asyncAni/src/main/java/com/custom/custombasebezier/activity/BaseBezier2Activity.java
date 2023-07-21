package com.custom.custombasebezier.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.custom.custombasebezier.R;
import com.custom.custombasebezier.customview.BaseBezier2;
import com.custom.custombasebezier.customview.BaseBezier3;

public class BaseBezier2Activity extends AppCompatActivity {
    private Button mBezier2SubBtn;
    private BaseBezier2 baseBezier2;

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
        setContentView(R.layout.basebezier2);
        baseBezier2 = findViewById(R.id.baseBezier2);
        mBezier2SubBtn =findViewById(R.id.basebezier2_btn_sup);
        mBezier2SubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseBezier2.setOnIsCleanSubListener(new BaseBezier2.onIsCleanSubListener2() {
                    @Override
                    public void onCleanSub(Boolean isclean) {
                        if(isclean){
                            mBezier2SubBtn.setText("去除辅助线");
                        }else{
                            mBezier2SubBtn.setText("添加辅助线");
                        }

                    }
                });
                baseBezier2.iscleanSub();
            }
        });
    }
}
