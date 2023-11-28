package net.tool.practical.filemanagerplus.free.cosyfile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.tool.practical.filemanagerplus.free.cosyfile.R;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ImagePojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.VideoPojo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
    private ArrayList<VideoPojo> videoPojos;
    private Context context;
    private boolean isFirst = false;
    private ItemClickListener listener;
    private int select_count =0;
    public VideoAdapter(Context context, ArrayList<VideoPojo> videoPojos, ItemClickListener listener){
        this.context = context;
        this.videoPojos = videoPojos;
        this.listener = listener;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建ViewHolder并关联item布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        VideoPojo videoPojo = videoPojos.get(position);
        // 加载为四个都是圆角的图片 可以设置圆角幅度


        Glide.with(context)
                .asBitmap()
                .load(videoPojo.getVideoPath())
                .frame(TimeUnit.SECONDS.toMicros(1))
                .into(holder.image_item);
        holder.image_name.setText(videoPojo.getVideoName());

        if (isFirst){
            if (!videoPojo.getSelect()){
                holder.ic_select.setImageResource(R.mipmap.select_no);
            }else {
                holder.ic_select.setImageResource(R.mipmap.select_yes);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst = true;

                if (videoPojo.getSelect()){
                    select_count--;
                    videoPojo.setSelect(false);
                    holder.ic_select.setImageResource(R.mipmap.select_no);
                }else {
                    select_count++;
                    videoPojo.setSelect(true);
                    holder.ic_select.setImageResource(R.mipmap.select_yes);

                }
                if (select_count !=0){
                    listener.onClick();
                }else {
                    listener.Deselect();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image_item;
        private ImageView ic_select;
        private TextView image_name;
        public ViewHolder(View itemView) {
            super(itemView);
            image_item = itemView.findViewById(R.id.image_item);
            ic_select = itemView.findViewById(R.id.ic_select);
            image_name = itemView.findViewById(R.id.image_name);
        }
    }
    public interface ItemClickListener{
        void onClick();
        void Deselect();
    }
    public int getSelect_count(){
        return select_count;
    }

    public ArrayList<VideoPojo> getNowImagePojo(){
        return videoPojos;
    }
    public void NotificationListUpdate(int i){
        select_count=0;

        try {
            videoPojos.remove(i);
            notifyItemRemoved(i);

        }catch (Exception e){
            Log.e("移除item出错",e.getMessage());
        }
    }
}
