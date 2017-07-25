package com.cicom.relatorioviaturas.controllers;

import com.cicom.relatorioviaturas.DAO.ServidorDAO;
import com.cicom.relatorioviaturas.model.Servidor;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class TelaListarServidoresController implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private TableView<Servidor> tbServidores;
    @FXML
    private TableColumn<Servidor, Integer> tbColumnMatriculaServidor = new TableColumn<>("matricula");
    @FXML
    private TableColumn<Servidor, String> tbColumnHieraquiaServidor = new TableColumn<>("hieraquia");
    @FXML
    private TableColumn<Servidor, String> tbColumnNomeServidor = new TableColumn<>("nome");
    @FXML
    private Button btnSelecionar;
    @FXML
    private TextField txtCampoBusca;

    /*
    CRIA OS DAOS
     */
    private Servidor servidor;
    private ServidorDAO daoServidor = new ServidorDAO();
    private Stage dialogStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregaDadosTabela();
    }

    private void carregaDadosTabela() {
        List<Servidor> listaServidor = daoServidor.getListAtivos();

        ObservableList<Servidor> dadosIniciais = FXCollections.observableList(listaServidor);

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Servidor> filtroDeDados = new FilteredList<>(dadosIniciais, p -> true);

        txtCampoBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            filtroDeDados.setPredicate(dado -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String valorDigitado = newValue.toLowerCase();

                if (dado.getNome().toLowerCase().contains(valorDigitado)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Servidor> sortedData = new SortedList<>(filtroDeDados);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tbServidores.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tbServidores.setItems(sortedData);
    }

    @FXML

    private void clickedBtnSelecionar(MouseEvent event) {        
        servidor = tbServidores.getSelectionModel().getSelectedItem();
        
        if (servidor != null) {
            root.getScene().getWindow().hide();
        }
    }

    @FXML
    private void keyPressedTxtBusca(KeyEvent event) {
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Servidor getServidor() {
        return this.servidor;
    }

    public void setServidor(Servidor value) {
        this.servidor = value;
    }

}
