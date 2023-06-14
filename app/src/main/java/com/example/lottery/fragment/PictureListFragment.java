package com.example.lottery.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lottery.CustomAdapter.RecyclerImageAdapter;
import com.example.lottery.MainActivity;
import com.example.lottery.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PictureListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridLayout grid_image;

    public PictureListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PictureListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureListFragment newInstance(String param1, String param2) {
        PictureListFragment fragment = new PictureListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_picture_list, container, false);

        RecyclerView recyclerImage = inflate.findViewById(R.id.recycler_Image);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        if (savedInstanceState != null) {
            linearLayoutManager = (LinearLayoutManager) savedInstanceState.getSerializable("layoutManager");
        }

        String[] paths = getPicturePaths(getContext());

        RecyclerImageAdapter recyclerImageAdapter = new RecyclerImageAdapter(paths, getContext());

        recyclerImage.setLayoutManager(linearLayoutManager);
        recyclerImage.scrollToPosition(linearLayoutManager.findFirstVisibleItemPosition());
        recyclerImage.setAdapter(recyclerImageAdapter);


        return inflate;
    }

    private String[] getPicturePaths(Context context) {
        String[] listFiles = context.getFilesDir().list((dir, name) -> {
            if (name.toLowerCase().contains("png")) {
                return true;
            }
            return false;
        });
        return listFiles;
    }

}