package is.bobbys;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("ForLoopReplaceableByForEach")
public final class Names implements java.io.Serializable {
    @Serial
    private final static long serialVersionUID = 25250250;

    private static Names instance = null;

    public static Names getInstance() {
        if (instance == null) {
            instance = read();
            if (instance == null) {
                instance = new Names();
                save();
            } else {
                instance.random = new Random();
            }
        }
        return instance;
    }

    private transient Random random;
    public final ArrayList<Entry> list;
    public int endIndex;

    private Names() {
        random = new Random();
        list = new ArrayList<>(32);
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("选择名字列表");
        chooser.setFileFilter(new FileNameExtensionFilter("文本文件(*.txt)", "txt"));
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream is = new FileInputStream(chooser.getSelectedFile());
                InputStreamReader ir = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(ir);
                String str;
                Entry entry = null;
                while ((str = br.readLine()) != null) {
                    if (str.trim().equals(""))
                        continue;
                    entry = new Entry(str, entry, 50);
                    add(entry);
                }
                br.close();
                ir.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.exit(0);
        }
    }

    public String getRand() {
        int r = random.nextInt(endIndex);
        Entry entry;
        for (int i=0;i<list.size();i++) {
            entry=list.get(i);
            if (entry.isBetween(r)) {
                return entry.name;
            }
        }
        return null;
    }

    public void add(Entry entry) {
        list.add(entry);
        if (entry.endIndex > endIndex)
            endIndex = entry.endIndex;
    }

    public Entry getByName(String name) {
        Entry entry;
        for (int i=0;i<list.size();i++) {
            entry=list.get(i);
            if (entry.name.equals(name)) {
                return entry;
            }
        }
        return null;
    }
    public void remove(Entry entry){
        list.remove(entry);
        order();
    }

    public boolean update(String name, int newLength) {
        Entry entry = getByName(name);
        if (entry == null)
            return false;
        entry.endIndex = entry.startIndex + newLength;
        order();
        return true;
    }
    private void order(){
        Entry temp = list.get(0);
        Entry temp1;
        for (int j = 1, size = list.size(); j < size; j++) {
            temp1 = list.get(j);
            putToNext(temp1, temp);
            temp = temp1;
        }
        endIndex = temp.endIndex;
    }

    public void putToNext(Entry entry, Entry last) {
        entry.endIndex = last.endIndex + entry.length();
        entry.startIndex = last.endIndex;
    }

    public static void save() {
        try {
            FileOutputStream os = new FileOutputStream("Name.dat");
            ObjectOutputStream objectS = new ObjectOutputStream(os);
            objectS.writeObject(getInstance());
            objectS.flush();
            objectS.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Names read() {
        try {
            FileInputStream is = new FileInputStream("Name.dat");
            ObjectInputStream ois = new ObjectInputStream(is);
            return ((Names) ois.readObject());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static final class Entry implements java.io.Serializable {
        private final static long serialVersionUID = 250250;

        public final String name;
        public int startIndex;//include
        public int endIndex;// not include

        public boolean isBetween(int index) {
            return index >= startIndex && index < endIndex;
        }

        public Entry(String name, int beginIndex, int length) {
            this.name = name;
            startIndex = beginIndex;
            endIndex = startIndex + length;
        }

        public Entry(String name, Entry lastEntry, int length) {
            this.name = name;
            startIndex = (lastEntry == null) ? 0 : lastEntry.endIndex;
            endIndex = startIndex + length;
        }

        public int length() {
            return endIndex - startIndex;
        }

        @Override
        public String toString() {
            return name + startIndex + "   " + endIndex;
        }
    }
}