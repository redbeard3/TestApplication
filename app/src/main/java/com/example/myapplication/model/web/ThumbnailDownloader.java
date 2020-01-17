package com.example.myapplication.model.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();

    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;

    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "URL: " + url);
        if (null == url) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (msg.what == MESSAGE_DOWNLOAD) {
                        T target = (T) msg.obj;
                        Log.i(TAG, "Got a request for URL: " +
                                mRequestMap.get(target));
                        handleRequest(target);
                    }
                }
        };
    }

    private void handleRequest(final T target) {
        try {
            final String url = mRequestMap.get(target);
            if (null == url) {
                return;
            }

            /**
             * todo пока заглушка, т.к. URL вида https://via.placeholder.com/150/501fe1 не грузится с ошибкой java.io.FileNotFoundException: https://via.placeholder.com/150/501fe1
             *  изначально была ошибка Cleartext HTTP traffic not permitted, использовал решение из https://stackoverflow.com/questions/45940861/android-8-cleartext-http-traffic-not-permitted
             *
             *  видимо, связано с тем, что на серваке физически нет картинки, она генерится каким-то скриптом.
             */
            byte[] bytes = new DataReciver().getUrlBytes("https://placekitten.com/g/250/250");
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Log.i(TAG, "Bitmap created");

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRequestMap.get(target) != url) {
                        return;
                    }
                    mRequestMap.remove(target);
                    mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
                }
            });
        } catch (IOException ex) {
            Log.e(TAG, "Error by downloading image", ex);
        }
    }

    /**
     * метод, очищающий очередь сообщений
     * нужен для очистки в том случае, когда изменена ориентация экрана
     * ThumbnailDownloader может оказаться связанным с недействительными экземплярами DetailHolder
     */
    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }
}
