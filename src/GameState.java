import gui.card.Card;

public class GameState {
    Card currentCard;
    Card suggestedCard;
    int numberOfLetters;
    Role[] roles;
    boolean mightToSetCard;
    public WhoCanClick currentWhoCanClick;
    public State state;
    public int suggested;

    public boolean canClick(int monitorIndex) {
        Role role = roles[monitorIndex];
        WhoCanClick whoCanClick = currentWhoCanClick;

        changeCurrentWhoCanClick();

        if(role == Role.Question) {
            return whoCanClick == WhoCanClick.Questioner ||
                    whoCanClick == WhoCanClick.AllWithoutOneAnswerer;
        } else if(role == Role.Answer) {
            if(whoCanClick == WhoCanClick.Answerer) {
                return true;
            } else if(whoCanClick == WhoCanClick.AllWithoutOneAnswerer) {
                return suggested != monitorIndex;
            } else {
                return false;
            }
        }
        return false;
    }

    private void changeCurrentWhoCanClick() {
        if(currentWhoCanClick == WhoCanClick.Questioner) {
            if(state == State.Questioner) currentWhoCanClick = WhoCanClick.Answerer;
        }
        else if(currentWhoCanClick == WhoCanClick.Answerer) {
            if(state == State.AllAnswerer) currentWhoCanClick = WhoCanClick.AllWithoutOneAnswerer;
        }
        else if(currentWhoCanClick == WhoCanClick.AllWithoutOneAnswerer) {
            currentWhoCanClick = WhoCanClick.Questioner;
        }
    }

    GameState(int numberOfPlayers) {
        currentWhoCanClick = WhoCanClick.NoOne;
        state = State.Questioner;
        roles = new Role[numberOfPlayers];
        roles[0] = Role.Question;
        for(int i = 1; i < numberOfPlayers; i++) {
            roles[i] = Role.Answer;
        }
    }

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
}
