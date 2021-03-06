package com.hellokiki.rrodemo.down;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hellokiki.rrodemo.R;
import com.hellokiki.rrorequest.down.DownInfo;
import com.hellokiki.rrorequest.down.DownState;
import com.hellokiki.rrorequest.down.HttpDownListener;
import com.hellokiki.rrorequest.down.HttpDownManager;

/**
 * Created by 黄麒羽 on 2017/12/22.
 */

public class DownItemView extends LinearLayout implements View.OnClickListener {

    private ProgressBar mProgressBar;
    private TextView mTextViewProgress;
    private Button mButtonDown;

    private DownInfo mDownInfo;
    private HttpDownManager manager;

    public DownItemView(Context context) {
        this(context,null);
    }

    public DownItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DownItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_down_item_view,this);

        mProgressBar= (ProgressBar) findViewById(R.id.progress);
        mTextViewProgress= (TextView) findViewById(R.id.text_view_progress);
        mButtonDown= (Button) findViewById(R.id.button_down);
        mButtonDown.setOnClickListener(this);
        findViewById(R.id.button_shop).setOnClickListener(this);

        manager=HttpDownManager.getInstance();
    }

    public void setDownInfo(DownInfo info){
        mDownInfo=info;
    }


    public void startDown(){
        if(mDownInfo==null||"".equals(mDownInfo.getUrl())){
            return;
        }

        manager.start(mDownInfo, new HttpDownListener() {
            @Override
            public void onStart() {
                Log.e("2017","onStart");
                mButtonDown.setText("暂停");
            }

            @Override
            public void onPause(long read) {
                mDownInfo.setReadLength(read);
                mButtonDown.setText("继续");
                mTextViewProgress.setText("下载暂停");
            }

            @Override
            public void onStop(long read) {
                mDownInfo.setReadLength(read);
                mButtonDown.setText("下载");
                mTextViewProgress.setText("下载停止");
            }

            @Override
            public void onFinish(DownInfo info) {
                Log.e("2017","onFinish");
                mDownInfo=info;
                mButtonDown.setText("下载");
                mTextViewProgress.setText("下载成功");
            }

            @Override
            public void onError(DownInfo info,String s) {
                Log.e("2017","onError="+s.toString());
                mDownInfo=info;
                mButtonDown.setText("下载");
                mTextViewProgress.setText("下载失败");
            }

            @Override
            public void onProgress(long currentRead, long addLength) {
                int pro=(int)(currentRead*100/addLength);
                mProgressBar.setProgress(pro);
                mTextViewProgress.setText("下载中："+pro+"%");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_down:
                startDown();
                break;
            case R.id.button_shop:
                manager.stop(mDownInfo);
                break;
        }
    }
}
