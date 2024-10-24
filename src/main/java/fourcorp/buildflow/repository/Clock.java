package fourcorp.buildflow.repository;
import java.util.Timer;
import java.util.TimerTask;
public class Clock {

    private Timer timer = new Timer();
    private boolean stopFlag = false;
    private int countTime = 0;
    private boolean running = false;
    private boolean isCounting = false;


    public int countUpClock(boolean stopFlag) {
        int elapsedTime = 0;
        if (stopFlag) {
            if (!running) {  // Se não estiver runig, inicia
                timer = new Timer();
                running = true;

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        countTime++;
                    }
                };

                timer.scheduleAtFixedRate(task, 0, 100);  // Incrementa a cada 1 segundo
                return -1;
            }
        } else {
            // Parar o contador e retornar o tempo decorrido
            if (running) {
                timer.cancel();
                running = false;
                elapsedTime = countTime;
                countTime = 0;

            }
        }
        return elapsedTime;
    }



        public boolean countDownClock(int countdownTime) {
            if (isCounting) {
                return false;  // Impede que o temporizador seja iniciado novamente enquanto funciona
            }

            timer = new Timer();
            isCounting = true;  // Marca que o relógio começou

            TimerTask task = new TimerTask() {
                int timeLeft = countdownTime;

                @Override
                public void run() {
                    if (timeLeft > 0) {
                        timeLeft--;
                    } else {
                        timer.cancel();  // Para o temporizador
                        isCounting = false;  // Marca que a contagem terminou

                    }
                }
            };

            // Executa a tarefa a cada 1 segundo
            timer.scheduleAtFixedRate(task, 0, 100);
            return true;  // Retorna true quando o temporizador é iniciado
        }
}




