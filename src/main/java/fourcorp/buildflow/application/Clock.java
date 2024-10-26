package fourcorp.buildflow.application;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The {@code Clock} class provides utility methods for time-related operations such as
 * counting up and counting down. It allows measuring elapsed time with a count-up timer
 * and executing a callback when a countdown reaches zero.
 *
 * <p>The class uses {@link Timer} and {@link TimerTask} for scheduling time-based tasks.
 */
public class Clock {
    private Timer timer;
    private boolean isCounting = false;

    /**
     * Starts a countdown timer for the specified duration in milliseconds. When the countdown
     * reaches zero, the provided {@link Runnable} callback is executed. If a countdown is
     * already in progress, the method returns without starting a new one.
     *
     * @param countdownTimeMillis the duration of the countdown in milliseconds
     * @param callback the callback to execute when the countdown reaches zero
     */
    public void countDownClock(int countdownTimeMillis, Runnable callback) {
        if (isCounting) {
            return;
        }

        timer = new Timer();
        isCounting = true;

        TimerTask task = new TimerTask() {
            int timeLeft = countdownTimeMillis;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                } else {
                    timer.cancel();
                    isCounting = false;
                    callback.run();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1);
    }
}
