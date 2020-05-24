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
    private Collage collage;

    public static final int collageWidth = 5;
    public static final int collageHeight = 2;
    public static final int collageSize = collageHeight * collageWidth;

    public CollageBuilder(String directoryName, Collage collage) {
        this.collage = collage;
        this.rootDirectory = new File(directoryName);
        this.cardsToCollage = new LinkedList<>();
        this.cards = new TreeMap<>();
        this.usedDirs = new ArrayList<>();
    }

    private Map<String, Card> cards;
    private LinkedList<Card> cardsToCollage;
    private ArrayList<String> usedDirs;

    public BufferedImage createCollage(String prefix) {
        File prefixRoot = getPrefixDir(prefix, rootDirectory);
        gatherCards(prefixRoot, prefix);
        // create result
        usedDirs.clear();
        return imagesSeed();
    }

    /**
     * возвращает папку, в которой хранятся все карточки, начинающиеся на {@param prefix}
     * */
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
    /**
     * собирает все карточки
     * */
    private void gatherCards(File dir, String prefix) {
        Stack<File> dirs = new Stack<>();
        dirs.push(dir);
        while (!dirs.isEmpty()) {
            File currentDir = dirs.pop();
            if(usedDirs.contains(currentDir.getName())) {
                continue;
            }
            usedDirs.add(currentDir.getName());

            String currentPath = currentDir.getAbsolutePath();
            File cardsListFile = new File(currentPath + "\\" + DESCRIPTION_FILE_NAME);

            if(checkToStopAndGatherCardsByList(cardsListFile, prefix, currentPath)) {
                return;
            }
            for (File file : Objects.requireNonNull(currentDir.listFiles(File::isDirectory))) {
                dirs.push(file);
            }
        }
        if(cardsToCollage.size() != collageSize) {
            gatherCards(dir.getParentFile(), prefix);
        }
    }

    private boolean checkToStopAndGatherCardsByList(File cardsListFile, String prefix, String currentPath) {
        try (Scanner scanner = new Scanner(cardsListFile)) {
            while (scanner.hasNext()) {
                String record = scanner.nextLine();
                String[] nameAndDescription = record.split(" ");
                String name = nameAndDescription[0];
                String cardsDescription = "";
                try {
                    cardsDescription = nameAndDescription[1];
                } catch(ArrayIndexOutOfBoundsException ignored) {}
                Card card = cards.get(name);
                if(card == null) {
                    addCard(prefix, currentPath, name, cardsDescription);
                } else {
                    cardsToCollage.add(card);
                }
                if(cardsToCollage.size() == collageSize) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
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

    private BufferedImage imagesSeed() {
        BufferedImage result = new BufferedImage(
                Card.width*collageWidth, Card.height*collageHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = result.createGraphics();
        int x = 0, y = 0;
        int counter = 0;
        while (!cardsToCollage.isEmpty()) {
            Card card = cardsToCollage.pop();
            g.drawImage(card.picture, x, y, null);
            collage.cardsInCollage[counter] = card;
            counter++;
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