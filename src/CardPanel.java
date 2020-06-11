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
    private static final int WAIT = 4000;
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

        System.out.println(++c + ") может кликать " + gameState.getCurrentState());
        System.out.print(gameState.roundsGlobalState.getRole(0) + " ");
        System.out.print(gameState.roundsGlobalState.getRole(1) + " ");
        //crSystem.out.println(gameState.roundsGlobalState.getRole(2));
    }
    //todo:  main panel - панель настроек - связывание мышек, выбор префикса, выбор размера коллажа 3-6, 2-8 ...
static int c;
    private void actionWithCard(int monitorIndex, Card card) {
        if(!gameState.canClick(monitorIndex)) {
            return;
        }
        State state = gameState.state;
        switch (state) {
            case Questioner:
                onMakeWord(card);
                break;
            case Suggester:
                onSuggest(card);
                break;
            case AllWithoutWhoAnswerer:
                if(!gameState.delayIsUp(monitorIndex)) {
                    return;
                }
                if(!gameState.guessedCardByDescription(card)) {
                    onWrongGuessedCard(card);
                    return;
                }
                if(gameState.roundsGlobalState.getRole(monitorIndex) == Role.Questioner) {
                    boolean isMakeCard = onQuestionerGuessedWord(card);
                    if(isMakeCard) {
                        return;
                    }
                    general.collageBuilder.usedCards.add(card.name);
                    BufferedImage collageImage = general.collageBuilder.createCollage(gameState.getPrefix());
                    for(PlayersFrame frame : general.playersFrames) {
                        frame.switchedPanel.cardPanel.setPicture(collageImage);
                    }
                }
                else if(gameState.roundsGlobalState.getRole(monitorIndex) == Role.Answerer) {
                    if(card.name.equals(gameState.currentCard.name)) {
                        answererGuessedRootWord();

                        general.collageBuilder.usedCards.add(card.name);
                        BufferedImage collageImage = general.collageBuilder.createCollage(gameState.getPrefix());
                        for(PlayersFrame frame : general.playersFrames) {
                            frame.switchedPanel.cardPanel.setPicture(collageImage);
                        }
                    }
                    else {
                        answererGuessedSuggestedWord();

                        general.collageBuilder.usedCards.add(card.name);
                        BufferedImage collageImage = general.collageBuilder.createCollage(gameState.getPrefix());
                        for(PlayersFrame frame : general.playersFrames) {
                            frame.switchedPanel.cardPanel.setPicture(collageImage);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void onWrongGuessedCard(Card card) {
        monologueBar.setText(card.name + " не " + gameState.suggestedCard.description);
        gameState.delay(monitorIndex);
    }

    private void answererGuessedSuggestedWord() {
        general.sendQuestioner("Вы не успели отгадать слово и сообщаете всем следующую букву загаданого вами слова");
        general.sendWhoNotQuestioner("Вы догадались, и загадывающий сообщает вам следующую букву загаданного слова");

        gameState.roundsGlobalState.changeSuggester();
        general.nextLetter();

        gameState.delay();
        try {Thread.sleep(WAIT); } catch (InterruptedException ignore) {}

        general.sendQuestioner("Вы выбрали слово: " + gameState.currentCard.name);
        general.sendAllAnswerers(getIntroForAnswerer());
        general.sendSuggester(getIntroForSuggester());

        gameState.state = State.Suggester;
    }

    private void answererGuessedRootWord() {
        general.sendQuestioner("Ваши соперники успешно отгадали слово");
        general.sendWhoNotQuestioner("Вы отгадали слово: " + gameState.currentCard.name);
        //todo 11111111111111111111111111111111111111111
        gameState.roundsGlobalState.changeSuggester();
        boolean endOfExam = gameState.roundsGlobalState.changeQuestioner();
        gameState.numberOfLetters = 0;
        if(endOfExam) {
            endOfExam();
            return;
        }

        gameState.delay();
        try {Thread.sleep(WAIT); } catch (InterruptedException ignore) {}

        general.sendQuestioner(CardPanel.CHOOSE_CARD);
        general.sendWhoNotQuestioner(CardPanel.WAIT_FOR_CHOOSE_CARD);

        gameState.state = State.Questioner;
    }

    private boolean onQuestionerGuessedWord(Card card) {
        if(card.name.equals(gameState.currentCard.name)) {
            return true;
        }
        general.sendQuestioner("Вы догадались и перехватили возможность противников получить следующую букву");
        String fail = "Вы не получили следующую букву: " +
                "загадывающий перехватил вашу возможность получить следующую букву";
        general.sendWhoNotQuestioner(fail);
        gameState.roundsGlobalState.changeSuggester();

        gameState.delay();
        try {Thread.sleep(WAIT); } catch (InterruptedException ignore) {}

        general.sendQuestioner("Вы выбрали слово: " + gameState.currentCard.name +
                "<br> Подождите, пока кто-нибудь не сделает предположение");
        general.sendAllAnswerers(getIntroForAnswerer());
        general.sendSuggester(getIntroForSuggester());

        gameState.state = State.Suggester;
        return false;
    }

    private void onSuggest(Card card) {
        gameState.suggestedCard = card;
        general.sendSuggester("Вы предположили, что было загадано: " + card.name +
                "<br> Дождитесь, пока кто-нибудь не догадается, что вы имеете в виду");
        general.sendQuestioner("Предположили: это " + card.description + "?");
        general.sendAllAnswerers("Предположили: это " + card.description + "?");

        gameState.state = State.AllWithoutWhoAnswerer;
    }

    private void onMakeWord(Card card) {
        gameState.currentCard = card;
        gameState.numberOfLetters = 1;

        general.sendQuestioner("Вы выбрали слово: " + gameState.currentCard.name +
                "<br> Подождите, пока кто-нибудь не сделает предположение");
        general.sendSuggester(getIntroForSuggester());
        general.sendAllAnswerers(getIntroForAnswerer());

        gameState.state = State.Suggester;
    }

    private void endOfExam() {
        System.out.println("end of game");
    }

    private String getIntroForAnswerer() {
        return "Загадывающий выбрал слово<br> " +
                "слово начинается на: \"" + gameState.getPrefix() +
                "\"<br>Подождите, пока ваш партёр сделает предположение";
    }

    private String getIntroForSuggester() {
        return "Загадывающий выбрал слово<br> " +
                "слово начинается на: \"" + gameState.getPrefix() +
                "\"<br>Сделайте предположение";
    }
}
