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
import android.util.Log;

import java.io.File;

public class DownloadService extends Service {
	
	
	private DownloadManager mDownloadManager;
    private BroadcastReceiver receiver;
//    private  static final String APK_NAME="youni.apk";
    private  static final String APK_NAME="koudaigk_"+ System.currentTimeMillis()+"_.apk";
    private String apkUrl;
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
    	apkUrl = intent.getStringExtra("onBind");
    	Log.e("manaul_log", "onBind"+apkUrl+"===");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("manaul_log", "onCreate");
        /*删除以前下载的安装包*/
        RecursionDeleteFile(new File(Environment.getExternalStorageDirectory() + "/download/chinese/"));
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onStart(Intent intent, int startId) {
    	apkUrl = intent.getStringExtra("url");
    	Log.e("manaul_log", "onStart"+apkUrl);
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	apkUrl = intent.getStringExtra("url");
    	Log.e("manaul_log", "onStartCommand"+apkUrl);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/chinese/"+APK_NAME)),
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
    	Log.e("manaul_log", "onDestroy");
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload() {
    	Log.e("manaul_log", "startDownload");
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setDescription("正在为您下载最新版本");
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+"/chinese", APK_NAME);
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