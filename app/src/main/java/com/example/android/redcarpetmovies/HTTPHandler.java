package com.example.android.redcarpetmovies;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HTTPHandler {
    private static final String LOG_TAG = HTTPHandler.class.getSimpleName();

    //Empty constructor
    public HTTPHandler(){

    }

    //Method to get the Url connection and the input stream
    public String getHTTPResponse(String receivedUrl){
        String response = null;

        try{
            URL url = new URL(receivedUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            response = convertStreamToString(in);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "MalformedURLException", e.fillInStackTrace());
        }catch (IOException e){
            Log.e(LOG_TAG, "IOException", e.fillInStackTrace());
        }
        return response;
    }

    // A method to convert the input stream to a string json to be parsed
    private String convertStreamToString(InputStream is) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try{
            while ((line=bufferedReader.readLine()) !=null){
                sb.append(line).append("\n");
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "IOException at converting to String", e.fillInStackTrace());
        }finally {
            try{
                is.close();
            }catch (IOException e){
                Log.e(LOG_TAG, "IO exception while closing input Stream", e.fillInStackTrace());
            }
        }
        return sb.toString();
    }


}

