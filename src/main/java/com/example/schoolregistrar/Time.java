package com.example.schoolregistrar;

public class Time {

    private int hour;
    private int minute;
    private String timeOfDay;

    public Time() {
        hour = 11;
        minute = 59;
        timeOfDay = "PM";
    }

    public Time(int hour, int minute, String timeOfDay) {
        setTime(hour, minute);
        this.timeOfDay = timeOfDay;
    }

    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setHour(int hour) {
        if (hour >= 0 && hour <= 23) {
            this.hour = hour;
        }
        else {
            this.hour = 0;
        }
    }
    public void setMinute(int minute) {
        if (minute >=0 && minute <= 59) {
            this.minute = minute;
        }
        else {
            this.minute = 0;
        }
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
    public void setTime(int h, int m) {
        setHour(h);
        setMinute(m);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d %s", hour, minute, timeOfDay);
    }

}
