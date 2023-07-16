package model;

import controller.Controller;

import java.util.regex.Matcher;

public class BidCommand extends Command{
    @Override
    protected String getPattern() {
        return "bid (?<bidderId>\\d+) (?<antiqueId>\\d+) (?<price>\\d+)";
    }

    @Override
    public String call(String command) {
        Matcher matcher = super.getMatcher(command);
        return Controller.getInstance().bid(matcher.group("bidderId"), matcher.group("antiqueId"), matcher.group("price"));
    }
}
