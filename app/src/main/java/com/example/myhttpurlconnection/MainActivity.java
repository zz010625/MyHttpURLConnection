package com.example.myhttpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //GET
        MyHttpURLConnection GETConnection = new MyHttpURLConnection.Builder()
                .url("http://www.baidu.com")
                .connectTimeout(8000)
                .readTimeout(8000)
                .build();
        GETConnection.setCallBack(new MyHttpURLConnection.CallBack() {
            @Override
            public void onResponse(String result) {
                Log.d("zz","GET成功"+result);
//                Toast.makeText(MainActivity.this,"GET",Toast.LENGTH_SHORT).show(); 回调方法是在子线程运行 因此不能Toast
                //在主线程中运行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"GET",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure() {
                Log.d("zz","GET失败");
            }
        }).start();

        //POST
        MyHttpURLConnection POSTConnection = new MyHttpURLConnection.Builder()
                .url("https://www.wanandroid.com/user/login")
                .requestMethod("POST")
                .connectTimeout(8000)
                .readTimeout(8000)
                .setPostData("username=zz147258&password=123456")
                .build();
        POSTConnection.setCallBack(new MyHttpURLConnection.CallBack() {
            @Override
            public void onResponse(String result) {
                Log.d("zz","POST成功"+result);
            }

            @Override
            public void onFailure() {
                Log.d("zz","POST失败");

            }
        }).start();
    }
}