/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
package data;

import java.sql.Timestamp;

public class EKGDTO {
    private double ekg;
    private String cpr;
    private Timestamp timestamp;
    private int id;


    public double getEkg() {
        return ekg;
    }

    public void setEkg(double ekg) {
        this.ekg = ekg;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
