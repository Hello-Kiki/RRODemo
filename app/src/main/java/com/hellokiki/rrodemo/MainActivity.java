package com.hellokiki.rrodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hellokiki.rrodemo.down.DownActivity;
import com.hellokiki.rrorequest.HttpManager;
import com.hellokiki.rrorequest.SimpleCallBack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button_goto_down).setOnClickListener(this);

        mTextView= (TextView) findViewById(R.id.text_view);

//        HttpManager.baseUrl("http://zd.gzrcqf.com");
        HttpManager.baseUrl("http://192.168.137.147:8080");

    }


    public void request(){

        HttpManager.getInstance().create(ApiService.class).getData()
                .compose(HttpManager.<JsonObject>applySchedulers())
                .subscribe(new SimpleCallBack<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Log.e("2017","成功-->"+jsonObject.toString());
                        mTextView.setText(jsonObject.toString());
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e("2017","失败-->"+e.toString());
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                request();
                break;
            case R.id.button_goto_down:
                Intent intent=new Intent(this,DownActivity.class);
                startActivity(intent);
                break;
        }

    }
}
