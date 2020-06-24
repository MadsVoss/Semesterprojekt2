/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
package data;

import java.util.LinkedList;
import java.util.List;

public interface EKGListener {
    void notifyEKG(LinkedList<EKGDTO>ekgData);
}
