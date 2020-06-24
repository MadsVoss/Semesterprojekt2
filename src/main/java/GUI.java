/** @author {Mads Voss, Mikkel Bech, Dalia Pireh, Sali Azou, Beant Sandhu}*/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUI extends Application {
    public static Stage stage;

    @Override
    public void start(final Stage stage) throws Exception {
        GUI.stage = stage;
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Temperature.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EKGGUI.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }
}