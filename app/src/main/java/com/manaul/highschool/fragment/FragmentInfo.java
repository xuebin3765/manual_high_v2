package com.manaul.highschool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.manaul.highschool.adapter.FragmentInfoAdapter;
import com.manaul.highschool.main.R;
import com.manaul.highschool.utils.ProgressDialogUtils;

import java.util.ArrayList;

/**
 * 资讯
 */
public class FragmentInfo extends Fragment {

    private PullToRefreshListView refreshListView;
    private ArrayList<String> mList;
    private ArrayList<String> list;
    private FragmentInfoAdapter infoAdapter;
    private int page = 1;
    private Button goTop;
    private ProgressDialogUtils progressDialogUtils;
    private View headView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_center_my , container , false);

        return view;
    }
}
