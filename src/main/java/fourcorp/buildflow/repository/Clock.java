package fourcorp.buildflow.repository;
import java.util.Timer;
import java.util.TimerTask;
public class Clock {

    private Timer timer = new Timer();
    private boolean stopFlag = false;


    public boolean countDownClock(int countdownTime) {

        TimerTask task = new TimerTask() {

            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (timeLeft > 0)
                    timeLeft--;
                else
                    timer.cancel();

            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
        return true;
    }

    public int countUpClock(boolean stop) {
        int countTime = 0;
        TimerTask task = new TimerTask() {
            int count = countTime;

            @Override
            public void run() {
                if (stop == false)
                    count++;
                else
                    timer.cancel();
            }
        };
        timer.scheduleAtFixedRate(task, 0,1000);
        return countTime;
    }
}




