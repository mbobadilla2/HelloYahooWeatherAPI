package fernando2.com.helloyahooweatherapi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fernando2 on 10/17/16.
 */
public class YahooWeather{
        private URL url;
        private MainActivity main;
        private String temp;
        private String text;
        private String location;
        private Bitmap conditions;

        public YahooWeather(MainActivity main) {
            this.main = main;
            location = main.getString(R.string.location);
        }

        public void getWeather() throws JSONException {

            new AsyncTask<String, Void, String>(){

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    try {

                        String yqlQuery = "select item.condition.temp, item.condition.text, item.description" +
                                " from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""+ location + "\")";
                        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(yqlQuery));

                        url = new URL(endpoint);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected String doInBackground(String... strings)  {

                    BufferedReader br = null;
                    HttpURLConnection connection = null;

                    try{

                        connection = (HttpURLConnection) url.openConnection();
                        connection.connect();

                        InputStream stream = connection.getInputStream();
                        br = new BufferedReader(new InputStreamReader(stream));

                        String line = "";
                        StringBuffer buffer = new StringBuffer();

                        while((line = br.readLine()) != null){
                            buffer.append(line);
                        }


                        JSONObject json = new JSONObject(buffer.toString());

                        temp = json.optJSONObject("query").optJSONObject("results")
                                .optJSONObject("channel").optJSONObject("item").optJSONObject("condition").getString("temp");


                        text = json.optJSONObject("query").optJSONObject("results")
                                .optJSONObject("channel").optJSONObject("item").optJSONObject("condition").getString("text");


                        String urlConditions =  json.optJSONObject("query").optJSONObject("results")
                                .optJSONObject("channel").optJSONObject("item").getString("description");

                        String[] temp = urlConditions.split("http");
                        temp = temp[1].split("gif");

                        urlConditions = "http" + temp[0] + "gif";

                        Log.e("**********IMAGEN: ", urlConditions);

                        try {
                            InputStream in = new java.net.URL(urlConditions).openStream();
                            conditions = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            Log.e("*** Error", e.getMessage());
                            e.printStackTrace();
                        }


                    }catch(Exception e){
                        e.printStackTrace();

                    }finally{

                        if(connection != null) {
                            connection.disconnect();
                        }
                        try {
                            if(br != null){
                                br.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(temp != null && text != null && conditions != null) {
                        return "";
                    }else{
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String data) {
                    super.onPostExecute(data);


                    if(data != null) {
                        main.displayData(conditions, Integer.parseInt(temp), text, location);

                    }else{
                        main.displayErrorMsg();
                    }
                }
            }.execute();

        }



}
