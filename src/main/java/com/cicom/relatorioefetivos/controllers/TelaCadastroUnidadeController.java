package com.cicom.relatorioefetivos.controllers;

import com.cicom.relatorioefetivos.DAO.RelatorioDiarioMesasDAO;
import com.cicom.relatorioefetivos.model.RelatorioDiarioMesas;
import com.cicom.relatorioefetivos.model.RelatorioDiarioEfetivo;
import com.cicom.relatorioefetivos.model.Unidade;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class TelaCadastroUnidadeController implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private TableView<Unidade> tableUnidadesDisponiveis;
    @FXML
    private TableColumn<Unidade, Integer> colIdUnidade;
    @FXML
    private TableColumn<Unidade, String> colNomeUnidade;
    @FXML
    private TableColumn<Unidade, String> colNomeCmdUnidade;

    @FXML
    private Button btAdicionarUnidade;
    @FXML
    private Button btCancelarAdicao;

    private Stage dialogStage;

    //DAO
    private RelatorioDiarioMesas relatorioMesasSelecionado;
    private final RelatorioDiarioMesasDAO daoRelatorioDiarioMesas = new RelatorioDiarioMesasDAO();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new CarregaDados().start();
    }

    class CarregaDados extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!relatorioMesasSelecionado.getMesa().getUnidades().isEmpty()) {
                            tableUnidadesDisponiveis.getItems().setAll(FXCollections.observableSet(relatorioMesasSelecionado.getMesa().getUnidades()));
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erro Unidade");
                            alert.setHeaderText("Possivelmente não há UNIDADES cadastradas/diponiveis no sitema!");
                            alert.showAndWait();
                            root.getScene().getWindow().hide();
                        }
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(TelaCadastroUnidadeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void clickedAdicionar() {
        Unidade unidadeSelecionada = tableUnidadesDisponiveis.getSelectionModel().getSelectedItem();

        if (unidadeSelecionada != null) {
            RelatorioDiarioEfetivo relatorioEfetivo = new RelatorioDiarioEfetivo();
            relatorioEfetivo.setUnidade(unidadeSelecionada);

            relatorioMesasSelecionado.getListaRelatorioDiarioEfetivo().add(relatorioEfetivo);

            daoRelatorioDiarioMesas.alterar(relatorioMesasSelecionado);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sucesso!");
            alert.setHeaderText("Unidade cadastrada com Sucesso");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao Cadastrar");
            alert.setHeaderText("É necessário a seleção de uma UNIDADES cadastradas/diponiveis na tabela!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedCancelar() {
        root.getScene().getWindow().hide();
    }

    public RelatorioDiarioMesas getRelatorioMesasSelecionado() {
        return relatorioMesasSelecionado;
    }

    public void setRelatorioMesasSelecionado(RelatorioDiarioMesas relatorioMesasSelecionado) {
        this.relatorioMesasSelecionado = relatorioMesasSelecionado;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
