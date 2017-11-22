package com.manaul.highschool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.manaul.highschool.bean.ADInfo;
import com.manaul.highschool.bean.Banner;
import com.manaul.highschool.loader.AsyncImageLoader;
import com.manaul.highschool.main.R;
import com.manaul.highschool.utils.ToastUtils;

import java.util.List;

/**
 * 轮播图
 * Created by Administrator on 2017/8/5.
 */

public class CyclePagerAdapter extends PagerAdapter {

    private List<Banner> infoList;
    private Context mContext;
    /**
     * 返回多少page
     */
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    /**view滑动到一半时是否创建新的view
     * true:表示不去创建，使用缓存；false:去重新创建
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public CyclePagerAdapter(Context context , List<Banner> adInfoList) {
        this.mContext = context;
        this.infoList = adInfoList;
    }

    /**
     * 类似于BaseAdapter的getView方法
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.adapter_ad, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        Banner ad = infoList.get(position % infoList.size());
        if(ad != null && ad.getImageUtl() != null){
            AsyncImageLoader loader = new AsyncImageLoader(mContext);
            loader.setCache2File(true); //false
            loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());
            //下载图片，第二个参数是否缓存至内存中
            loader.downloadImage(ad.getImageUtl(), true/*false*/, new AsyncImageLoader.ImageCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                    if(bitmap != null){
                        imageView.setImageBitmap(bitmap);

                    }else{
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_error));
                    }
                }
            });
        }else {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_error));
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToastShort(mContext , "点击图片了");
            }
        });
        container.addView(view);
        return view;
    }
    /**
     * @param position:当前需要销毁第几个page
     * @param object:当前需要销毁的page
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
