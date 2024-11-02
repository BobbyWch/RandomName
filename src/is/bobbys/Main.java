package is.bobbys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("随机点名");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false); // 设置窗口不可调整大小

        JCheckBox alwaysOnTopCheckBox = new JCheckBox("强制置顶(用于PPT播放期间)");
        alwaysOnTopCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    frame.setAlwaysOnTop(true);
                } else {
                    frame.setAlwaysOnTop(false);
                }
            }
        });

        Button button = new Button(new Font("微软雅黑", Font.PLAIN, 60));
        frame.add(button, BorderLayout.CENTER);
        frame.add(alwaysOnTopCheckBox, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}