import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StopwatchPanel extends JPanel {
    private final JLabel timeLabel = new JLabel("00:00.000", SwingConstants.CENTER);
    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton resetButton = new JButton("Reset");

    private Timer timer;
    private long startTime;
    private long elapsedTime = 0; // Skumulowany czas w milisekundach
    private boolean running = false;

    public StopwatchPanel() {
        setLayout(new BorderLayout());
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        add(timeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Odświeżanie co 10 ms zapewnia płynne wyświetlanie milisekund
        timer = new Timer(10, (ActionEvent e) -> updateDisplay());

        startButton.addActionListener(e -> start());
        stopButton.addActionListener(e -> stop());
        resetButton.addActionListener(e -> reset());
    }

    private void start() {
        if (!running) {
            // Wznowienie od tego, co było poprzednio
            startTime = System.currentTimeMillis() - elapsedTime;
            timer.start();
            running = true;
        }
    }

    private void stop() {
        if (running) {
            timer.stop();
            // Zapamiętujemy dotychczas upłynięty czas
            elapsedTime = System.currentTimeMillis() - startTime;
            running = false;
        }
    }

    private void reset() {
        timer.stop();
        running = false;
        elapsedTime = 0;
        timeLabel.setText("00:00.000");
    }

    private void updateDisplay() {
        long currentElapsed = System.currentTimeMillis() - startTime;

        long millis = currentElapsed % 1000;
        long totalSeconds = currentElapsed / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        // Jeśli stoper przekroczył godzinę, dynamicznie dodaj godziny na początku
        if (hours > 0) {
            timeLabel.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis));
        } else {
            timeLabel.setText(String.format("%02d:%02d.%03d", minutes, seconds, millis));
        }
    }
}
