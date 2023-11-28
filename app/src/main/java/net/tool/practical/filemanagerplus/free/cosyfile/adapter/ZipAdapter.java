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

import net.tool.practical.filemanagerplus.free.cosyfile.R;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.VideoPojo;
import net.tool.practical.filemanagerplus.free.cosyfile.pojo.ZipPojo;

import java.util.ArrayList;

public class ZipAdapter extends RecyclerView.Adapter<ZipAdapter.ViewHolder>{
    private ArrayList<ZipPojo> zipPojos;
    private Context context;
    private boolean isFirst = false;
    private ItemClickListener listener;
    private int select_count =0;
    public ZipAdapter(Context context, ArrayList<ZipPojo> zipPojos, ItemClickListener listener){
        this.context = context;
        this.zipPojos = zipPojos;
        this.listener = listener;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建ViewHolder并关联item布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zip, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ZipPojo zipPojo = zipPojos.get(position);
        holder.zip_time.setText(zipPojo.getZipTime());
        holder.zip_size.setText(zipPojo.getZipSize());
        holder.zip_name.setText(zipPojo.getZipName());

        if (isFirst){
            if (!zipPojo.getSelect()){
                holder.ic_select.setImageResource(R.mipmap.select_no);
            }else {
                holder.ic_select.setImageResource(R.mipmap.select_yes);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst = true;

                if (zipPojo.getSelect()){
                    select_count--;
                    zipPojo.setSelect(false);
                    holder.ic_select.setImageResource(R.mipmap.select_no);
                }else {
                    select_count++;
                    zipPojo.setSelect(true);
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
        return zipPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView zip_name;
        private TextView zip_size;
        private ImageView ic_select;
        private TextView zip_time;
        public ViewHolder(View itemView) {
            super(itemView);
            zip_name = itemView.findViewById(R.id.zip_name);
            ic_select = itemView.findViewById(R.id.select);
            zip_size = itemView.findViewById(R.id.zip_size);
            zip_time = itemView.findViewById(R.id.zip_time);
        }
    }
    public interface ItemClickListener{
        void onClick();
        void Deselect();
    }
    public int getSelect_count(){
        return select_count;
    }

    public ArrayList<ZipPojo> getNowZipPojo(){
        return zipPojos;
    }
    public void NotificationListUpdate(int i){
        select_count=0;

        try {
            zipPojos.remove(i);
            notifyItemRemoved(i);

        }catch (Exception e){
            Log.e("移除item出错",e.getMessage());
        }
    }
}
