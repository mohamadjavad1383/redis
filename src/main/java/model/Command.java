package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
    protected abstract String getPattern();

    public Matcher getMatcher(String command) {
        Matcher matcher = Pattern.compile(this.getPattern()).matcher(command);
        if (matcher.matches())
            return matcher;
        return null;
    }

    public abstract String call(String command);
}
