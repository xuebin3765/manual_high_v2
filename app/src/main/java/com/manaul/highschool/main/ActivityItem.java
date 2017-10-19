package com.manaul.highschool.main;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manaul.highschool.adapter.MyGridAdapter;
import com.manaul.highschool.bean.Navigate;
import com.manaul.highschool.bean.Project;
import com.manaul.highschool.dao.NavigateDao;
import com.manaul.highschool.dao.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ActivityItem extends AppCompatActivity {

    private Context mContext;
    private TextView tvTitle;

    private SQLiteHelper sqliteHelper;
    private SQLiteDatabase db;
    private NavigateDao navigateDao;
    private LinearLayout activity_item_lin_zsd;
    private LinearLayout activity_item_lin_tiku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mContext = this;
        sqliteHelper = new SQLiteHelper(mContext);
        db = sqliteHelper.getWritableDatabase();
        navigateDao = new NavigateDao(db);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Project p = (Project) bundle.get("project");
        // 对象没有
        if (p == null) {
            p = new Project();
        }

        final Project project = p;

        ActionBar mActionbar = getSupportActionBar();
        mContext = this;
        if (mActionbar != null) {
            mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionbar.setDisplayShowCustomEnabled(true);
            mActionbar.setCustomView(R.layout.title_to_center_item);
            tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
            tvTitle.setText(mContext.getResources().getString(R.string.app_name));

            mActionbar.getCustomView().findViewById(R.id.iv_home_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mActionbar.getCustomView().findViewById(R.id.iv_home_favorite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Navigate navigate = new Navigate();
                    navigate.setNavigateId(-1);
                    navigate.setName("收藏夹");
                    navigate.setType(project.getType());
                    Intent intent = new Intent(mContext, ContentListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("navigate", navigate);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }



        activity_item_lin_zsd = (LinearLayout) findViewById(R.id.activity_item_lin_zsd);
        activity_item_lin_tiku = (LinearLayout) findViewById(R.id.activity_item_lin_tiku);

        List<Navigate> navigates = navigateDao.findNavigateSimple(project.getProjectId(), 1, 1000);
        View girdItemLYView = getLayoutInflater().inflate(R.layout.layout_gird_item, null);
        GridView gridView = (GridView) girdItemLYView.findViewById(R.id.layout_gird_item_girdview);

        // 设置GridView
        if (navigates == null || navigates.size() == 0) {
            navigates = new ArrayList<>();
            Navigate navigate = new Navigate();
            navigate.setNavigateId(-2);
            navigate.setName("还没有知识点呢");
            navigate.setType("null");
            navigates.add(navigate);
            gridView.setNumColumns(1);
        }

        final List<Navigate> finalNavigates = navigates;
        gridView.setAdapter(new MyGridAdapter(mContext, navigates));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (finalNavigates.get(position).getNavigateId() == -2) return;
            Intent intent = new Intent(mContext, ContentListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("navigate", finalNavigates.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
            }
        });
        activity_item_lin_zsd.addView(girdItemLYView); // 添加到布局文件
    }
}
