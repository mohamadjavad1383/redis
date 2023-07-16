package model;

import controller.Controller;

import java.util.regex.Matcher;

public class RegisterBidderCommand extends Command{
    @Override
    protected String getPattern() {
        return "register bidder (?<id>\\d+) (?<name>\\S+) (?<budget>\\d+)";
    }

    @Override
    public String call(String command) {
        Matcher matcher = super.getMatcher(command);
        return Controller.getInstance().registerBidder(matcher.group("id"), matcher.group("name"), matcher.group("budget"));
    }
}
