package net.tool.practical.filemanagerplus.free.cosyfile.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ImagePojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.VideoPojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ZipPojo;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FileUtil {

    public static void getInfo(Context context,QueryFinish listener){
        ArrayList<ImagePojo> imageList = new ArrayList<>();
        ArrayList<VideoPojo> videoList = new ArrayList<>();
        ArrayList<ZipPojo> zipList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

               Thread image = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] projection = { MediaStore.Images.Media.DATA };
                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                                // 处理图片路径
                                File image =new File(imagePath);
                                if (image.exists() && image.isFile()){
                                    String name = image.getName();
                                    imageList.add(new ImagePojo(imagePath,name));
                                }

                            }
                            cursor.close();
                        }
                    }
                });

                Thread video = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String[] projection = { MediaStore.Video.Media.DATA };
                        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                                File image =new File(videoPath);
                                if (image.exists()){


                                    videoList.add(new VideoPojo(videoPath,image.getName()));
                                }


                            }
                            cursor.close();
                        }
                    }
                });

                Thread zip = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Uri uri = MediaStore.Files.getContentUri("external");

                        String[] projection = {
                                MediaStore.Files.FileColumns.DATA,
                                MediaStore.Files.FileColumns.MIME_TYPE
                        };

                        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
                        String[] selectionArgs = {"application/zip"}; // 查询 ZIP 压缩包文件

                        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

                        if (cursor != null) {

                            while (cursor.moveToNext()) {
                                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                                File zip = new File(filePath);
                                if (zip.exists()){
                                    String fileSize = getFileSize(zip.length());
                                    long l = zip.lastModified();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    String formattedTime = dateFormat.format(new Date(l));

                                    zipList.add(new ZipPojo(filePath,fileSize,formattedTime,zip.getName()));
                                }

                            }
                            cursor.close();

                    }
                }});
                try {
                    image.start();
                    video.start();
                    zip.start();

                    image.join();
                    video.join();
                    zip.join();

                    System.out.println("--**--**--"+imageList.size());
                    listener.searchResult(imageList,videoList,zipList);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public interface QueryFinish{
        void searchResult(ArrayList<ImagePojo> imageList,ArrayList<VideoPojo> videoList,ArrayList<ZipPojo> zipList);
    }

    // 工具类 提供px和dip的相互转
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        public static int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

        public static String getFileSize(long size){
            double sizeMB = (double) size / (1024 * 1024);
            String formattedSize;

            if (sizeMB < 1) {
                // 如果大小小于1MB，则显示为KB
                double sizeKB = (double) size / 1024;
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                formattedSize = decimalFormat.format(sizeKB) + "KB";
            } else {
                // 如果大小大于等于1MB，则显示为MB
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                formattedSize = decimalFormat.format(sizeMB) + "MB";
            }
            return formattedSize;
        }

}
