package fernando2.com.helloyahooweatherapi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private Button button_getJson;
    private TextView textView_putJson;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_getJson = (Button) findViewById(R.id.button_getJson);
        textView_putJson = (TextView) findViewById(R.id.textView_putJson);

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

                textView_putJson.setText("La temperatura en Veracruz es: ");
            }
        });

    }

    public void setTemp(String temp){
        dialog.dismiss();
        textView_putJson.setText("La temperatura en Veracruz es: " + temp + "º F");
    }
}
