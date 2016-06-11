package fanative.com.stormy.Weather;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import fanative.com.stormy.R;

public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemp;
    private double mHumidity;
    private double mPrecip;
    private String mSummary;
    private String mTimezone;



    public CurrentWeather(String icon, long time, double temp, double humidity, double precip, String summary, String timezone) {
        mIcon = icon;
        mTime = time;
        mTemp = temp;
        mHumidity = humidity;
        mPrecip = precip;
        mSummary = summary;
        mTimezone = timezone;

}


    public String getIcon() {

        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId(){
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        int iconID = R.drawable.clear_day;
        if(mIcon == "clear-night") {
            iconID = R.drawable.clear_night;
        }
        if(mIcon == "cloudy") {
            iconID = R.drawable.cloudy;
        }
        if(mIcon == "cloudy-night") {
            iconID = R.drawable.cloudy_night;
        }
        if(mIcon == "fog") {
            iconID = R.drawable.fog;
        }
        if(mIcon == "partly-cloudy") {
            iconID = R.drawable.partly_cloudy;
        }
        if(mIcon == "rain") {
            iconID = R.drawable.rain;
        }
        if(mIcon == "sleet") {
            iconID = R.drawable.sleet;
        }
        if(mIcon == "snow") {
            iconID = R.drawable.snow;
        }
        if(mIcon == "sunny") {
            iconID = R.drawable.sunny;
        }
        if(mIcon == "wind") {
            iconID = R.drawable.wind;
        }
        return iconID;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date date = new Date(getTime()*1000);
        String timeString = formatter.format(date);
        return timeString;
    }

    public int getTemp() {
        return (int)Math.round(mTemp);
    }

    public void setTemp(double temp) {
        mTemp = temp;
    }

    public double getHumidity() {

        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecip() {
        return (int)Math.round(mPrecip*100);
    }

    public void setPrecip(double precip) {
        mPrecip = precip;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

}
