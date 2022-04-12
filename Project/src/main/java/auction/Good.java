package auction;

import java.util.ArrayList;

public class Good {
    private String Name;
    private ArrayList <Bid> Bids = new ArrayList();

    public Good(String new_Name){
        Name = new_Name;
    }

    public void add_Bid(String login, int bid){
        Bids.add(new Bid(login, bid));
    }

    public void calculate_winner(){
        //select best bid
        //update sql
        //delete this good
    }
}
