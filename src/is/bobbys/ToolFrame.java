package is.bobbys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ToolFrame extends JFrame {
    private final Names name = Names.getInstance();

    public ToolFrame() {
        super("设置");
        String s=JOptionPane.showInputDialog("输入root密码");
        if (s==null||!s.equals("NmYYDS")) System.exit(0);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 460);
        setBackground(Color.WHITE);
        setLayout(null);

        Pane pane1 = new Pane("查询概率(查询all列举所有学生的概率)", "查询", (button, input) -> {
            if (input.equals("all")) {
                StringBuilder builder = new StringBuilder(300);
                boolean type = false;
                for (Names.Entry entry : name.list) {
                    builder.append("姓名：");
                    builder.append(entry.name);
                    builder.append("  ");
                    builder.append("概率：");
                    builder.append(entry.length());
                    type = !type;
                    builder.append(type ? "     " : "\n");
                }
                msg(builder.toString());
                return;
            }
            Names.Entry entry = name.getByName(input);
            if (entry == null) {
                msg("该学生不存在！");
            } else {
                String builder = entry.name +
                        "    概率：" +
                        entry.length() +
                        "\n(默认值为50)";
                msg(builder);
            }
        });
        add(pane1);
        pane1.setBounds(5, 5, 370, 80);

        Pane pane2 = new Pane("修改概率", "修改", (button, input) -> {
            Names.Entry entry = name.getByName(input);
            if (entry == null) {
                msg("该学生不存在！");
            } else {
                String var = JOptionPane.showInputDialog("输入新概率");
                int len;
                try {
                    len = Integer.parseInt(var);
                    if (len <= 0)
                        throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    msg("请输入一个正整数！");
                    return;
                }
                name.update(input, len);
                msg("修改成功！");
            }
        });
        add(pane2);
        pane2.setBounds(5, 100, 370, 80);

        Pane pane3 = new Pane("添加学生", "添加", (button, input) -> {
            Names.Entry old= name.getByName(input);
            if (old!=null){
                msg("该学生已存在！");
                return;
            }
            Names.Entry entry = new Names.Entry(input, name.endIndex, 50);
            name.add(entry);
            msg("添加成功！");
        });
        add(pane3);
        pane3.setBounds(5, 195, 370, 80);

        Pane pane4 = new Pane("删除学生", "删除", (button, input) -> {
            Names.Entry entry = name.getByName(input);
            if (entry == null) {
                msg("该学生不存在！");
            }else {
                name.remove(entry);
                msg("删除成功！");
            }

        });
        add(pane4);
        pane4.setBounds(5, 290, 370, 80);
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

    private final static class Pane extends JPanel {
        private final JButton button;
        private final JTextField field;

        public Pane(String label, String button, Action action) {
            super();
            this.button = new JButton(button);
            JLabel label1 = new JLabel(label);
            field = new JTextField();
            field.setToolTipText("学生姓名");
            label1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            setLayout(null);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            add(this.button);
            add(label1);
            add(this.field);
            label1.setBounds(5, 0, 300, 20);
            this.field.setBounds(5, 25, 260, 30);
            this.button.setBounds(280, 25, 60, 30);
            this.field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar()=='\n')
                        Pane.this.button.doClick();
                }
            });
            this.button.addActionListener(e -> {
                action.action(this.button, field.getText());
                this.field.setText(null);
            });
        }
    }
}