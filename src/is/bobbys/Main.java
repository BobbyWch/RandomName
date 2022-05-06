package is.bobbys;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class Main {
    private static Font font;

    public static void main(String[] args) {
        System.out.println(System.getProperties());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        if (args.length == 1) {
            if (args[0].equals("setting")) {
                putMiddle(new ToolFrame());
            }
        } else {
            loadFont();
            JFrame frame = new JFrame("随机点名");
            frame.setResizable(false);
            frame.setSize(350, 200);
            putMiddle(frame);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            frame.getContentPane().add(new Button(font), BorderLayout.CENTER);
            frame.setVisible(true);
        }
    }

    public static void putMiddle(Container container) {
        Rectangle r = new Rectangle();
        r.x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - container.getWidth() / 2;
        r.y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - container.getHeight() / 2;
        r.width = container.getWidth();
        r.height = container.getHeight();
        container.setBounds(r);
    }

    private static void loadFont() {
        File file = new File("font.ttf");
        if (file.exists()) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(60.0f);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
                font = new Font("微软雅黑", Font.PLAIN, 60);
            }
        } else {
            font = new Font("微软雅黑", Font.PLAIN, 60);
        }
    }
}