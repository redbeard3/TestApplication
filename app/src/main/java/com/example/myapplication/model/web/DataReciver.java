package com.example.myapplication.model.web;

import android.util.Log;

import com.example.myapplication.model.pojo.Album;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataReciver {

    private static final String TAG = "DataReciver";

    public byte[] getUrlBytes(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();
            if (HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
                throw new IOException(connection.getResponseMessage() + stringUrl); // todo обработать в виде заглушки
            }

            int bytesRead = 0;
            byte[] bytes = new byte[1024];
            while ((bytesRead = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, bytesRead);
            }

            outputStream.close();
            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getStringUrl (String stringUrl) throws IOException {
        return new String(getUrlBytes(stringUrl));
    }

    public List<Album> getAlbumItems() {
        List<Album> albums = new ArrayList<>();
        try {
            String resultByJson = new DataReciver().getStringUrl("https://jsonplaceholder.typicode.com/albums");
            albums = new Gson().fromJson(resultByJson, ArrayList.class);
            Log.i(TAG, "Content of URL: " + resultByJson);
            return albums;
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch URL: ", e.getCause());
        }

        return albums;
    }
}
