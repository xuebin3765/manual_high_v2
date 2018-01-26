package com.manaul.highschool.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SystemUtil;

import java.io.File;

public class DownloadService extends Service {
	
	
	private DownloadManager mDownloadManager;
    private BroadcastReceiver receiver;

    private String apkUrl;
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
    	apkUrl = intent.getStringExtra("onBind");
        DebugUtil.d("onBind"+apkUrl+"===");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        DebugUtil.d("onCreate");
        /*删除以前下载的安装包*/
        RecursionDeleteFile(new File(Environment.getExternalStorageDirectory() + "/download/manual_high/"));
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onStart(Intent intent, int startId) {
    	apkUrl = intent.getStringExtra("url");
    	DebugUtil.d("onStart"+apkUrl);
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	apkUrl = intent.getStringExtra("url");
        DebugUtil.d("onStartCommand"+apkUrl);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/manual_high/"+ SystemUtil.APP_APK_NAME)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
                stopSelf();
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        DebugUtil.d("onDestroy");
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload() {
    	DebugUtil.d( "startDownload");
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setDescription("正在为您下载最新版本");
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+"/manual_high", SystemUtil.APP_APK_NAME);
        mDownloadManager.enqueue(request);
    }
    /**
     * 递归删除文件和文件夹
     * @param file    要删除的根目录
     */
    public void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }
}