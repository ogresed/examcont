import gui.card.CollageBuilder;
import utils.Constants;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {

        //new WelcomeFrame();
        CollageBuilder builder = new CollageBuilder(System.getProperty("user.dir") +
                 Constants.RESOURCES_PATH + "\\cards");
        BufferedImage collage = builder.createCollage("пр");
    }
}