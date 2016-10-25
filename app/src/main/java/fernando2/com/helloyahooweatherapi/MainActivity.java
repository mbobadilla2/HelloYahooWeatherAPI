package fernando2.com.helloyahooweatherapi;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private TextView textView_temp;
    private TextView textView_location;
    private TextView textView_text;
    private ImageView imageView_conditions;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_temp = (TextView) findViewById(R.id.textView_temp);
        textView_location = (TextView) findViewById(R.id.textView_location);
        textView_text = (TextView) findViewById(R.id.textView_text);
        imageView_conditions = (ImageView) findViewById(R.id.imageView_conditions);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        YahooWeather yahooWeather = new YahooWeather(MainActivity.this);

        try {
            yahooWeather.getWeather();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayData(Bitmap conditions, int temp, String text, String location){

        temp = fahrenheitToCelsius(temp);

        dialog.dismiss();

        imageView_conditions.setImageBitmap(conditions);
        textView_temp.setText(temp+"");
        textView_text.setText(text);
        textView_location.setText(location);

    }

    public void displayErrorMsg(){
        dialog.dismiss();

        imageView_conditions.setImageResource(R.drawable.ic_insert_photo_white_200dp);
        textView_temp.setText("--");
        textView_text.setText("Conditions N/A");
        textView_location.setText("Location N/A");

        Toast.makeText(MainActivity.this, "Unable to connect to server. Check your internet connection.",
                Toast.LENGTH_LONG).show();
    }

    public int fahrenheitToCelsius(int f){
        return (int)((f-32) / 1.8);
    }
}
