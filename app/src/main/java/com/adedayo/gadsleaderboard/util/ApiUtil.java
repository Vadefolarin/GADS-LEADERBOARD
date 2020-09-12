package com.adedayo.gadsleaderboard.util;

import android.net.Uri;
import android.util.Log;

import com.adedayo.gadsleaderboard.model.Skills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiUtil {
    private ApiUtil(){}

    public static final String BASE_URL =
            "https://gadsapi.herokuapp.com";
    public static final String PARAM = "api";
    public static final String LEARNING = "hours";
    public static final String IQ = "skilliq";

    public static URL buildLearningUrl(){
        URL url = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PARAM)
                .appendPath(LEARNING).build();
        try{
            url = new URL(uri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildIqUrl(){
        URL url = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PARAM)
                .appendPath(IQ).build();
        try{
            url = new URL(uri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        }
        catch (Exception e){
            Log.d("Error", e.toString());
            return null;
        }
        finally {
            connection.disconnect();
        }
    }

    public static ArrayList<Skills> parseJson(String json){
        final String NAME = "name";
        final String HOURS = "hours";
        final String COUNTRY = "country";
        final String BADGEURL = "badgeUrl";
        final String SCORE = "score";

        ArrayList<Skills> skillsArray = new ArrayList<>();
        if(json != null){
            try{
                JSONArray jsonArray = new JSONArray(json);
                int result = jsonArray.length();
                for (int i = 0; i < result; i++) {
                    JSONObject skillObj = jsonArray.getJSONObject(i);
                    Skills skills = new Skills(skillObj.isNull(NAME) ? "" : skillObj.getString(NAME),
                            skillObj.isNull(HOURS) ? "" : skillObj.getString(HOURS),
                            skillObj.isNull(COUNTRY) ? "" : skillObj.getString(COUNTRY),
                            skillObj.isNull(BADGEURL) ? "" : skillObj.getString(BADGEURL),
                            skillObj.isNull(SCORE) ? "" : skillObj.getString(SCORE));
                    skillsArray.add(skills);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }else {
            Log.e("returned null", "onPostExecute: ", null);
        }
        return skillsArray;
    }

    // create retrofit instance
    public static Retrofit getRetrofit(){
        HttpLoggingInterceptor Interceptor = new HttpLoggingInterceptor();
        Interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(Interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/d/e/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

}
