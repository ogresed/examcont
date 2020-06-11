public class RoundsGlobalState {
    private static final int ANSWERER_TIMES_DURING_ROUND = 2;
		private static final int totalRoundsNumber = 3;
    private static final int totalActionPerRound = 4;

    private int numberOfAction;
		private int numberOfRounds;

    private int numberOfActors;

		private int suggester;
		private int questioner;	

    private Role[] roles;
    private int[] timesWasAnswerer;

    private int howMuchWasQuestioners;
    private int howMuchWasAnswerer;

    public RoundsGlobalState(int numberOfActors) {
        this.numberOfActors = numberOfActors;
        roles = new Role[numberOfActors];
        timesWasAnswerer = new int[numberOfActors];

        questioner = 0;
        suggester = 1;

        roles[questioner] = Role.Questioner;
        roles[suggester] = Role.Suggester;

        for(int i = 2; i < numberOfActors; i++) {
            roles[i] = Role.Answerer;
        }
    }
    // change
    public void newRound() {

    }
    // change suggester and answerer, don't touch questioner
    public void newAction() {

    }

    public boolean readyToChangeRound() {
        return totalRoundsNumber <=  numberOfRounds;
    }

    public int getSuggester() {
        return suggester;
    }

    public int getQuestioner() {
        return questioner;
    }

    public Role getRole(int monitorIndex) {
        return roles[monitorIndex];
    }

    public boolean changeQuestioner() {
        if(numberOfAction >= totalActionPerRound) {
            numberOfAction = 0;
            numberOfRounds++;

            if(numberOfRounds >= totalRoundsNumber) {
                return true;
            }

            questioner = questioner + 1;
            roles[questioner] = Role.Questioner;
            suggester = 0;
            roles[suggester] = Role.Suggester;
            for(int i = 0; i < numberOfActors; i++) {
                if(i != questioner && i != suggester) {
                    roles[i] = Role.Answerer;
                }
            }
        }
        return false;
    }

    public void changeSuggester() {
        int tempSuggest = (suggester + 1) % numberOfActors;
        if(tempSuggest == questioner) {
            tempSuggest = (tempSuggest + 1) % numberOfActors;
        }

        roles[suggester] = Role.Answerer;
        roles[tempSuggest] = Role.Suggester;
        suggester = tempSuggest;

        numberOfAction++;
    }
}
