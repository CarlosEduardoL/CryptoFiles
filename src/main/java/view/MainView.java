package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            primaryStage.setTitle("CryptoFiles");
            var loader = new FXMLLoader();
            loader.setLocation(MainView.class.getResource("/view/RootLayout.fxml"));
            var rootLayout = (Parent) loader.load();
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
