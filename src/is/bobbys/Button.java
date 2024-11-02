package is.bobbys;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Button extends JButton {
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 120, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
    private boolean started = false;
    private final Names names = Names.getInstance();
    private PhotoFrame photoFrame;
    private String lastName;

    Button(Font font) {
        super("换一个");
        setFont(font);
        photoFrame = new PhotoFrame(); // 初始化PhotoFrame
        addActionListener(e -> {
            if (started) {
                stop();
            } else {
                start();
            }
        });
    }

    private void start() {
        if (!started) {
            executor.execute(this::updateName);
            started = true;
        }
    }

    private void stop() {
        started = false;
        SwingUtilities.invokeLater(() -> {
            setText(lastName); // 显示最后选中的人的名字
            photoFrame.showPhoto(lastName); // 显示最后选中的人的照片
        });
    }

    private void updateName() {
        try {
            while (started) {
                String name = names.getRand();
                if (name != null && !name.equals(lastName)) {
                    lastName = name;
                    SwingUtilities.invokeLater(() -> {
                        setText(name); // 更新按钮文本
                        photoFrame.showPhoto(name); // 更新图片
                    });
                }
                Thread.sleep(30L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}