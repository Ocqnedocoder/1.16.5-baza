package ru.levelup.client.api.utils.misc;

public class TimerUtil {
    public long mc = System.currentTimeMillis();

    public void reset() {
        this.mc = System.currentTimeMillis();
    }

    public long getMc() {
        return System.currentTimeMillis() - this.mc;
    }

    public boolean hasReached(int n) {
        return System.currentTimeMillis() - this.mc > (long)n;
    }

    public boolean hasReached(double milliseconds) {
        return getTimePassed() >= milliseconds;
    }

    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - mc > time;
    }

    public long getTime() {
        return System.currentTimeMillis() - mc;
    }

    public static TimerUtil create() {
        return new TimerUtil();
    }

    public long getLastMS() {
        return mc;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - mc;
    }

    public long getCurrentTime() {
        return System.nanoTime() / 1000000L;
    }

    public void setTime(long time) {
        mc = time;
    }
}