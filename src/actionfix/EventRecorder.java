package actionfix;

import java.io.*;

public class EventRecorder {
    private FileOutputStream writer;

    public EventRecorder(String fileName) {
        File output = createFile(fileName);
        try {
            writer = new FileOutputStream(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private File createFile(String fileName) {
        File dir = new File(System.getProperty("user.dir"));
        File[] files = dir.listFiles();
        int index = 0;
        while(dirContainsFile(files, fileName, index)) {
            index++;
        }
        String newFileName = getFileName(fileName, index);
        return new File(newFileName);
    }

    private boolean dirContainsFile(File[] files, String fileName, int index) {
        fileName = getFileName(fileName, index);
        for(File file: files) {
            if(file.getName().equals(fileName))
                return true;
        }
        return false;
    }

    private String getFileName(String fileName, int index) {
        if (index != 0)
            return fileName + "(" + index + ")";
        return fileName;
    }

    long start;

    public void begin() {
        start = System.currentTimeMillis();
        try {
            writer.write(("1 раунд\n\nподход 1").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeString(String str) {
        //writer.write(str);
        //
    }

    public void ask(int monitorIndex, String name) {
        try {
            writer.write(("\n" + monitorIndex + " выбрал " + name + " загадка: "
                    + (System.currentTimeMillis() - start)).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void suggest(int monitorIndex, String name, String prefix) {
        try {
            writer.write(( "\n" + "префикс " + prefix + "\n" +
                    monitorIndex + " выбрал " + name + " предположение: "
                    + (System.currentTimeMillis() - start)).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
int podxod = 1;
    public void answer(int monitorIndex) {
        try {
            writer.write(("\n" + monitorIndex +  " ответил:  "
                    + (System.currentTimeMillis() - start) + "\n\nподход "
                    + ++podxod ).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
// todo: нумеровать раунды
