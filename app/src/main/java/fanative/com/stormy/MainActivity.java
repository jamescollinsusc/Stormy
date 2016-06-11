package fanative.com.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import fanative.com.stormy.UI.AlertDialogFragment;
import fanative.com.stormy.Weather.CurrentWeather;
import fanative.com.stormy.Weather.DailyWeather;
import fanative.com.stormy.Weather.Forecast;
import fanative.com.stormy.Weather.HourlyWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //POPULAR APIS
//    YouTube API
//    Twitter Search API
//    Treehouse Blog API
    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.tempView) TextView mTempView;
    @BindView(R.id.timeView) TextView mTimeView;
    @BindView(R.id.summaryView) TextView mSummaryView;
    @BindView(R.id.cityView) TextView mCityView;
    @BindView(R.id.refreshView) ImageButton mRefreshView;
    @BindView(R.id.degreeView) ImageView mDegreeView;
    @BindView(R.id.iconView) ImageView mIconView;
    @BindView(R.id.humidityLabel) TextView mHumidityLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipLabel) TextView mPrecipLabel;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    private int mIconID = R.drawable.snow;
    private Forecast mForecast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);
        mCityView.setText("Austin, TX");
        mHumidityLabel.setText("HUMIDITY");
        mPrecipLabel.setText("PRECIPITATION");
        mDegreeView.setImageResource(R.drawable.degree);
        final Double latitude = 30.256045;
        final Double longitude = -97.764027;
        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);
            }
        });
        getForecast(latitude,longitude);

    }

    private void getForecast (Double latitude, double longitude) {
        String apiKey = "b807704e94df3860c103b1fa0982494e";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;
        if (!networkAvailable()){
            Toast.makeText(this, R.string.noNetwork, Toast.LENGTH_LONG).show();
        }
        else {
            toggleProgressBar();
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleProgressBar();
                        }
                    });
                    try {
                        String data = response.body().string();
                        Log.v(TAG, data);
                        if (response.isSuccessful()) {
                            Log.v(TAG, getString(R.string.successMessage));
                            mForecast = getForecastDetails(data);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

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

            Log.d(TAG, "IT RUNS");
        }
    }

    private void toggleProgressBar() {
        if(mProgressBar.getVisibility()==View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshView.setVisibility(View.INVISIBLE);
        }
        else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        CurrentWeather current = mForecast.getCurrentWeather();
        mTempView.setText(current.getTemp() + "");
        mTimeView.setText(current.getFormattedTime() + "");
        mIconView.setImageResource(current.getIconId());
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecip() + "%");
        mSummaryView.setText(current.getSummary() + "");
        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconView.setImageDrawable(drawable);
    }


    private Forecast getForecastDetails(String JSONdata) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrentWeather(getCurrentDetails(JSONdata));
        forecast.setHourlyWeather(getHourlyDetails(JSONdata));
        forecast.setDailyWeather(getDailyDetails(JSONdata));
        return forecast;
    }

    private HourlyWeather[] getHourlyDetails(String data) throws JSONException{
        JSONObject forecast = new JSONObject(data);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray dataArray = hourly.getJSONArray("data");
        HourlyWeather[] hourlyArray = new HourlyWeather[dataArray.length()];
        for(int i = 0; i < dataArray.length(); ++i){
            HourlyWeather temp = new HourlyWeather();
            temp.setIcon(dataArray.getJSONObject(i).getString("icon"));
            temp.setTemp(dataArray.getJSONObject(i).getDouble("temperature"));
            temp.setSummary(dataArray.getJSONObject(i).getString("summary"));
            temp.setTime(dataArray.getJSONObject(i).getLong("time"));
            temp.setTimezone(timezone);
            hourlyArray[i]=temp;
        }
        return hourlyArray;
    }

    private DailyWeather[] getDailyDetails(String data) throws JSONException{
        JSONObject forecast = new JSONObject(data);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dataArray = daily.getJSONArray("data");
        DailyWeather[] dailyArray = new DailyWeather[dataArray.length()];
        for(int i = 0; i < dataArray.length(); ++i){
            DailyWeather temp = new DailyWeather();
            temp.setIcon(dataArray.getJSONObject(i).getString("icon"));
            temp.setMaxTemp(dataArray.getJSONObject(i).getDouble("temperatureMax"));
            temp.setSummary(dataArray.getJSONObject(i).getString("summary"));
            temp.setTime(dataArray.getJSONObject(i).getLong("time"));
            temp.setTimezone(timezone);
            dailyArray[i]=temp;
        }
        return dailyArray;
    }

    private CurrentWeather getCurrentDetails(String data) throws JSONException {
        JSONObject forecast = new JSONObject(data);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONObject currently = forecast.getJSONObject("currently");
//        JSONObject hourly = forecast.getJSONObject("hourly");
//        JSONObject daily = forecast.getJSONObject("daily");
        String icon = currently.getString("icon");
        Long time = currently.getLong("time");
        Double temp = currently.getDouble("temperature");
        Double humidity = currently.getDouble("humidity");
        Double precip = currently.getDouble("precipProbability");
        String summary = currently.getString("summary");
        CurrentWeather currentWeather = new CurrentWeather(icon,time,temp,humidity,precip,summary,timezone);
        Log.d(TAG, currentWeather.getFormattedTime());
        Log.d(TAG, currentWeather.getIcon());
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
