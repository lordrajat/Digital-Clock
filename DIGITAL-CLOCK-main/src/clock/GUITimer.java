package clock;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

public class GUITimer extends JFrame {

    private static final long serialVersionUID = 5924880907001755076L;

    JLabel jltime;
    JTextField tfHours, tfMinutes, tfSeconds;
    JButton jbtStart, jbtReset, jbtStop;
    Timer timer;
    NumberFormat format;
    long initial, remaining;

    public GUITimer() {
        // Timer display panel
        JPanel timePanel = new JPanel();
        jltime = new JLabel("00:00:00");
        jltime.setFont(new Font("DS-Digital", Font.BOLD, 96));
        jltime.setForeground(Color.BLUE);
        jltime.setHorizontalAlignment(SwingConstants.CENTER);
        timePanel.add(jltime);

        // Input panel
        JPanel jp1 = new JPanel(new FlowLayout());
        jp1.add(new JLabel("Hours:"));
        tfHours = new JTextField(2);
        jp1.add(tfHours);
        jp1.add(new JLabel("Minutes:"));
        tfMinutes = new JTextField(2);
        jp1.add(tfMinutes);
        jp1.add(new JLabel("Seconds:"));
        tfSeconds = new JTextField(2);
        jp1.add(tfSeconds);

        // Start, Stop, and Reset buttons
        jbtStart = new JButton("START");
        jbtStop = new JButton("STOP");
        jbtReset = new JButton("RESET");
        jp1.add(jbtStart);
        jp1.add(jbtStop);
        jp1.add(jbtReset);

        getContentPane().add(jp1, BorderLayout.NORTH);
        getContentPane().add(timePanel, BorderLayout.CENTER);

        jbtStart.addActionListener(e -> startTimer());
        jbtStop.addActionListener(e -> stopTimer());
        jbtReset.addActionListener(e -> resetTimer());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Custom Timer with Sound");
        setSize(400, 300);
        setVisible(true);
    }

    // Method to start the timer
    void startTimer() {
        remaining = convertTime(); // Get initial remaining time
        initial = System.currentTimeMillis();
        timer = new Timer(1000, new Timeclass());
        timer.start();
    }

    // Method to stop the timer
    void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    // Method to reset the timer display
    void resetTimer() {
        jltime.setText("00:00:00");
        if (timer != null) {
            timer.stop();
        }
    }

    // Method to play alarm sound
    private void playAlarmSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\hridy\\Downloads\\mixkit-classic-alarm-995.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    // Convert hours, minutes, and seconds to milliseconds
    public long convertTime() {
        long hours = Long.parseLong(tfHours.getText());
        long minutes = Long.parseLong(tfMinutes.getText());
        long seconds = Long.parseLong(tfSeconds.getText());
        return (hours * 3600000) + (minutes * 60000) + (seconds * 1000);
    }

    // ActionListener for the timer countdown
    public class Timeclass implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            long current = System.currentTimeMillis();
            long elapsed = current - initial;
            remaining -= elapsed;
            initial = current;

            if (remaining < 0) remaining = 0;
            int hours = (int) (remaining / 3600000);
            int minutes = (int) ((remaining % 3600000) / 60000);
            int seconds = (int) ((remaining % 60000) / 1000);
            format = NumberFormat.getNumberInstance();
            format.setMinimumIntegerDigits(2);
            jltime.setText(format.format(hours) + ":" + format.format(minutes) + ":" + format.format(seconds));

            if (remaining == 0) {
                jltime.setText("00:00:00");
                timer.stop();
                playAlarmSound();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUITimer::new);
    }
}
