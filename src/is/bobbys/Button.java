package is.bobbys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Button extends JButton {
    private final Names names = Names.getInstance();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean started = false;

    Button(Font font) {
        super("换一个");
        setFont(font);
        setFocusable(false);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (started) {
                    stop();
                } else {
                    start();
                }
            }
        });
    }

    private void start() {
        started = true;
        executor.scheduleWithFixedDelay(() -> {
            String name = names.getRand();
            if (name != null) {
                SwingUtilities.invokeLater(() -> setText(name));
            }
        }, 0, 30, TimeUnit.MILLISECONDS);
    }

    private void stop() {
        started = false;
        executor.shutdownNow();
        SwingUtilities.invokeLater(() -> {
            String name = getText();
            if (name != null && !name.isEmpty()) {
                showConfirmationDialog(name);
            }
        });
    }

    private void showConfirmationDialog(String name) {
        int result = JOptionPane.showConfirmDialog(Button.this, "确认选择 " + name + " 吗?", "确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            names.confirmName(name);
            System.exit(0);
        }
    }
}