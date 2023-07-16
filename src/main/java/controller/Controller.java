package controller;

import lombok.Setter;
import redis.clients.jedis.Jedis;

@Setter
public class Controller {
    private static Controller instance;
    private Jedis jedis;

    private Controller() {
    }

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public String addAntique(String id, String name, String price) {
        if (jedis.hget("antique_" + id, "name") != null)
            return "id already exist";
        jedis.hset("antique_" + id, "name", name);
        jedis.hset("antique_" + id, "price", price);
        jedis.hset("antique_" + id, "on_auct", "false");
        return "antique added";
    }

    public String registerBidder(String id, String name, String budget) {
        if (jedis.hget("bidder_" + id, "name") != null)
            return "id already exist";
        jedis.hset("bidder_" + id, "name", name);
        jedis.hset("bidder_" + id, "budget", budget);
        return "bidder registered";
    }

    public String auct(String id) {
        String onAuct = jedis.hget("antique_" + id, "on_auct");
        if (onAuct == null)
            return "id does not exist";
        if (onAuct.equals("true"))
            return "antique is already on auct";
        jedis.hset("antique_" + id, "on_auct", "true");
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        return "antique is now on auct";
    }

    public String bid(String bidderId, String antiqueId, String price) {
        int budget;
        int basePrice;

        try {
            budget = Integer.parseInt(jedis.hget("bidder_" + bidderId, "budget"));
        } catch (NumberFormatException e) {
            return "bidder id does not exist";
        }

        try {
            basePrice = Integer.parseInt(jedis.hget("antique_" + antiqueId, "price"));
        } catch (NumberFormatException e) {
            return "antique id does not exist";
        }

        String onAuct = jedis.hget("antique_" + antiqueId, "on_auct");
        if (onAuct.equals("false"))
            return "antique is not on auct";
        int intPrice = Integer.parseInt(price);
        if (intPrice > budget)
            return "inefficient money";
        if (intPrice <= basePrice)
            return "base price is higher";
        String buyerId = jedis.hget("antique_" + antiqueId, "buyer");
        if (buyerId != null) {
            String currentBudget = jedis.hget("bidder_" + buyerId, "budget");
            jedis.hset("bidder_" + buyerId, "budget", String.valueOf(basePrice + Integer.parseInt(currentBudget)));
        }
        jedis.hset("bidder_" + bidderId, "budget", String.valueOf(budget - intPrice));
        jedis.hset("antique_" + antiqueId, "price", price);
        jedis.hset("antique_" + antiqueId, "buyer", bidderId);
        jedis.expire("antique_" + antiqueId, 10);
        return "successful";
    }

}
