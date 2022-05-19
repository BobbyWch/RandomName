package is.bobbys;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Button extends JButton {
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
    private boolean started = false;
    private final Names names = Names.getInstance();
    private String lastName;

    Button(Font font) {
        super("换一个");
        setFont(font);
        addActionListener(e -> {
            if (started) {
                stop();
            } else {
                start();
            }
        });
    }

    private void start() {
        executor.execute(func);
    }

    private void stop() {
        started = false;
    }
private final HashSet<String> olds=new HashSet<>();
    private final Runnable func = () -> {
        started = true;
        try {
            while (started) {
                setText(names.getRand());
                Thread.sleep(30L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true){
            if (olds.contains(getText())) {
                setText(names.getRand());
            }else {
                break;
            }
        }
        olds.add(getText());
        if (olds.size()==names.list.size()/2) olds.clear();
    };
}