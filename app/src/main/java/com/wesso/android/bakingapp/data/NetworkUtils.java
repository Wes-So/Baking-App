package com.wesso.android.bakingapp.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class NetworkUtils {


    private static URL buildURL(String data) {
        URL url = null;

        try {
            url = new URL(data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static  byte[] getByteResponseFromHttpURL(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Error connecting to " + url.toString() + " with this error: " + connection.getResponseMessage());
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public static String getData(String url) throws IOException{
        byte[] data = getByteResponseFromHttpURL(buildURL(url));
        return new String(data);
    }


}
