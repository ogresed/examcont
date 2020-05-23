public class GeneralLogic {
    int numberOfMice;
    ReadyChecker readyChecker;

    public GeneralLogic(int numberOfMice) {
        this.numberOfMice = numberOfMice;
        readyChecker = new ReadyChecker(numberOfMice);
    }



    public ReadyChecker getReadyChecker() {
        return readyChecker;
    }
}
