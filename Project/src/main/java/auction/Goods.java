package auction;

import java.util.ArrayList;

public class Goods {
    private static ArrayList <Good> Goods = new ArrayList();

    //Aggregation 
    public static void add_good(String Name){
        Goods.add(new Good(Name));
    }
}
