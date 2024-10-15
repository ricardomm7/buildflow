package fourcorp.buildflow.application;

import java.time.LocalDateTime;

public class Runing extends WorkstationWorking {
    private LocalDateTime start;
    private LocalDateTime stop;
    private boolean runingState;


    public Runing() {
        this.start = null;
        this.stop = null;
        this.runingState = false;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getStop() {
        return stop;
    }

    public boolean isRuningState() {
        return runingState;
    }

    public void setStop() {
        this.stop = LocalDateTime.now();
    }

    public void setStart() {
        this.start = LocalDateTime.now();
    }

    public void setRuningState(boolean runing) {
        this.runingState = runing;
    }
}
