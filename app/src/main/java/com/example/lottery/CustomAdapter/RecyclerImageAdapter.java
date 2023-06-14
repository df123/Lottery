package com.example.lottery.CustomAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lottery.MainActivity;
import com.example.lottery.R;
import com.example.lottery.fragment.CameraFragment;
import com.example.lottery.utils.PageUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> {

    private List<String> mPaths;
    private Context mParentContext;

    public RecyclerImageAdapter(String[] paths, Context parentContext) {
        mPaths = new ArrayList<String>(Arrays.asList(paths));
        mParentContext = parentContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_image, parent, false);
        return new ViewHolder(view, mPaths, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getImageView().setImageURI(Uri.fromFile(new File(mParentContext.getFilesDir(), mPaths.get(position))));
    }


    @Override
    public int getItemCount() {
        return mPaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView, @NonNull List<String> paths, @NonNull RecyclerImageAdapter adapter) {
            super(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setIcon(R.drawable.ic_home).setTitle("警告").setMessage("确定要删除这张照片吗？");
                    alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(v.getContext().getFilesDir(), paths.get(getAdapterPosition()));
                            file.delete();
                            adapter.removeAt(getAdapterPosition());
                        }
                    });
                    AlertDialog alertDialog1 = alertDialog.create();
                    alertDialog1.show();
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity) v.getContext();
//                    BottomNavigationView btn_bottom_nav = mainActivity.findViewById(R.id.btn_bottom_nav);
                    CameraFragment cameraFragment = (CameraFragment) mainActivity.getFragments()[0];
                    cameraFragment.setPictureListPaths(paths.get(getAdapterPosition()));
//                    btn_bottom_nav.setSelectedItemId(btn_bottom_nav.getMenu().getItem(1).getItemId());
//                    mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fra_con, mainActivity.getFragments()[0]).commit();
                    PageUtils.performNavigationSelection(mainActivity,1,0);
                }
            });
            imageView = itemView.findViewById(R.id.image_view_recycler);
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    public void removeAt(int position) {
        mPaths.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mPaths.size());
    }
}
