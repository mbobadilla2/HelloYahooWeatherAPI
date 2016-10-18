package fernando2.com.helloyahooweatherapi;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
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
        private String location;

        public YahooWeather(MainActivity main) {
            this.main = main;
            location = main.getString(R.string.location);
        }

        public void getTemp() throws JSONException {

            new AsyncTask<String, Void, String>(){

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    try {

                        String yqlQuery = "select item.condition.temp from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""+ location + "\")";
                        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(yqlQuery));

                        url = new URL(endpoint);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    //dynamictext = (TextView) findViewById(R.id.dynamictext);
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

                        System.out.println("************ BACKGROUND La temperatura en Veracruz es: " + temp);

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

                    return temp;
                }

                @Override
                protected void onPostExecute(String temp) {
                    super.onPostExecute(temp);
                    //dynamictext.setText(strings);
                    //dialog.dismiss();
                    //System.out.println("************ POST La temperatura en Veracruz es: " + temp);

                    if(temp != null) {
                        main.showData(Integer.parseInt(temp), location);

                    }else{
                        main.showErrorMsg();
                    }
                }
            }.execute();

        }



}
