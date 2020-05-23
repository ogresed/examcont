package gui.card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

//todo: запомнить ее координаты

public class CollageBuilder {
    private static final String DESCRIPTION_FILE_NAME = "description";
    private static final String PNG = ".png";
    private final File rootDirectory;

    private static final int collageWidth = 5;
    private static final int collageHeight = 2;

    public CollageBuilder(String directoryName) {
        rootDirectory = new File(directoryName);
        cardsToCollage = new LinkedList<>();
        cards = new TreeMap<>();
    }

    Map<String, Card> cards;
    LinkedList<Card> cardsToCollage;

    public BufferedImage createCollage(String prefix) {
        File prefixRoot = getPrefixDir(prefix, rootDirectory);
        gatherCards(prefixRoot, prefix);
        // create result
        return imageSeed();
    }

    private File getPrefixDir(String prefix, File dir) {
        if(prefix.equals("")) {
            return rootDirectory;
        }
        LinkedList<File> dirsList = new LinkedList<>();
        dirsList.add(dir);
        while (!dirsList.isEmpty()) {
            for (File file : Objects.requireNonNull(dirsList.pop().listFiles(File::isDirectory))) {
                if(file.getName().equals(prefix)) {
                    return file;
                } else {
                    dirsList.add(file);
                }
            }
        }
        return null;
    }

    private void gatherCards(File dir, String prefix) {
        Stack<File> dirs = new Stack<>();
        dirs.push(dir);
        while (!dirs.isEmpty()) {
            File currentDir = dirs.pop();
            String currentPath = currentDir.getAbsolutePath();
            File description = new File(currentPath + "\\" + DESCRIPTION_FILE_NAME);

            try {
                Scanner scanner = new Scanner(description);
                while (scanner.hasNext()) {
                    String record = scanner.nextLine();
                    String[] nameAndDescription = record.split(" ");
                    String name = nameAndDescription[0];
                    String cardsDescription = "";
                    try{
                        cardsDescription = nameAndDescription[1];
                    } catch(ArrayIndexOutOfBoundsException ignored) {}
                    Card card = cards.get(name);
                    if(card == null) {
                        addCard(prefix, currentPath, name, cardsDescription);
                    } else {
                        cardsToCollage.add(card);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (File file : Objects.requireNonNull(currentDir.listFiles(File::isDirectory))) {
                dirs.push(file);
            }
        }
    }

    private void addCard(String prefix, String currentPath, String name, String description) {
        String imageName = currentPath + "\\" + name + PNG;
        name = name.substring(2);
        try {
            BufferedImage image = ImageIO.read(new File(imageName));
            Card card = new Card(prefix, name, description, image);
            cards.put(name, card);
            cardsToCollage.add(card);
        } catch (IOException ignored) {
        }
    }

    private BufferedImage imageSeed() {
        BufferedImage result = new BufferedImage(
                Card.width*collageWidth, Card.height*collageHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = result.createGraphics();
        int x = 0, y = 0;
        while (!cardsToCollage.isEmpty()) {
            Card card = cardsToCollage.pop();
            g.drawImage(card.picture, x, y, null);
            x += Card.width;
            if(x >= result.getWidth()) {
                x = 0;
                y += Card.height;
            }
        }
        try {
            ImageIO.write(result, "png", new File("qwe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
// todo: распределить процессорное время в пользу этого приложения или увеличить приоритет потока
// solution: Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
// todo: перед экспериментом расчитывать размер картинки и ресайзить ее исходя из размера экрана