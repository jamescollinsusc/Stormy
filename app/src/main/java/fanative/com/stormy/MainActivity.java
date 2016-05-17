package fanative.com.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //hello
    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;
    private ImageView iconView;
    private int mIconID = R.drawable.snow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "b807704e94df3860c103b1fa0982494e";
        Double latitude = 30.256045;
        Double longitude = -97.764027;
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;
        iconView = (ImageView)findViewById(R.id.iconView);
        if (!networkAvailable()){
            Toast.makeText(this, R.string.noNetwork, Toast.LENGTH_LONG).show();
        }
        else {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String data = response.body().string();
                        Log.v(TAG, data);
                        if (response.isSuccessful()) {
                            Log.v(TAG, getString(R.string.successMessage));
                            mCurrentWeather = getCurrentDetails(data);
                            mIconID = mCurrentWeather.getIconId();
                        } else {
                            alertUserError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }
                }
            });
            iconView.setImageResource(mIconID);
            Log.d(TAG, "IT RUNS");
        }

    }

    private CurrentWeather getCurrentDetails(String data) throws JSONException {
        JSONObject forecast = new JSONObject(data);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONObject currently = forecast.getJSONObject("currently");
        String icon = currently.getString("icon");
        Long time = currently.getLong("time");
        Double temp = currently.getDouble("temperature");
        Double humidity = currently.getDouble("humidity");
        Double precip = currently.getDouble("precipProbability");
        String summary = currently.getString("summary");
        CurrentWeather currentWeather = new CurrentWeather(icon,time,temp,humidity,precip,summary,timezone);
        Log.d(TAG, currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean networkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserError() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(), "errorDialog");

    }


}
