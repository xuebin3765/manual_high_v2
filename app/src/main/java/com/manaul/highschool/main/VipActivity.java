package com.manaul.highschool.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manaul.highschool.bean.User;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.DataUtil;
import com.manaul.highschool.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class VipActivity extends AppCompatActivity implements OnClickListener {

	private Context mContext;
	private String topTitle;
	private TextView loginTitle;
	private TextView loginText;
	private LinearLayout login;
	private Button buy_sure_wx;
	private Button buy_sure_zfb;
	private ProgressDialog dialog;
	private TextView vipOneMonth;
	private TextView vipOneQuarter;
	private TextView vipOneYear;
	private TextView vipNever;
	private String vipOneMonthText;
	private String vipOneQuarterText;
	private String vipOneYearText;
	private String vipNeverText;
	
	private TextView allPrice;
	private String allPriceTest;

	private double price = Constant.MONWEY_180;
	private int type = 3;
	private String title = "开通【180天】会员";

	private User user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "InlinedApi", "NewApi" })
	private void init() {
		mContext = this;
		setContentView(R.layout.vip_center);
		user = BmobUser.getCurrentUser(User.class);
		if(user == null){
			finish();
			Intent intent =  new Intent(mContext,LoginActivity.class);
			intent.putExtra("top_title", "登陆");
			startActivity(intent);
		}
		Intent intent = getIntent();
		topTitle = intent.getStringExtra("top_title");
		ActionBar mActionbar = getSupportActionBar();
		if (mActionbar != null) {
			mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			mActionbar.setDisplayShowCustomEnabled(true);
			mActionbar.setCustomView(R.layout.title_to_center);
			TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
			tvTitle.setText(topTitle);
			ImageView iv_tbb_back = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_back);
			ImageView iv_tbb_user = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_user);
			iv_tbb_back.setVisibility(View.VISIBLE);
			iv_tbb_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			iv_tbb_user.setVisibility(View.GONE);
		}
		login = (LinearLayout) findViewById(R.id.my_login);
		login.setClickable(false);
		loginTitle = (TextView) findViewById(R.id.my_login_title);
		loginText = (TextView) findViewById(R.id.my_login_text);



		user = BmobUser.getCurrentUser(User.class);
		
		if (user == null) {
			// 未登录
			loginTitle.setText("未登录,点击登陆");
			loginText.setVisibility(View.GONE);
			loginText.setText("未开通会员");

			login.setClickable(true);
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				// 未登录，进入用户登录页面
				Intent intent = new Intent(mContext, LoginActivity.class);
				intent.putExtra("top_title", "登陆");
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				}
			});
		} else {
			loginText.setVisibility(View.VISIBLE);
			// 已登录
			loginTitle.setText("用户名：" + user.getUsername());
			if(DataUtil.vipIsEnd(user.getVipEnd())){
				loginText.setText("VIP有效期："+DataUtil.getAfterTime(new Date(user.getVipEnd()),0));
			}else {
				loginText.setText("VIP有效期：未开通" );
			}
		}
		// 总价
		allPrice = (TextView) findViewById(R.id.all_price);
		allPriceTest = "<strong>共计 " + price + " 元</strong> <small>已优惠" + (price * 0.5) + "元</small>";
		allPrice.setText(Html.fromHtml(allPriceTest));
		// 一个月
		vipOneMonth = (TextView) findViewById(R.id.vip_one_month);
		vipOneMonthText = "<h3>" + Constant.MONWEY_30 + " 元<small>/30天</small></h3>" + "原价："
				+ (Constant.MONWEY_30 * 1.5) + "元/30<small>";
		// 一季度
		vipOneQuarter = (TextView) findViewById(R.id.vip_one_quarter);
		vipOneQuarterText = "<h3>" + Constant.MONWEY_90 + " 元<small>/90天</small></h3>" + "原价："
				+ (Constant.MONWEY_90 * 1.5) + " 元/季<small>";
		// 一年
		vipOneYear = (TextView) findViewById(R.id.vip_one_year);
		vipOneYearText = "<h3>" + Constant.MONWEY_180 + " 元<small>/180天</small></h3>" + "原价："
				+ (Constant.MONWEY_180 * 1.5) + " 元/180天</small>";
		// 永久
		vipNever = (TextView) findViewById(R.id.vip_never);
		vipNeverText = "<h3>" + Constant.MONWEY_NEVER + " 元<small>/永久</small></h3>" + "原价："
				+ (Constant.MONWEY_NEVER * 1.5) + " 元/永久</small>";

		vipOneMonth.setText(Html.fromHtml(vipOneMonthText));
		vipOneQuarter.setText(Html.fromHtml(vipOneQuarterText));
		vipOneYear.setText(Html.fromHtml(vipOneYearText));
		vipNever.setText(Html.fromHtml(vipNeverText));

		vipOneYear.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_red));

		vipOneMonth.setOnClickListener(this);
		vipOneQuarter.setOnClickListener(this);
		vipOneYear.setOnClickListener(this);
		vipNever.setOnClickListener(this);

		
		// 支付
		buy_sure_wx = (Button) findViewById(R.id.buy_weixin);
		buy_sure_zfb = (Button) findViewById(R.id.buy_zhifubao);
		buy_sure_wx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pay(false); // 微信支付
			}
		});
		buy_sure_zfb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pay(true); // 支付宝支付
			}
		});

	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onClick(View v) {
		vipOneMonth.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_bai));
		vipOneQuarter.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_bai));
		vipOneYear.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_bai));
		vipNever.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_bai));
		price = Constant.MONWEY_180;
		title = "开通【180天】会员";
		type = 3;
		
		switch (v.getId()) {
		case R.id.id_top_center_back_lin:
			finish();
			break;
		case R.id.vip_one_month:
			price = Constant.MONWEY_30;
			title = "开通【30天】会员";
			type = 1;
			vipOneMonth.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_red));
			break;
		case R.id.vip_one_quarter:
			price = Constant.MONWEY_90;
			title = "开通【90天】会员";
			type = 2;
			vipOneQuarter.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_red));
			break;
		case R.id.vip_one_year:
			price = Constant.MONWEY_180;
			title = "开通【180天】会员";
			type = 3;
			vipOneYear.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_red));
			break;
		case R.id.vip_never:
			price = Constant.MONWEY_NEVER;
			title = "开通【永久】会员";
			type = 4;
			vipNever.setBackground(mContext.getResources().getDrawable(R.drawable.tab_shape_red));
			break;
		}
		allPriceTest = "<strong>共计 " + price + " 元</strong> <small>已优惠" + (price * 0.5) + "元</small>";
		allPrice.setText(Html.fromHtml(allPriceTest));
	}

	/**
	 * 检查某包名应用是否已经安装
	 * 
	 * @param packageName
	 *            包名
	 * @param browserUrl
	 *            如果没有应用市场，去官网下载
	 * @return
	 */
	private boolean checkPackageInstalled(String packageName, String browserUrl) {
		try {
			// 检查是否有支付宝客户端
			getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			// 没有安装支付宝，跳转到应用市场
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + packageName));
				startActivity(intent);
			} catch (Exception ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(browserUrl));
					startActivity(intent);
				} catch (Exception eee) {
					Toast.makeText(mContext, "您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝或微信吧", Toast.LENGTH_SHORT).show();
				}
			}
		}
		return false;
	}

	/**
	 * 调用支付
	 * 
	 * @param alipayOrWechatPay
	 *            支付类型，true为支付宝支付,false为微信支付
	 */
	void pay(final boolean alipayOrWechatPay) {
		if (alipayOrWechatPay) {
			if (!checkPackageInstalled("com.eg.android.AlipayGphone", "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
				Toast.makeText(mContext, "请安装支付宝客户端", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {

			if (checkPackageInstalled("com.tencent.mm", "http://weixin.qq.com")) {// 需要用微信支付时，要安装微信客户端，然后需要插件
				// 有微信客户端，看看有无微信支付插件，170602更新了插件，这里可检查可不检查
				if (!BP.isAppUpToDate(this, "cn.bmob.knowledge", 8)){
					Toast.makeText(
							mContext,
							"监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
							Toast.LENGTH_SHORT).show();
					installApk("bp.db");
					return;
				}
			} else {// 没有安装微信
				Toast.makeText(mContext, "请安装微信客户端", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		showDialog("正在获取订单...");

		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName cn = new ComponentName("com.bmob.app.sport","com.bmob.app.sport.wxapi.BmobActivity");
			intent.setComponent(cn);
			this.startActivity(intent);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		String message = "您（"+user.getUsername()+"）将"+title;
		BP.pay(title, message , price , alipayOrWechatPay, new PListener() {
			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				ToastUtils.showToastShort(mContext , "支付结果未知,请稍后手动查询或联系客服！");
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				if(user == null) user = BmobUser.getCurrentUser(User.class);
				int day = 0;
				switch (type){
					case 1:
						day = 30;
						break;
					case 2:
						day = 90;
						break;
					case 3:
						day = 180;
						break;
					case 4:
						day = 3000;
						break;
				}
				// 未过期
				if(DataUtil.vipIsEnd(user.getVipEnd())){
					user.setVipEnd(DataUtil.getAfterTimeLong(new Date(user.getVipEnd()) , day));
					ToastUtils.showToastShort(mContext , DataUtil.getCurrentTime(new Date(user.getVipEnd())));
				}else {
					user.setVipEnd(DataUtil.getAfterTimeLong(new Date() , day));
				}

				User newUser = new User();
				newUser.setVipEnd(user.getVipEnd());
				newUser.update(user.getObjectId(),new UpdateListener() {
					@Override
					public void done(BmobException e) {
						hideDialog();
						if(e==null){
							ToastUtils.showToastShort(mContext , "激活成功");
						}else{
							ToastUtils.showToastShort(mContext , "激活失败，如已付款请联系客服");
						}
					}
				});
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				showDialog("获取订单成功!请等待跳转到支付页面~");
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {
				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				if (code == -3) {
					Toast.makeText(mContext, "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付", Toast.LENGTH_SHORT)
							.show();
					// installBmobPayPlugin("bp.db");
					installApk("bp.db");
				} else if (code == -2) {
					Toast.makeText(mContext, "取消支付", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();

				}

				hideDialog();
			}
		});
	}

	private static final int REQUESTPERMISSION = 101;

	private void installApk(String s) {

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// 申请权限
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
					REQUESTPERMISSION);
		} else {
			installBmobPayPlugin(s);
		}
	}

	void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
		}
	}

	void hideDialog() {
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}

	/**
	 * 安装assets里的apk文件
	 * 
	 * @param fileName
	 */
	void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName + ".apk");
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
			startActivity(intent);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
