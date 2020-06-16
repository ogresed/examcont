package actionfix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class EventRecorder {
    private PrintWriter writer;

    public EventRecorder(String fileName) throws FileNotFoundException {
        File output = createFile(fileName);
        writer = new PrintWriter(output);
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

    public void writeString(String str) {
        writer.write(str);
    }
}
