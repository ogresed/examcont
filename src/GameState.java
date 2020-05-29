import gui.card.Card;

public class GameState {
    private static final long GLOBAL_DELAY = 5000;
    private static final long DELAY_FOR_WRONG_CHOSEN_CARD = 1000;
    private static final int ANSWERER_TIMES_DURING_ROUND = 2;
    int numberOfLetters;
    private int round;
    public int suggester;
    private long globalDelay = 0;
    private int[] timesWasAnswerer;
    private long[] delays;
    Card currentCard;
    Card suggestedCard;
    public WhoCanClick currentWhoCanClick;
    public State state;
    Role[] roles;

    GameState(int numberOfPlayers) {
        round = 1;
        questioner = 0;
        suggester = 1;
        state = State.Questioner;
        currentWhoCanClick = WhoCanClick.NoOne;

        timesWasAnswerer = new int[numberOfPlayers];
        delays = new long[numberOfPlayers];
        roles = new Role[numberOfPlayers];
        roles[questioner] = Role.Questioner;
        roles[suggester] = Role.Suggester;

        for(int i = 2; i < numberOfPlayers; i++) {
            roles[i] = Role.Answerer;
        }
    }

    public boolean canClick(int monitorIndex) {
        if(System.currentTimeMillis() - globalDelay <= GLOBAL_DELAY) {
            return false;
        }
        Role role = roles[monitorIndex];
        WhoCanClick whoCanClick = currentWhoCanClick;
        boolean retVal = false;

        if(role == Role.Questioner) {
            retVal = whoCanClick == WhoCanClick.Questioner ||
                    whoCanClick == WhoCanClick.AllWithoutOneAnswerer;
        } else if(role == Role.Answerer) {
            if(whoCanClick == WhoCanClick.Answerer) {
                retVal = true;
            } else if(whoCanClick == WhoCanClick.AllWithoutOneAnswerer) {
                retVal = suggester != monitorIndex;
            }
        }
        return retVal;
    }

    public void changeCurrentWhoCanClick() {
        if(currentWhoCanClick == WhoCanClick.Questioner) {
            if(state == State.AllAnswerer) currentWhoCanClick = WhoCanClick.Answerer;
        }
        else if(currentWhoCanClick == WhoCanClick.Answerer) {
            if(state == State.AllWithoutWhoAnswered) currentWhoCanClick = WhoCanClick.AllWithoutOneAnswerer;
        }
        else if(currentWhoCanClick == WhoCanClick.AllWithoutOneAnswerer) {
            if(state == State.AllAnswerer)
                currentWhoCanClick = WhoCanClick.Answerer;
        }
    }

    //todo по очереди предпологать варианты
    public int questioner;

    public Role getRole(int monitorIndex) {
        return roles[monitorIndex];
    }

    public WhoCanClick getCurrentWhoCanClick() {
        return currentWhoCanClick;
    }

    public void setCurrentWhoCanClick(WhoCanClick whoCanClick) {
        this.currentWhoCanClick = whoCanClick;
    }

    public String getPrefix() {
        return currentCard.name.substring(0, numberOfLetters);
    }

    public boolean guessedCardByDescription(Card card) {
        return suggestedCard.name.equals(card.name);
    }

    public void delay(int monitorIndex) {
        try {
            delays[monitorIndex] = System.currentTimeMillis();
        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    public boolean checkDelay(int monitorIndex) {
        long currentTime = System.currentTimeMillis();
        return currentTime - delays[monitorIndex] >= DELAY_FOR_WRONG_CHOSEN_CARD;
    }

    public boolean globalDelayCheck() {
        return System.currentTimeMillis() - globalDelay > GLOBAL_DELAY;
    }

    public void delay() {
        globalDelay = System.currentTimeMillis();
    }
}
