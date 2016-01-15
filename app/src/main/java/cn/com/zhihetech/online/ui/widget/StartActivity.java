package cn.com.zhihetech.online.ui.widget;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.xutils.view.annotation.ContentView;

import cn.com.zhihetech.online.R;

@ContentView(R.layout.activity_start)
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigatMainActivity();
    }

    private void navigatMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
