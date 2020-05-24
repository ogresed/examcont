import gui.card.Card;

public class GameState {
    Card currentCard;
    int numberOfLetters;
    Role[] roles;
    boolean mightToSetCard;

    public enum Role {
        Answer, Question
    }

    GameState(int numberOfPlayers) {
        roles = new Role[numberOfPlayers];
        roles[0] = Role.Question;
        for(int i = 1; i < numberOfPlayers; i++) {
            roles[i] = Role.Answer;
        }
    }

    public Role getRole(int monitorIndex) {
        return roles[monitorIndex];
    }
}
