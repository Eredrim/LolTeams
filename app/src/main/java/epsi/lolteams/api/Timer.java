package epsi.lolteams.api;

/**
 * Created by HaDriel on 03/06/2016.
 */
public class Timer {

    private static final float SCALE = 1_000_000F;

    private long mark;

    public Timer() {
        mark = System.nanoTime();
    }

    public void reset() {
        mark = System.nanoTime();
    }

    public float elapsed() {
        return (System.nanoTime() - mark) / SCALE;
    }
}