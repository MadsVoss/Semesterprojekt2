/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
import data.*;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import java.util.LinkedList;

public class EKGLoadController {
    public TextField cprField;
    public XYChart.Series<Double, Double> stringDoubleData = new XYChart.Series<>();
    public LineChart lineChart;

    //This is where we add the data to the lineChart when we retrieve it from the database. Again we remove the symbols to cause less cluttering.
    public void LoadData(ActionEvent actionEvent) {
        stringDoubleData.getData().clear();
        lineChart.getData().add(stringDoubleData);
        EKGDAO ekgDAO = new EKGDAOSQLImpl();
        LinkedList<EKGDTO> ekgData = ekgDAO.loadEKG(cprField.getText());
        for (int i = 0; i < ekgData.size(); i++) {
            stringDoubleData.getData().add(new XYChart.Data<Double, Double>((double) i, ekgData.get(i).getEkg()));
        }
        lineChart.setCreateSymbols(false);


    }

}