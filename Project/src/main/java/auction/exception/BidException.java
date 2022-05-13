package auction.exception;

/**
 * Custom exception
 */
public class BidException extends Exception  
{  
    /**
     * Exception about bid input
     * @param str
     */
    public BidException (String str)  
    {  
        super(str);  
    }  
}  