/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
package data;

import java.util.LinkedList;

public interface EKGListener {
    void notifyEKG(LinkedList<EKGDTO>ekgData);
}
