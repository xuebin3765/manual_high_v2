package com.manaul.highschool.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.manaul.highschool.utils.DebugUtil;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncImageLoader {

	//保存正在下载的图片URL集合，避免重复下载用
    private static HashSet<String> sDownloadingSet;  
    //软引用内存缓存
    private static Map<String,SoftReference<Bitmap>> sImageCache;   
    //图片三种获取方式管理者，网络URL获取、内存缓存获取缓存外部文件缓存获获取
    private static LoaderImpl impl;  
    //线程池相
    private static ExecutorService sExecutorService;  
      
    //通知UI线程图片获取ok时使用
    private Handler handler;   
      
      
    /** 
     * 异步加载图片完毕的回调接口
     */  
    public interface ImageCallback{  
        /** 
         * 回调函数 
         * @param bitmap: may be null! 
         * @param imageUrl  
         */  
        public void onImageLoaded(Bitmap bitmap, String imageUrl);  
    }
      
    static{  
        sDownloadingSet = new HashSet<String>();  
        sImageCache = new HashMap<String,SoftReference<Bitmap>>();  
        impl = new LoaderImpl(sImageCache);  
    }  
  
    public AsyncImageLoader(Context context){  
        handler = new Handler();  
        startThreadPoolIfNecessary();  
          
        String defaultDir = context.getCacheDir().getAbsolutePath();  
        setCachedDir(defaultDir);  
    }  
      
    /** 
     * 是否缓存图片/data/data/package/cache/目录
     * 默认不缓
     */  
    public void setCache2File(boolean flag){  
        impl.setCache2File(flag);  
    }  
      
    /** 
     * 设置缓存路径，setCache2File(true)
     */  
    public void setCachedDir(String dir){  
        impl.setCachedDir(dir);  
    }  
  
    /**�?启线程池*/  
    public static void startThreadPoolIfNecessary(){  
        if(sExecutorService == null || sExecutorService.isShutdown() || sExecutorService.isTerminated()){  
            sExecutorService = Executors.newFixedThreadPool(3);  
        }
    }  
      
    /** 
     * 异步下载图片，并缓存到memory中
     * @param url    
     * @param callback  see ImageCallback interface 
     */  
    public void downloadImage(final String url, final ImageCallback callback){  
        downloadImage(url, true, callback);  
    }  
      
    /** 
     *  
     * @param url 
     * @param cache2Memory 是否缓存至memory
     * @param callback 
     */  
    public void downloadImage(final String url, final boolean cache2Memory, final ImageCallback callback){  
        if(sDownloadingSet.contains(url)){  
            DebugUtil.d( "###该图片正在下载，不能重复下载");
            return;  
        }  
          
        Bitmap bitmap = impl.getBitmapFromMemory(url);  
        if(bitmap != null){
            DebugUtil.d( " 缓冲中获取");
            if(callback != null){  
                callback.onImageLoaded(bitmap, url);  
            }  
        }else{
            DebugUtil.d( " 从网络端下载图片 ");
            //从网络端下载图片  
            sDownloadingSet.add(url);  
            sExecutorService.submit(new Runnable(){  
                @Override  
                public void run() {  
                    final Bitmap bitmap = impl.getBitmapFromUrl(url, cache2Memory);  
                    handler.post(new Runnable(){  
                        @Override  
                        public void run(){  
                            if(callback != null)  
                                callback.onImageLoaded(bitmap, url);  
                            sDownloadingSet.remove(url);  
                        }  
                    });  
                }  
            });  
        }  
    }  
      
    /** 
     * 预加载下在张图片，缓存至memory
     * @param url  
     */  
    public void preLoadNextImage(final String url){  
        //将callback置为空，只将bitmap缓存到memory即可
        downloadImage(url, null);  
    }
}
