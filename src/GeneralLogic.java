public class GeneralLogic {
    int numberOfMice;
    ReadyChecker readyChecker;

    GeneralLogic(int numberOfMice) {
        this.numberOfMice = numberOfMice;
        readyChecker = new ReadyChecker(numberOfMice);
    }



    public ReadyChecker getReadyChecker() {
        return readyChecker;
    }
}
