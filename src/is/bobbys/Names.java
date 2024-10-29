package is.bobbys;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public final class Names implements Serializable {
    private static final long serialVersionUID = 25250250L;
    private static Names instance;
    public static String path = "Name.dat";
    public static HashSet<String> oldNames = new HashSet<>(); // 静态HashSet，用于记录旧的名字

    private transient Random random;
    public final ArrayList<Entry> list = new ArrayList<>();
    public int endIndex;

    private Names() {
        random = new Random();
        endIndex = 0;
        readData();
        readOldNames();
    }

    public static Names getInstance() {
        if (instance == null) {
            instance = new Names();
        }
        return instance;
    }

    public String getRand() {
        int r = random.nextInt(endIndex);
        for (Entry entry : list) {
            if (entry.isBetween(r)) {
                return entry.name;
            }
        }
        return null;
    }

    public void add(Entry entry) {
        list.add(entry);
        if (entry.endIndex > endIndex) {
            endIndex = entry.endIndex;
        }
    }

    public Entry getByName(String name) {
        for (Entry entry : list) {
            if (entry.name.equals(name)) {
                return entry;
            }
        }
        return null;
    }

    public void remove(Entry entry) {
        list.remove(entry);
        order();
    }

    public boolean update(String name, int newLength) {
        Entry entry = getByName(name);
        if (entry == null) {
            return false;
        }
        entry.endIndex = entry.startIndex + newLength;
        order();
        return true;
    }

    private void order() {
        int index = 0;
        for (Entry entry : list) {
            entry.startIndex = index;
            entry.endIndex = index + entry.length();
            index = entry.endIndex;
        }
        endIndex = index;
    }

    public static void save() {
        try {
            FileOutputStream os = new FileOutputStream(path);
            ObjectOutputStream objectS = new ObjectOutputStream(os);
            objectS.writeObject(getInstance());
            objectS.flush();
            objectS.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveOldNames();
    }

    public void confirmName(String name) {
        if (!oldNames.contains(name)) {
            oldNames.add(name);
            saveOldNames();
        }
    }

    private void readData() {
        File file = new File(path);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Names names = (Names) ois.readObject();
                list.clear();
                list.addAll(names.list);
                endIndex = names.endIndex;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void readOldNames() {
        File file = new File("oldNames.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                oldNames = (HashSet<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveOldNames() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("oldNames.dat"))) {
            oos.writeObject(oldNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<String> getOldNames() {
        return oldNames;
    }

    public static final class Entry implements Serializable {
        private static final long serialVersionUID = 250250L;

        public final String name;
        public int startIndex;
        public int endIndex;

        public Entry(String name, Entry lastEntry, int length) {
            this.name = name;
            if (lastEntry == null) {
                this.startIndex = 0;
            } else {
                this.startIndex = lastEntry.endIndex;
            }
            this.endIndex = this.startIndex + length;
        }

        public boolean isBetween(int index) {
            return index >= startIndex && index < endIndex;
        }

        public int length() {
            return endIndex - startIndex;
        }
    }
}