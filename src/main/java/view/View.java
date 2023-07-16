package view;

import controller.Controller;
import lombok.Setter;
import model.Command;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Setter
public class View {
    private static View instance;
    private Jedis jedis;
    private List<Command> commands;

    private View() {
    }

    public static View getInstance() {
        if (instance == null)
            instance = new View();
        return instance;
    }

    public void run() {
        Controller.getInstance().setJedis(this.jedis);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            Optional<Command> command = this.commands.stream().filter(c -> c.getMatcher(input) != null)
                    .findFirst();
            if (command.isEmpty())
                System.out.println("invalid command");
            else
                System.out.println(command.get().call(input));
        }
    }
}
