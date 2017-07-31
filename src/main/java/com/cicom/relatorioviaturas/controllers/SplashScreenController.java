package com.cicom.relatorioviaturas.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class SplashScreenController implements Initializable {

    @FXML
    private StackPane painelPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(7000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/fxml/TelaPrincipal.fxml"));
                        } catch (IOException ex) {
                            Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
//                        stage.setResizable(false);
                        stage.setTitle("SisEF 1.0 - Sistema de Efetivo e Frota - STELECOM                    ||                    © Copyright@2017 ©  D.Madeira / I.Bastos / L.Matos");
                        stage.centerOnScreen();
                        stage.show();

                        painelPrincipal.getScene().getWindow().hide();
                    }

                });
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
