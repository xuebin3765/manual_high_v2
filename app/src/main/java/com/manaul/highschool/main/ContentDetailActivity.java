package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manaul.highschool.bean.ItemContent;
import com.manaul.highschool.bean.ListItemShare;
import com.manaul.highschool.bean.User;
import com.manaul.highschool.dao.ListItemShareDao;
import com.manaul.highschool.dao.NavigateDao;
import com.manaul.highschool.dao.SQLiteHelper;
import com.manaul.highschool.utils.DataUtil;
import com.manaul.highschool.utils.SharedConfig;
import com.manaul.highschool.utils.ToastUtils;
import com.manaul.highschool.view.LinkMovementMethodExt;
import com.manaul.highschool.view.MessageSpan;
import com.manaul.highschool.view.TextViewFormHtmlUtil;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobUser;

/**
 * 详情页面
 * 
 * @author lovebin
 *
 */
@SuppressLint("RtlHardcoded")
public class ContentDetailActivity extends AppCompatActivity implements OnClickListener {
	private ListItemShareDao listItemShareDao;
	private NavigateDao navigateDao;
	private SQLiteHelper sqliteHelper;
	private SQLiteDatabase db;
	private Context mContext;
	private ListItemShare itemShare = null;
	private int parentId;
	private int itemId;
	private SharedPreferences shareConfig;
	private Intent intent;
	private Handler mHandler;
	private Button back;
	private Button next;
	private int index = 0;
	private List<Integer> list = new ArrayList<Integer>();
	private TextView tvTitle;
	private ImageView iv_tbb_back;
	private ImageView iv_tbb_user;
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_screen_detail_page);
		mContext = this;
		sqliteHelper = new SQLiteHelper(getApplicationContext());
		db = sqliteHelper.getWritableDatabase();
		listItemShareDao = new ListItemShareDao(db);
		navigateDao = new NavigateDao(db);

		ActionBar mActionbar = getSupportActionBar();
		mContext = this;
		if (mActionbar != null) {
			mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			mActionbar.setDisplayShowCustomEnabled(true);
			mActionbar.setCustomView(R.layout.title_to_center);
			tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
			tvTitle.setText("Java 手册");
			iv_tbb_back = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_back);
			iv_tbb_user = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_user);

			iv_tbb_back.setVisibility(View.VISIBLE);
			iv_tbb_user.setVisibility(View.VISIBLE);

			iv_tbb_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}

		shareConfig = new SharedConfig(mContext).getConfig();

		// 初始=
		init();

	}

	// 初始化页面数
	@SuppressWarnings("deprecation")
	@SuppressLint({ "InlinedApi", "NewApi", "HandlerLeak" })
	void init() {

		intent = getIntent();
		itemId = intent.getIntExtra("itemId", -1);
		list = intent.getIntegerArrayListExtra("list");
		if (list != null && list.size() > 0) {
			index = list.indexOf(itemId);
		}

		String topTitle = intent.getStringExtra("topTitle");
		tvTitle.setText(topTitle);
		ItemContent itemContent = navigateDao.getItemContent(list.get(index));

		if (itemContent == null) {
			itemContent = new ItemContent();
			itemContent.setTitle("数据好像丢失");
		}

		parentId = itemContent.getParentId();

		itemShare = listItemShareDao.findByCidPid(itemContent.getParentId(), itemContent.getItemId());
		// 如果收藏了，设置收藏的图标为红色
		if (itemShare != null) {
			iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_black_24dp));
		} else {
			iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
		}

		iv_tbb_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				itemShare = listItemShareDao.findByCidPid(parentId, itemId);
				if (itemShare == null) {
					ContentValues values = new ContentValues();
					values.put("parentId", parentId);
					values.put("itemId", itemId);
					boolean flag = listItemShareDao.insert(values);
					Toast.makeText(getApplicationContext(), "添加到收藏夹", Toast.LENGTH_SHORT).show();
					if (flag) {
						iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_black_24dp));
					}
				} else {
					boolean flag = listItemShareDao.delete(itemShare.getShareId());
					if (flag) {
						Toast.makeText(getApplicationContext(), "从收藏夹删除", Toast.LENGTH_SHORT).show();
						iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
					}
				}
			}
		});


		// 底部下一页 上一页
		back = (Button) findViewById(R.id.back);
		next = (Button) findViewById(R.id.next);
		back.setOnClickListener(this);
		next.setOnClickListener(this);
		// 展示数据
		showData(itemContent);

	}

	// 展示数据
	@SuppressLint("HandlerLeak")
	private void showData(ItemContent itemCon) {

		TextView title = (TextView) findViewById(R.id.detail_title);
		TextView content = (TextView) findViewById(R.id.detail_content);

		new TextViewFormHtmlUtil(itemCon.getTitle(), title, mContext).execute();
		new TextViewFormHtmlUtil(itemCon.getContext(), content, mContext).execute();

		List<String> imageUrlList = DataUtil.getImageSrc(itemCon.getContext());

		if (imageUrlList != null && imageUrlList.size() > 0) {
			for (String string : imageUrlList) {
				if(string.contains("ueditor"))
					imageUrlList.remove(string);
			}
		}
		Editor editor = shareConfig.edit();
		editor.putString("images", StringUtils.join(imageUrlList , "&&"));
		editor.commit();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				int what = msg.what;
				Log.i("222" , msg.toString());
				if (what == 200) {
					MessageSpan ms = (MessageSpan) msg.obj;
					Object[] spans = (Object[]) ms.getObj();
					for (Object span : spans) {
						if (span instanceof ImageSpan) {
							final Intent intent = new Intent(mContext, ShowPicActivity.class);
							final Bundle bundle = new Bundle();
							bundle.putString("imgUrl", ((ImageSpan) span).getSource());
							intent.putExtras(bundle);
							startActivity(intent);
						}
					}
				}
			};
		};

		content.setMovementMethod(LinkMovementMethodExt.getInstance(mHandler, ImageSpan.class));
		content.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
		db.close();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back) {
			if (index - 1 < 0) {
				ToastUtils.showToastShort(mContext, "亲，已经是第一篇了");
			} else { // 进入上一页
				index--;
				ItemContent itemContent = navigateDao.getItemContent(list.get(index));
				if (itemContent != null) {
					itemId = itemContent.getItemId();
					User user = BmobUser.getCurrentUser(User.class);
					// 公开或已登陆的会员
					if (itemContent.getPrice() <= 0 || ( user != null && DataUtil.vipIsEnd(user.getVipEnd()))) { // 会员可见
						// 如果收藏了，设置收藏的图标为红色
						itemShare = listItemShareDao.findByCidPid(parentId, itemContent.getItemId());
						// 如果收藏了，设置收藏的图标为红色
						if (itemShare != null) {
							iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_black_24dp));
						} else {
							iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
						}
						showData(itemContent);
					} else {
						finish();
						Intent intent =  new Intent(mContext,VipActivity.class);
						intent.putExtra("top_title", "开通VIP");
						startActivity(intent);
					}
				} else {
					ToastUtils.showToastShort(mContext, "数据好像出错了");
				}

			}
		}
		if (v.getId() == R.id.next) {
			if (index + 1 >= list.size()) {
				ToastUtils.showToastShort(mContext, "亲，已经是最后一篇了");
			} else { // 进入下一页
				index++;
				ItemContent itemContent = navigateDao.getItemContent(list.get(index));
				if (itemContent != null) {
					itemId = itemContent.getItemId();
					User user = BmobUser.getCurrentUser(User.class);
					// 公开或会员已登陆的会会员
					if (itemContent.getPrice() <= 0 || ( user != null && DataUtil.vipIsEnd(user.getVipEnd()))) { // 会员可见
						// 如果收藏了，设置收藏的图标为红色
						itemShare = listItemShareDao.findByCidPid(parentId, itemContent.getItemId());
						// 如果收藏了，设置收藏的图标为红色
						if (itemShare != null) {
							iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red_black_24dp));
						} else {
							iv_tbb_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
						}
						showData(itemContent);
					} else {
						finish();
						Intent intent =  new Intent(mContext,VipActivity.class);
						intent.putExtra("top_title", "开通VIP");
						startActivity(intent);
					}
				} else {
					ToastUtils.showToastLength(mContext, "数据好像出错了");
				}

			}
		}
	}

}
