package net.tool.practical.filemanagerplus.free.cosyfile.Activity;

import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.IMAGE_PATH;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.IMAGE_TAG;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.MY_TAG;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.VIDEO_PATH;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.VIDEO_TAG;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.ZIP_PATH;
import static net.tool.practical.filemanagerplus.free.cosyfile.constant.Constant.ZIP_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import net.tool.practical.filemanagerplus.free.cosyfile.R;
import net.tool.practical.filemanagerplus.free.cosyfile.adapter.ImageAdapter;
import net.tool.practical.filemanagerplus.free.cosyfile.adapter.VideoAdapter;
import net.tool.practical.filemanagerplus.free.cosyfile.adapter.ZipAdapter;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ImagePojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.VideoPojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ZipPojo;

import java.io.File;
import java.util.ArrayList;

public class ManagementActivity extends AppCompatActivity {

    private RecyclerView my_recyclerView;
    private ImageButton bt_back,bt_delete;
    private TextView select_count,title_text;
    private String tag = "";
    private ImageAdapter imageAdapter;
    private VideoAdapter videoAdapter;
    private ZipAdapter zipAdapter;

    private boolean firstCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        initUI();
        initData();
        initListener();
    }
    public void initUI(){
        my_recyclerView = findViewById(R.id.my_recyclerView);
        bt_back = findViewById(R.id.bt_back);
        bt_delete = findViewById(R.id.bt_delete);
        select_count = findViewById(R.id.select_item);
        title_text = findViewById(R.id.title_text);

    }
    public void initData(){
        tag = MMKV.defaultMMKV().decodeString(MY_TAG);

        if (tag!=null&&!tag.equals("")){
            switch (tag){
                case IMAGE_TAG:
                    title_text.setText("Photo");
                    String image_list = MMKV.defaultMMKV().decodeString(IMAGE_PATH);
                    ArrayList<ImagePojo> imageList = new Gson().fromJson(image_list, new TypeToken<ArrayList<ImagePojo>>() {}.getType());
                    startImageAdapter(imageList);
                    break;
                case VIDEO_TAG:
                    title_text.setText("Video");
                    String video_list = MMKV.defaultMMKV().decodeString(VIDEO_PATH);
                    ArrayList<VideoPojo> videoList = new Gson().fromJson(video_list, new TypeToken<ArrayList<VideoPojo>>() {}.getType());
                    startVideoAdapter(videoList);
                    break;
                case ZIP_TAG:
                    title_text.setText("Zip");
                    String zip_list = MMKV.defaultMMKV().decodeString(ZIP_PATH);
                    ArrayList<ZipPojo> zipList = new Gson().fromJson(zip_list, new TypeToken<ArrayList<ZipPojo>>() {}.getType());
                    startZipAdapter(zipList);
                    break;
            }
        }
    }
    public void initListener(){
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tag!=null&&!tag.equals("")){
                    switch (tag){
                        case IMAGE_TAG:
                            if (imageAdapter.getSelect_count()!=0){
                                ImageDelete();
                            }
                            break;
                        case VIDEO_TAG:
                            if (videoAdapter.getSelect_count()!=0){
                                VideoDelete();
                            }
                            break;
                        case ZIP_TAG:
                            if (zipAdapter.getSelect_count()!=0){
                                ZipDelete();
                            }

                            break;
                    }
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void startImageAdapter(ArrayList<ImagePojo> imageList){
// 创建GridLayoutManager，并设置每行显示的列数为2
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

// 设置RecyclerView的布局管理器为GridLayoutManager
        my_recyclerView.setLayoutManager(layoutManager);

// 创建适配器并设置给RecyclerView
        imageAdapter = new ImageAdapter(this, imageList, new ImageAdapter.ItemClickListener() {
            @Override
            public void onClick() {
                int itemCount = imageAdapter.getSelect_count();
                select_count.setText(itemCount+" Item");
                if (firstCheck){
                    firstCheck = false;
                    ReadyDelete();
                }

            }

            @Override
            public void Deselect() {
                firstCheck = true;
                cancelDelete();
            }
        });
        my_recyclerView.setAdapter(imageAdapter);
    }

    public void startVideoAdapter(ArrayList<VideoPojo> videoPojos){
// 创建GridLayoutManager，并设置每行显示的列数为2
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

// 设置RecyclerView的布局管理器为GridLayoutManager
        my_recyclerView.setLayoutManager(layoutManager);

// 创建适配器并设置给RecyclerView
        videoAdapter = new VideoAdapter(this, videoPojos, new VideoAdapter.ItemClickListener() {
            @Override
            public void onClick() {
                int itemCount = videoAdapter.getSelect_count();
                select_count.setText(itemCount+" Item");
                if (firstCheck){
                    firstCheck = false;
                    ReadyDelete();
                }

            }

            @Override
            public void Deselect() {
                firstCheck = true;
                cancelDelete();
            }
        });
        my_recyclerView.setAdapter(videoAdapter);
    }

    public void startZipAdapter(ArrayList<ZipPojo> zipPojos){
// 创建GridLayoutManager，并设置每行显示的列数为2
        System.out.println(zipPojos.size());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

// 设置RecyclerView的布局管理器为GridLayoutManager
        my_recyclerView.setLayoutManager(layoutManager);

// 创建适配器并设置给RecyclerView
        zipAdapter = new ZipAdapter(this, zipPojos, new ZipAdapter.ItemClickListener() {
            @Override
            public void onClick() {
                int itemCount = zipAdapter.getSelect_count();
                select_count.setText(itemCount+" Item");
                if (firstCheck){
                    firstCheck = false;
                    ReadyDelete();
                }

            }

            @Override
            public void Deselect() {
                firstCheck = true;
                cancelDelete();
            }
        });
        my_recyclerView.setAdapter(zipAdapter);
    }
    public void ReadyDelete(){

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);

        select_count.startAnimation(fadeIn);
        bt_delete.startAnimation(fadeIn);
        select_count.setVisibility(View.VISIBLE);
        bt_delete.setVisibility(View.VISIBLE);


    }

    public void cancelDelete(){
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(500);
        fadeOut.setFillAfter(true);

        select_count.startAnimation(fadeOut);
        bt_delete.startAnimation(fadeOut);
        select_count.setVisibility(View.GONE);
        bt_delete.setVisibility(View.GONE);
    }

    public void ImageDelete(){
        ArrayList<ImagePojo> selectImageBeans = imageAdapter.getNowImagePojo();
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < selectImageBeans.size(); i++) {
            if (selectImageBeans.get(i).getSelect()){
                integers.add(i);
                File file = new File(selectImageBeans.get(i).getImagePath());
                if (file.exists()&&file.isFile()){
                    if (file.delete()){
                        Log.i("delete","文件已删除:"+selectImageBeans.get(i).getImagePath());
                    }else {
                        Log.i("delete","文件删除失败:"+selectImageBeans.get(i).getImagePath());
                    }

                }else {
                    Log.i("delete","不是一个文件:"+selectImageBeans.get(i).getImagePath());
                }
            }
        }

        for (int i = 0; i < integers.size(); i++) {
            if (i!=0){
                imageAdapter.NotificationListUpdate(integers.get(i)-i);
            }else {
                imageAdapter.NotificationListUpdate(integers.get(i));
            }

        }

        cancelDelete();

    }

    public void VideoDelete(){
        ArrayList<VideoPojo> selectImageBeans = videoAdapter.getNowImagePojo();
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < selectImageBeans.size(); i++) {
            if (selectImageBeans.get(i).getSelect()){
                integers.add(i);
                File file = new File(selectImageBeans.get(i).getVideoPath());
                if (file.exists()&&file.isFile()){
                    if (file.delete()){
                        Log.i("delete","文件已删除:"+selectImageBeans.get(i).getVideoPath());
                    }else {
                        Log.i("delete","文件删除失败:"+selectImageBeans.get(i).getVideoPath());
                    }

                }else {
                    Log.i("delete","不是一个文件:"+selectImageBeans.get(i).getVideoPath());
                }
            }
        }

        for (int i = 0; i < integers.size(); i++) {
            if (i!=0){
                videoAdapter.NotificationListUpdate(integers.get(i)-i);
            }else {
                videoAdapter.NotificationListUpdate(integers.get(i));
            }

        }

        cancelDelete();

    }

    public void ZipDelete(){
        ArrayList<ZipPojo> selectZipBeans = zipAdapter.getNowZipPojo();
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < selectZipBeans.size(); i++) {
            if (selectZipBeans.get(i).getSelect()){
                integers.add(i);
                File file = new File(selectZipBeans.get(i).getZipPath());
                if (file.exists()&&file.isFile()){
                    if (file.delete()){
                        Log.i("delete","文件已删除:"+selectZipBeans.get(i).getZipPath());
                    }else {
                        Log.i("delete","文件删除失败:"+selectZipBeans.get(i).getZipPath());
                    }

                }else {
                    Log.i("delete","不是一个文件:"+selectZipBeans.get(i).getZipPath());
                }
            }
        }

        for (int i = 0; i < integers.size(); i++) {
            if (i!=0){
                zipAdapter.NotificationListUpdate(integers.get(i)-i);
            }else {
                zipAdapter.NotificationListUpdate(integers.get(i));
            }

        }

        cancelDelete();

    }
}