package com.manas.earthquakereportapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryJson {
    public static List<Info> fetchEarthquakeData(String requestURL){
       
        URL url = createURL(requestURL);
        String jsonResponse = "";
        try{
            jsonResponse=makeHttpRequest(url);
        }catch(IOException e){
            Log.e("","error in making http connection",e);
        }
        List<Info> earthquakes=extractEarthquakes(jsonResponse);
        return earthquakes;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse="";
        if(url==null)
            return jsonResponse;

        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try{
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                jsonResponse=readInputBuffer(inputStream);
            }else{
                Log.e("","Error Response code:"+urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e("","Problem in getting JsonResponse",e);
        }finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("", "error with url creation");
            return null;
        }
        return url;
    }
    private static String readInputBuffer(InputStream inputStream) throws IOException{
        StringBuilder output=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line!=null){
                output=output.append(line);
                line=reader.readLine();
            }
        }
        return  output.toString();
    }

    public static List<Info> extractEarthquakes(String JSONresponse){
        if(TextUtils.isEmpty(JSONresponse))
            return  null;

        List<Info> ans=new ArrayList<Info>();

        try {
            JSONObject root = new JSONObject(JSONresponse);
            JSONArray feature=root.getJSONArray("features");
            for(int i=0;i<feature.length();i++)
            {
                JSONObject current=feature.getJSONObject(i);
                JSONObject properties=current.getJSONObject("properties");
                double mag=properties.getDouble("mag");
                String location=properties.getString("place");
                long time=properties.getLong("time");
                String url=properties.getString("url");

                Info c=new Info(mag,location,time,url);
                ans.add(c);
            }

        }catch (JSONException e){
        }

        return ans;
    }
}
