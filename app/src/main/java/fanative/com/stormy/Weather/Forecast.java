package fanative.com.stormy.Weather;

public class Forecast {
    private CurrentWeather mCurrentWeather;
    private HourlyWeather[] mHourlyWeather;
    private DailyWeather[] mDailyWeather;


    public CurrentWeather getCurrentWeather() {
        return mCurrentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        mCurrentWeather = currentWeather;
    }

    public HourlyWeather[] getHourlyWeather() {
        return mHourlyWeather;
    }

    public void setHourlyWeather(HourlyWeather[] hourlyWeather) {
        mHourlyWeather = hourlyWeather;
    }

    public DailyWeather[] getDailyWeather() {
        return mDailyWeather;
    }

    public void setDailyWeather(DailyWeather[] dailyWeather) {
        mDailyWeather = dailyWeather;
    }
}
