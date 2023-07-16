import model.*;
import redis.clients.jedis.Jedis;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Config config = new Config("localhost", 6379);
        connect(config);
    }

    private static void connect(Config config) {
        Jedis jedis = new Jedis(config.getUrl(), config.getPort());
        try {
            jedis.ping();
            System.out.println("connected");
            setCommands();
            View.getInstance().setJedis(jedis);
            View.getInstance().run();
        } catch (Exception e) {
            System.out.println("could not connect");
        }
    }

    private static void setCommands() {
        List<Command> commands = new ArrayList<>();
        commands.add(new AddAntiqueCommand());
        commands.add(new BidCommand());
        commands.add(new AuctCommand());
        commands.add(new RegisterBidderCommand());
        commands.add(new ExitCommand());
        View.getInstance().setCommands(commands);
    }
}
