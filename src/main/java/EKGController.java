/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
import data.EKGDAO;
import data.EKGDAOSQLImpl;
import data.EKGDTO;
import data.EKGListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.LinkedList;

public class EKGController implements EKGListener {

    public LineChart<Double, Double> lineChart;
    public XYChart.Series<Double, Double> stringDoubleData = new XYChart.Series<>();
    public TextField idField;
    private boolean record;
    private final EKGDAO ekgDAO = new EKGDAOSQLImpl();

    //This is where the magic happens. Here we draw our graph in the GUI. It adds data to our XYChart which is how we show it in the LineChart. We also make sure to get the cpr if we press record.
    @Override
    public void notifyEKG(LinkedList<EKGDTO> ekgData) {
        if (this.record) {
            for (int i = 0; i < ekgData.size(); i++) {
                ekgData.get(i).setCpr(idField.getText());
            }
            ekgDAO.saveEkg(ekgData);

        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stringDoubleData.getData().clear();
                for (int i = 0; i < ekgData.size(); i++) {
                    stringDoubleData.getData().add(new XYChart.Data<Double, Double>((double) i, ekgData.get(i).getEkg()));
                }
            }

        });

    }
    //This is what happens when we press the start button. We add the data from above to the lineChart and run it in a new Thread to not cause problems. We also remove the symbols to make it less cluttered.
    public void startEKG(ActionEvent actionEvent) throws InterruptedException {
        lineChart.getData().add(stringDoubleData);
        ProducerConsumer pc = new ProducerConsumer();
        //pc.registerDB(this);
        pc.registerGUI(this);
        lineChart.setCreateSymbols(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pc.runThreads();
                lineChart.getData().add(stringDoubleData);

            }
        }).start();


    }
    //Start recording.
    public void startRecordingEKG(ActionEvent actionEvent) {
        this.record = !this.record;
    }

    //This is what happens if we press the "Load Data" button. We open a new window where you can type in a cpr and then retrieve data from the database.
//We decided to make it in another window so you could run both at the same time.
    public void loadEKGPage(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoadGUI.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            Stage loadStage = new Stage();
            loadStage.setScene(new Scene(anchorPane));
            loadStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
