package net.tool.practical.filemanagerplus.free.cosyfile.Activity;

import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.IMAGE_PATH;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.IMAGE_TAG;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.MY_TAG;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.VIDEO_PATH;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.VIDEO_TAG;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.ZIP_PATH;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.ZIP_TAG;
import static net.tool.practical.filemanagerplus.free.cosyfile.util.FileUtil.getInfo;
import static net.tool.practical.filemanagerplus.free.cosyfile.util.StorageUtils.getTotalStorage;
import static net.tool.practical.filemanagerplus.free.cosyfile.util.StorageUtils.getUsedStorage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.tool.practical.filemanagerplus.free.cosyfile.R;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ImagePojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.VideoPojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ZipPojo;
import net.tool.practical.filemanagerplus.free.cosyfile.util.FileUtil;

import java.util.ArrayList;
import java.util.Timer;

public class MainAct extends AppCompatActivity {
    private ProgressBar scanPr;
    private Timer timer;
    private int count = 0;
    private TextView capacity,image_count,video_count,zip_count,start_text;
    private String isUsd;
    private String all;
    private Handler handler = new Handler();
    private static  int INTERVAL = 100;
    private RelativeLayout image_layout,video_layout,zip_layout;

    private Runnable timerRunnable = new Runnable() {
        // 每隔100毫秒执行一次

        @Override
        public void run() {
            // 在这里执行计时器任务的逻辑
            count++;
            scanPr.setProgress(count);

            if (count <= 100) {
                handler.postDelayed(this, INTERVAL); // 延迟指定的时间后再次执行任务
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
    }
    public void initUI(){
        scanPr = findViewById(R.id.scanPr);
        capacity = findViewById(R.id.capacity);
        image_count = findViewById(R.id.image_count);
        video_count = findViewById(R.id.video_count);
        zip_count = findViewById(R.id.zip_count);
        image_layout = findViewById(R.id.image_layout);
        video_layout = findViewById(R.id.video_layout);
        zip_layout = findViewById(R.id.zip_ly);
        start_text = findViewById(R.id.start_text);

        scanPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
    public void initListener(){
        image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMKV.defaultMMKV().encode(MY_TAG,IMAGE_TAG);
                startManagement();
            }
        });
        video_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMKV.defaultMMKV().encode(MY_TAG,VIDEO_TAG);
                startManagement();
            }
        });
        zip_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMKV.defaultMMKV().encode(MY_TAG,ZIP_TAG);
                startManagement();
            }
        });

    }

    public void startManagement(){
        Intent intent = new Intent(MainAct.this, ManagementActivity.class);
        startActivity(intent);
    }
    @SuppressLint("SetTextI18n")
    public void initData() {
        start_text.setText("Scanning");
        count = 0;
        isUsd = getUsedStorage();
        all = getTotalStorage();

        capacity.setText(isUsd + " / " + all);

        startTimer();
        getInfo(this, new FileUtil.QueryFinish() {
            @Override
            public void searchResult(ArrayList<ImagePojo> imageList, ArrayList<VideoPojo> videoList, ArrayList<ZipPojo> zipList) {
                runOnUiThread(()->{
                    int imageCount = imageList.size();
                    int videoCount = videoList.size();
                    int zipCount = zipList.size();
                    image_count.setText(String.valueOf(imageCount));
                    video_count.setText(String.valueOf(videoCount));
                    zip_count.setText(String.valueOf(zipCount));

                    String imageGs = new Gson().toJson(imageList);
                    String videoGs = new Gson().toJson(videoList);
                    String zipGs = new Gson().toJson(zipList);

                    MMKV.defaultMMKV().encode(IMAGE_PATH,imageGs);
                    MMKV.defaultMMKV().encode(VIDEO_PATH,videoGs);
                    MMKV.defaultMMKV().encode(ZIP_PATH,zipGs);

                    stopTimer();
                    INTERVAL = 10;
                    startTimer();
                    start_text.setText("Start Scan");
                });

            }
        });
    }
    // 启动计时器
    private void startTimer() {

        handler.postDelayed(timerRunnable, 0);
    }

    // 停止计时器
    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}