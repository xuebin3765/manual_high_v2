package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.manaul.highschool.adapter.ExpandDateAdapter;
import com.manaul.highschool.bean.ItemContent;
import com.manaul.highschool.bean.Navigate;
import com.manaul.highschool.bean.User;
import com.manaul.highschool.dao.NavigateDao;
import com.manaul.highschool.dao.SQLiteHelper;
import com.manaul.highschool.utils.DataUtil;
import com.manaul.highschool.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 
 * @author lovebin
 *
 */
public class ContentListActivity extends AppCompatActivity {

	private ExpandDateAdapter adapter = null;
	private SQLiteHelper sqliteHelper;
	private SQLiteDatabase db;
	private NavigateDao navigateDao;
	private Context mContext;
	private TextView tvTitle;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_list);
		ActionBar mActionbar = getSupportActionBar();

		mContext = this;
		if (mActionbar != null) {
			mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			mActionbar.setDisplayShowCustomEnabled(true);
			mActionbar.setCustomView(R.layout.title_to_center);
			tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
			tvTitle.setText("Java 手册");
			ImageView iv_tbb_back = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_back);
			ImageView iv_tbb_user = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_user);

			iv_tbb_back.setVisibility(View.VISIBLE);
			iv_tbb_user.setVisibility(View.INVISIBLE);
			iv_tbb_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			iv_tbb_user.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.toast(mContext, "用户");
				}
			});
		}

		sqliteHelper = new SQLiteHelper(mContext);
		db = sqliteHelper.getWritableDatabase();
		navigateDao = new NavigateDao(db);

		initView();
		
	}

	private void initView() {

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Navigate navigate = (Navigate) bundle.get("navigate");
		// 对象没有
		if (navigate == null) {
			navigate = new Navigate();
		}

		tvTitle.setText(navigate.getName());
		
		final List<Navigate> listDate = navigateDao.findAll(navigate.getNavigateId() , navigate.getType());
		
		ExpandableListView eListView = (ExpandableListView) findViewById(R.id.expandablelistview);
		adapter = new ExpandDateAdapter(ContentListActivity.this, listDate);
		eListView.setAdapter(adapter);
		if(listDate.size() > 0){
			eListView.expandGroup(0);
		}
		eListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {

				List<ItemContent> itemContents = listDate.get(groupPosition).getItemContents();
				ArrayList<Integer> list = new ArrayList<Integer>();
				if(itemContents != null && itemContents.size() > 0){
					for (ItemContent item : itemContents) {
						if(item.getItemId() != null){
							list.add(item.getItemId());
						}
					}
				}
				
				ItemContent item = listDate.get(groupPosition).getItemContents().get(childPosition);
				User user = BmobUser.getCurrentUser(User.class);
				// 公开或者已登陆的会元
				if (item.getPrice() <= 0 || ( user != null && DataUtil.vipIsEnd(user.getVipEnd()))){
					// 会员可见 或已经登陆的可见
					Intent intent = new Intent(mContext, ContentDetailActivity.class);
					intent.putIntegerArrayListExtra("list", list);
					intent.putExtra("itemId", item.getItemId());
					intent.putExtra("topTitle", listDate.get(groupPosition).getName());
					startActivity(intent);
				} else { // 不是公开文档，也不是会员，进入个人主页,购买会会员
					finish();
					Intent intent =  new Intent(mContext,VipActivity.class);
					intent.putExtra("top_title", "开通VIP");
					startActivity(intent);
				}
				return false;
			}
		});
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
