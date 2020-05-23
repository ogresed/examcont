class ClickButtonAction {
    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @FunctionalInterface
    public interface Action {
        public void action(int x, int y);
    }
}