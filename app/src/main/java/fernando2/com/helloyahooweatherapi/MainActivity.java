package fernando2.com.helloyahooweatherapi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private Button button_getJson;
    private TextView textView_showTemp;
    private TextView textView_showLocation;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_getJson = (Button) findViewById(R.id.button_getJson);
        textView_showTemp = (TextView) findViewById(R.id.textView_showTemp);
        textView_showLocation = (TextView) findViewById(R.id.textView_showLocation);

        button_getJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Cargando...");
                dialog.show();

                YahooWeather yahooWeather = new YahooWeather(MainActivity.this);

                try {
                    yahooWeather.getTemp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void showData(int temp, String location){

        temp = fahrenheitToCelsius(temp);

        dialog.dismiss();
        textView_showTemp.setText(temp + " ºC");
        textView_showLocation.setText(location);

    }

    public void showErrorMsg(){
        dialog.dismiss();

        textView_showTemp.setText("N/A");
        textView_showLocation.setText("Location N/A");

        Toast.makeText(MainActivity.this, "Ocurrió un error al conectarse al servidor. Verifica tu conexión a Internet.",
                Toast.LENGTH_LONG).show();
    }

    public int fahrenheitToCelsius(int f){
        return (int)((f-32) / 1.8);
    }
}
