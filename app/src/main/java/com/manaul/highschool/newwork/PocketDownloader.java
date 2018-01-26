package com.manaul.highschool.newwork;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/11/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class PocketDownloader {

    private Context mContext;

    public PocketDownloader(Context context) {
        mContext = context;
    }

    public void openView(){
        Intent intent = new Intent();
        intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        mContext.startActivity(intent);
    }

    /**
     * 使用系统自带的下载器下载一个文件
     *
     * @param url
     */
    public long download(String url, String targetPath,String mimeType) {

        DownloadManager manager = (DownloadManager)
                mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        return manager.enqueue(newRequest(url, targetPath,mimeType));

    }

    /**
     * new 一个下载器的请求
     *
     * @param url        文件路径
     * @param targetPath 相对公共的路径下的路径
     * @return
     */
    public DownloadManager.Request newRequest(
            String url, String targetPath,String mimeType) {
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType(mimeType);
        request.setDestinationUri(Uri.fromFile(
                Environment.getExternalStoragePublicDirectory(
                        targetPath)));
        return request;
    }

    /**
     *
     * -1：找不到记录
     *  1：下载中
     *  2：暂定
     *  3：成功
     *  4：失败 -- 发现失败会删除该记录
     * @return
     */
    public int getDownloadStatus(long referenceId){

        //获取下载管理器
        DownloadManager manager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();

        //查询特定id 的文件
        query.setFilterById(referenceId);
        Cursor cursor = manager.query(query);
        if(!cursor.moveToFirst()){
            return -1;
        }

        //获取下载状态
        int statue = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

        switch (statue){
            case DownloadManager.STATUS_PENDING:
            case DownloadManager.STATUS_RUNNING:
                return 1;
            case DownloadManager.STATUS_PAUSED:
                return 2;
            case DownloadManager.STATUS_SUCCESSFUL:
                return 3;
            case DownloadManager.STATUS_FAILED:
                manager.remove(referenceId);
                return 4;
        }
        return -1;
    }

}
