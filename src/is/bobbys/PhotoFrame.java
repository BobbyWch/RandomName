package is.bobbys;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PhotoFrame extends JFrame {
    private JLabel imageLabel;

    public PhotoFrame() {
        super("Photo");
        imageLabel = new JLabel();
        this.add(imageLabel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false); // 设置窗口不可调整大小
        this.setSize(400, 400); // 设置窗口大小为 400x400
        this.setLocation(0, 0); // 设置窗口位置到屏幕左上角
        this.setVisible(false); // 初始时不显示窗口
    }

    public void updateImage(String name) {
        try {
            String path = "/is/photos/" + name + ".png";
            URL url = getClass().getResource(path);
            if (url == null) {
                throw new IOException("Resource not found: " + path);
            }
            ImageIcon imageIcon = new ImageIcon(url);
            Image image = imageIcon.getImage();
            if (image == null) {
                throw new IOException("Image not found: " + path);
            }
            Image scaledImage = getScaledImage(image, this.getWidth(), this.getHeight());
            SwingUtilities.invokeLater(() -> {
                imageLabel.setIcon(new ImageIcon(scaledImage));
                this.setVisible(true);
            });
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to load image: " + name + ".png");
        }
    }

    private Image getScaledImage(Image srcImg, int width, int height) {
        double srcWidth = srcImg.getWidth(null);
        double srcHeight = srcImg.getHeight(null);
        double ratio = Math.min((double) width / srcWidth, (double) height / srcHeight);
        int scaledWidth = (int) (srcWidth * ratio);
        int scaledHeight = (int) (srcHeight * ratio);
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, scaledWidth, scaledHeight, null);
        g2.dispose();
        return resizedImg;
    }

    public void showPhoto(String name) {
        updateImage(name);
    }

    public void hidePhoto() {
        this.setVisible(false);
    }
}