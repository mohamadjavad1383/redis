package model;

public class ExitCommand extends Command{
    @Override
    protected String getPattern() {
        return "exit";
    }

    @Override
    public String call(String command) {
        System.exit(0);
        return "exited";
    }
}
