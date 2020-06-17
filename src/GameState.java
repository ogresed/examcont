import actionfix.EventRecorder;
import gui.card.Card;

public class GameState {
    private static final long GLOBAL_DELAY = 4000;
    private static final long DELAY_FOR_WRONG_CHOSEN_CARD = 1000;

		int numberOfLetters;
    private long globalDelay = 0;
    private long[] delays;
		Card currentCard;
		Card suggestedCard;
	public State state;
    RoundsGlobalState roundsGlobalState;
    EventRecorder eventRecorder;

    GameState(int numberOfPlayers) {
        this.eventRecorder = new EventRecorder("journal.txt");
        roundsGlobalState = new RoundsGlobalState(numberOfPlayers);
        state = State.Questioner;
        delays = new long[numberOfPlayers];
    }

    public boolean canClick(int monitorIndex) {
        if(System.currentTimeMillis() - globalDelay <= GLOBAL_DELAY) {
            return false;
        }
        Role role = roundsGlobalState.getRole(monitorIndex);
        boolean retVal = false;

        if(role == Role.Questioner) {
            retVal = state == State.Questioner ||
                    state == State.AllWithoutWhoAnswerer;
        }
        else if(role == Role.Answerer) {
            if(state == State.AllAnswerer) {
                retVal = true;
            } else if(state == State.AllWithoutWhoAnswerer) {
                retVal = roundsGlobalState.getSuggester() != monitorIndex;
            }
        }
        else if(role == Role.Suggester) {
            retVal = state == State.Suggester;
        }
        return retVal;
    }

    public void changeCurrentWhoCanClick() {
        /*if(currentWhoCanClick == WhoCanClick.Questioner) {
            if(state == State.AllAnswerer) currentWhoCanClick = WhoCanClick.Answerer;
        }
        else if(currentWhoCanClick == WhoCanClick.Answerer) {
            if(state == State.AllWithoutWhoAnswered) currentWhoCanClick = WhoCanClick.AllWithoutOneAnswerer;
        }
        else if(currentWhoCanClick == WhoCanClick.AllWithoutOneAnswerer) {
            if(state == State.AllAnswerer)
                currentWhoCanClick = WhoCanClick.Answerer;
        }*/
    }

    //todo по очереди предпологать варианты

    public Role getRole(int monitorIndex) {
        return roundsGlobalState.getRole(monitorIndex);
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

    public boolean delayIsUp(int monitorIndex) {
        long currentTime = System.currentTimeMillis();
        return currentTime - delays[monitorIndex] >= DELAY_FOR_WRONG_CHOSEN_CARD;
    }

    public boolean globalDelayCheck() {
        return System.currentTimeMillis() - globalDelay > GLOBAL_DELAY;
    }

    public void delay() {
        globalDelay = System.currentTimeMillis();
    }

    public String getCurrentState() {
        return state.name();
    }
}
