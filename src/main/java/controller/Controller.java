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

        int intPrice = Integer.parseInt(price);
        String reject = rejectBid(antiqueId, budget, basePrice, intPrice);
        if (reject != null) return reject;
        String buyerId = jedis.hget("antique_" + antiqueId, "buyer");

        payMoneyBack(basePrice, buyerId);
        acceptBid(bidderId, antiqueId, price, budget, intPrice);
        return "successful";
    }

    private void payMoneyBack(int basePrice, String buyerId) {
        if (buyerId != null) {
            String currentBudget = jedis.hget("bidder_" + buyerId, "budget");
            jedis.hset("bidder_" + buyerId, "budget", String.valueOf(basePrice + Integer.parseInt(currentBudget)));
        }
    }

    private String rejectBid(String antiqueId, int budget, int basePrice, int intPrice) {
        String onAuct = jedis.hget("antique_" + antiqueId, "on_auct");
        if (onAuct.equals("false"))
            return "antique is not on auct";
        if (intPrice > budget)
            return "inefficient money";
        if (intPrice <= basePrice)
            return "base price is higher";
        return null;
    }

    private void acceptBid(String bidderId, String antiqueId, String price, int budget, int intPrice) {
        jedis.hset("bidder_" + bidderId, "budget", String.valueOf(budget - intPrice));
        jedis.hset("antique_" + antiqueId, "price", price);
        jedis.hset("antique_" + antiqueId, "buyer", bidderId);
        jedis.expire("antique_" + antiqueId, 10);
    }


}
