package auction.threads;

import java.sql.SQLException;

import auction.sql.SQL;

public class UpdateLicense extends Thread{
    private String licenseKey;
    private int id;

    public UpdateLicense(String licenseKey, int id){
        this.licenseKey = licenseKey;
        this.id = id;
    }

    @Override
    public void run(){
        try {
            SQL.UPDATE_UserLicense(licenseKey, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
