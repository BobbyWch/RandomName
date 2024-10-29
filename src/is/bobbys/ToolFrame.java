package is.bobbys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ToolFrame extends JFrame {
    private final Names names = Names.getInstance();

    public ToolFrame() {
        super("设置");
        String s = JOptionPane.showInputDialog("输入root密码");
        if (s == null || !s.equals("NmYYDS")) System.exit(0);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 460);
        setBackground(Color.WHITE);
        setLayout(null);

        Pane pane1 = new Pane("查询概率(查询all列举所有学生的概率)", "查询", (button, input) -> {
            if ("all".equals(input)) {
                StringBuilder builder = new StringBuilder(300);
                boolean type = false;
                for (Names.Entry entry : names.list) {
                    builder.append("姓名：").append(entry.name).append("  ")
                            .append("概率：").append(entry.length()).append(type ? "     " : "\n");
                    type = !type;
                }
                msg(builder.toString());
            } else {
                Names.Entry entry = names.getByName(input);
                if (entry == null) {
                    msg("该学生不存在！");
                } else {
                    msg(entry.name + "    概率：" + entry.length() + "\n(默认值为50)");
                }
            }
        });
        add(pane1);
        pane1.setBounds(5, 5, 390, 80);

        Pane pane2 = new Pane("修改概率", "修改", (button, input) -> {
            Names.Entry entry = names.getByName(input);
            if (entry == null) {
                msg("该学生不存在！");
            } else {
                String var = JOptionPane.showInputDialog("输入新概率");
                try {
                    int len = Integer.parseInt(var);
                    if (len <= 0) throw new NumberFormatException();
                    names.update(input, len);
                    msg("修改成功！");
                } catch (NumberFormatException e) {
                    msg("请输入一个正整数！");
                }
            }
        });
        add(pane2);
        pane2.setBounds(5, 100, 390, 80);

        Pane pane3 = new Pane("添加学生", "添加", (button, input) -> {
            Names.Entry entry = names.getByName(input);
            if (entry != null) {
                msg("该学生已存在！");
                return;
            }
            entry = new Names.Entry(input, names.list.isEmpty() ? null : names.list.get(names.list.size() - 1), 50);
            names.add(entry);
            msg("添加成功！");
        });
        add(pane3);
        pane3.setBounds(5, 195, 390, 80);

        Pane pane4 = new Pane("删除学生", "删除", (button, input) -> {
            Names.Entry entry = names.getByName(input);
            if (entry == null) {
                msg("该学生不存在！");
            } else {
                names.remove(entry);
                msg("删除成功！");
            }
        });
        add(pane4);
        pane4.setBounds(5, 290, 390, 80);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Names.save();
            }
        });
        setVisible(true);
    }

    private void msg(String str) {
        JOptionPane.showMessageDialog(this, str, "消息", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class Pane extends JPanel {
        private final JButton button;
        private final JTextField field;

        public Pane(String label, String buttonText, Action action) {
            super();
            this.button = new JButton(buttonText);
            JLabel label1 = new JLabel(label);
            field = new JTextField();
            field.setToolTipText("学生姓名");
            label1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            setLayout(null);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            add(this.button);
            add(label1);
            add(this.field);
            label1.setBounds(5, 0, 370, 20);
            this.field.setBounds(5, 25, 260, 30);
            this.button.setBounds(280, 25, 100, 30);
            this.field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == '\n') Pane.this.button.doClick();
                }
            });
            this.button.addActionListener(e -> action.action(this.button, field.getText()));
        }
    }

    @FunctionalInterface
    interface Action {
        void action(JButton button, String input);
    }
}