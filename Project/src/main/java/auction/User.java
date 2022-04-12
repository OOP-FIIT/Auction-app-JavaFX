package auction;
abstract public class User {
    private String login;
    private String password;
    private String email;
    private int balance;

    public void handle_log_out(){
        //close program

    }

    //Polymorphism
    public void handle_button(){
        //One button to rule them all
        System.out.println("Universal button");
    }

}
