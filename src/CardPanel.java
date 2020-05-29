import coordinates.Coordinates;
import gui.card.Card;
import gui.card.CollageBuilder;
import gui.view.MonologueBar;
import gui.view.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TreeMap;

public class CardPanel extends JPanel {
    public static final String CHOOSE_CARD = "Выберите карточку, кликнув по ней";
    public static final String WAIT_FOR_CHOOSE_CARD = "Дождитесь, пока ваш соперник выберет карточку";
    int monitorIndex;
    ManyMouseObserver mouseObserver;
    GeneralLogic general;
    GameState gameState;
    TreeMap<Integer, Integer> winDevMap;

    StatusBar downBar;
    MonologueBar monologueBar;

    private volatile Image showedImage = null;

    public CardPanel(int index, GeneralLogic generalLogic,
                     StatusBar downBar, MonologueBar monologueBar) {
        this.monitorIndex = index;
        this.mouseObserver = generalLogic.mouseObserver;
        this.general = generalLogic;
        this.gameState = generalLogic.gameState;
        this.downBar = downBar;
        this.monologueBar = monologueBar;
        this.winDevMap = generalLogic.windowDevise;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            g.drawImage(showedImage, 0, 0, null);
        } catch (NullPointerException ignored) {
        }
        try {
            int deviceIndex = winDevMap.get(monitorIndex);
            Coordinates coordinates = mouseObserver.getCoordinates(deviceIndex);
            g.drawImage(PlayersFrame.cursor, coordinates.getX(), coordinates.getY(), null);
        } catch (IndexOutOfBoundsException ignored) {}
        repaint();
    }

    public void setPicture(BufferedImage collage) {
        showedImage = collage;
    }

    public void setChooseAction() {
        mouseObserver.setAction(monitorIndex, this::onCardChooseAction);
    }
static int c;
    void onCardChooseAction(int xC, int yC) {
        if(!general.clickable) {
            return;
        }
        int x = xC / Card.width;
        int y = yC / Card.height;
        int cardIndex = x + y * CollageBuilder.collageWidth;
        if(cardIndex > CollageBuilder.collageSize - 1 || x > CollageBuilder.collageWidth) {
            return;
        }
        actionWithCard(monitorIndex, general.collage.cardsInCollage[cardIndex]);

        System.out.println(++c + ") может кликать " + gameState.getCurrentWhoCanClick().name());
        System.out.println("статус " + gameState.state);
    }
    //todo:  main panel - панель настроек - связывание мышек, выбор префикса, выбор размера коллажа 3-6, 2-8 ...

    private void actionWithCard(int monitorIndex, Card card) {
        if(!gameState.canClick(monitorIndex)) {
            System.out.println("cant click");
            return;
        }
        State state = gameState.state;
        switch (state) {
            case Questioner:
                gameState.currentCard = card;
                gameState.numberOfLetters++;

                monologueBar.setText("Вы выбрали слово: " + gameState.currentCard.name);
                general.sendAllAnswerers(getIntroForAnswerer());

                gameState.state = State.AllAnswerer;
                break;
            case AllAnswerer:
                gameState.suggester = monitorIndex;
                gameState.suggestedCard = card;

                monologueBar.setText("Вы предположили, что было загадано: " + card.name +
                        "<br> Дождитесь, пока кто-нибудь не догадается, что вы имеете в виду");
                general.sendAllWithoutSuggester("Предположили: это " + card.description + "?");

                gameState.state = State.AllWithoutWhoAnswered;
                break;
            case AllWithoutWhoAnswered:
                if(!gameState.checkDelay(monitorIndex)) {
                    return;
                }
                if(!gameState.guessedCardByDescription(card)) {
                    monologueBar.setText(card.name + " не " + gameState.suggestedCard.description);
                    gameState.delay(monitorIndex);
                    return;
                }
                else {
                    if(gameState.roles[monitorIndex] == Role.Questioner) {
                        monologueBar.setText("Вы догадались и перехватили возможность противников получить следующую букву");
                        general.sendAllAnswerers("Вы не получили следующую букву: " +
                                "загадывающий перехватил вашу возможность получить следующую букву");

                        gameState.delay();
                        try {Thread.sleep(5000); } catch (InterruptedException ignore) {}

                        monologueBar.setText("Вы выбрали слово: " + gameState.currentCard.name);
                        general.sendAllAnswerers(getIntroForAnswerer());

                        gameState.state = State.AllAnswerer;
                    }
                    else if(gameState.roles[monitorIndex] == Role.Answerer) {
                        return;
                        /*general.sendAllAnswerers("Вы догадались, и загадывающий сообщает вам следующую букву загаданного слова");
                        general.sendQuestioner("Вы не успели отгадать слово и сообщаете всем следующую букву загаданого вами слова");
                        try {Thread.sleep(5000); } catch (InterruptedException ignore) {}
                        //gameState.delayForQuestioner();
                        gameState.state = State.AnswererGuessed;

                        gameState.numberOfLetters++;
                        general.sendQuestioner("Вы выбрали слово: " + gameState.currentCard.name);
                        general.sendAllAnswerers(getIntroForAnswerer());

                        gameState.state = State.AllAnswerer;*/
                    }
                }
                break;
            case AnswererGuessed:
                break;
            case QuestionerGuessed:
                break;
            default:
                break;
        }
        gameState.changeCurrentWhoCanClick();
    }

    private String getIntroForAnswerer() {
        return "Загадывающий выбрал слово<br> " +
                "слово начинается на: " + "\"" + gameState.getPrefix() + "\"" +
                "<br>Сделайте предположение";
    }
}
