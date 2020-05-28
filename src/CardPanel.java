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
    }
    // todo : настройка размера коллажа: 3-6, 2-8 ...

    private void actionWithCard(int monitorIndex, Card card) {
        if(!gameState.canClick(monitorIndex)) {
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
                gameState.suggested = monitorIndex;
                gameState.suggestedCard = card;

                monologueBar.setText("Вы предположили, что это: " + card.name + " — " + card.description +
                        "<br> Дождитесь, пока кто-нибудь не догадается, что вы имеете в виду");
                general.sendAllWithoutSuggester("Предположили: это " + card.description + "?");

                gameState.state = State.AllWithoutWhoAnswered;
                break;
            case AllWithoutWhoAnswered:
                if(!gameState.guessedCardByDescription(card)) {
                    monologueBar.setText("Нет, " + card.name + " не " + gameState.suggestedCard.description);
                    ///gameState.delay(monitorIndex);
                    break;
                }
                else {
                    if(gameState.roles[monitorIndex] == Role.Answer) {
                        general.sendAllAnswerers("Вы догадались");
                        try {Thread.sleep(5000); } catch (InterruptedException ignore) {}
                        //gameState.delayForQuestioner();
                        gameState.state = State.AnswererGuessed;

                        gameState.numberOfLetters++;
                        general.sendAllAnswerers("Загадывающий выбрал слово<br> " +
                                "слово начинается на: " + "\"" + gameState.getPrefix() + "\"");

                        gameState.state = State.AllAnswerer;
                    }
                    else if(gameState.roles[monitorIndex] == Role.Question) {
                        monologueBar.setText("Вы догадались и перехватили возможность противников получить следующую букву");
                        general.sendAllAnswerers("Вы не получили следующую букву: " +
                                "загадывающий перехватил вашу возможность получить следующую букву");

                        //gameState.delay();
                        try {Thread.sleep(5000); } catch (InterruptedException ignore) {}

                        monologueBar.setText("Вы выбрали слово: " + gameState.currentCard.name);
                        general.sendAllAnswerers(getIntroForAnswerer());

                        gameState.state = State.QuestionerGuessed;
                    }
                }
                break;
            case QuestionerGuessed:
                break;
            case AnswererGuessed:
                break;
            default:
                break;
        }

        //monologueBar.setText(card.name);
    }

    private String getIntroForAnswerer() {
        return "Загадывающий выбрал слово<br> " +
                "слово начинается на: " + "\"" + gameState.getPrefix() + "\"" +
                "<br>Сделайте предположение";
    }
    // todo : настройка размера коллажа: 3-6, 2-8 ...
}
