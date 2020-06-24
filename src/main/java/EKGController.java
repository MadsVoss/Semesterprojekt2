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

    final int WINDOW_SIZE = 149;


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

                if (stringDoubleData.getData().size() > WINDOW_SIZE)
                    stringDoubleData.getData().remove(0);
            }

        });

    }

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

    public void startRecordingEKG(ActionEvent actionEvent) {
        this.record = !this.record;
    }

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
