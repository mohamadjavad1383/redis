package model;

import controller.Controller;

import java.util.regex.Matcher;

public class AddAntiqueCommand extends Command{
    @Override
    protected String getPattern() {
        return "add antique (?<id>\\d+) (?<name>\\S+) (?<price>\\d+)";
    }

    @Override
    public String call(String command) {
        Matcher matcher = super.getMatcher(command);
        return Controller.getInstance().addAntique(matcher.group("id"), matcher.group("name"), matcher.group("price"));
    }
}
