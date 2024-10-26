package fourcorp.buildflow.application;

import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    private Timer timer;
    private int countTime = 0;
    private boolean running = false;
    private boolean isCounting = false;

    public double countUpClock(boolean stopFlag) {
        double elapsedTime = 0;

        if (stopFlag) {
            if (!running) {
                timer = new Timer();
                running = true;
                countTime = 0;

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        countTime++;
                    }
                };

                timer.scheduleAtFixedRate(task, 0, 1);
                return -1;
            }
        } else {
            if (running) {
                timer.cancel();
                running = false;
                elapsedTime = countTime / 1000.0;
                countTime = 0;
            }
        }
        return elapsedTime;
    }

    // Contagem regressiva
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
