package model;

import controller.Controller;

import java.util.regex.Matcher;

public class AuctCommand extends Command{
    @Override
    protected String getPattern() {
        return "auct (?<id>\\d+)";
    }

    @Override
    public String call(String command) {
        Matcher matcher = super.getMatcher(command);
        return Controller.getInstance().auct(matcher.group("id"));
    }
}
