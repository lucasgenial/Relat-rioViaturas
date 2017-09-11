package com.cicom.relatorioefetivos.controllers.adm;

import com.cicom.relatorioefetivos.DAO.PoDAO;
import com.cicom.relatorioefetivos.model.PO;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Lucas Matos
 */
public class TelaAdmCadastroTipoPOController1 implements Initializable {

    @FXML
    private TableView<PO> tableTipoPO;
    @FXML
    private TableColumn<PO, Integer> tbColumnId;
    @FXML
    private TableColumn<PO, String> tbColumnNome;
    @FXML
    private AnchorPane root;
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnExcluir;
    @FXML
    private TextField txtTipoPo;

    /*
    Extras
     */
    private Stage dialogStage;
    private PoDAO daoPO = new PoDAO();
    private PO novoPO;
    private boolean editar = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregaDados();
    }

    @FXML
    private void clickedTableTipoPO() {
        novoPO = tableTipoPO.getSelectionModel().getSelectedItem();
        txtTipoPo.setText(novoPO.getNome());
        btnSalvar.setText("Editar");
        btnSalvar.setDisable(false);
        txtTipoPo.setDisable(true);
        editar = false;
    }

    @FXML
    private void clickedBtnSalvar() {
        //Ver a ação no botão editar
        if (btnSalvar.getText().equals("Editar")) {
            txtTipoPo.setDisable(false);
            btnSalvar.setText("Salvar");
            editar = true;

            //Se o botão salvar for ativado e o editar for verdadeiro
            //Estamos modificando um Tipo P.O. Já existente
        } else if (btnSalvar.getText().equals("Salvar") && editar) {
            if (!txtTipoPo.getText().isEmpty()) {
                if (daoPO.buscaPorNome(txtTipoPo.getText()) == null) {
                    //Altera o nome do PO
                    novoPO.setNome(txtTipoPo.getText());
                    daoPO.alterar(novoPO);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Tipo P.O. alterado com sucesso!");
                    alert.showAndWait();
                    carregaDados();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro ao salvar!");
                    alert.setHeaderText("Tipo P.O. já existe!\nTente outro nome");
                    alert.showAndWait();
                    txtTipoPo.setFocusTraversable(true);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("campo Tipo P.O. deverá ser preenchido!");
                alert.showAndWait();
                txtTipoPo.setFocusTraversable(true);
            }
            //Adicionando novo tipo P.O.
        } else if (btnSalvar.getText().equals("Salvar") && !editar) {
            if (!txtTipoPo.getText().isEmpty()) {
                if (daoPO.buscaPorNome(txtTipoPo.getText()) == null) {

                    novoPO = new PO(txtTipoPo.getText());
                    daoPO.salvar(novoPO);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Tipo P.O. cadastrado com sucesso!");
                    alert.showAndWait();
                    carregaDados();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro ao salvar!");
                    alert.setHeaderText("Tipo P.O. já existe!");
                    alert.showAndWait();
                    txtTipoPo.setFocusTraversable(true);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("campo Tipo P.O. deverá ser preenchido!");
                alert.showAndWait();
                txtTipoPo.setFocusTraversable(true);
            }
        }
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void carregaDados() {
        txtTipoPo.setDisable(true);
        btnSalvar.setDisable(true);
        btnSalvar.setText("Editar");

        //Carrega a lista de Funções
        List<PO> listaPOS = daoPO.getListAtivos();

        if (!listaPOS.isEmpty()) {
            tableTipoPO.getItems().setAll(FXCollections.observableList(listaPOS));
        }
    }

    @FXML
    private void clickedBtnNovo(MouseEvent event) {
        txtTipoPo.setDisable(false);
        btnSalvar.setDisable(false);
        btnSalvar.setText("Salvar");
        txtTipoPo.setText("");
        editar = false;
        novoPO = null;
    }

    @FXML
    private void clickedBtnExcluir(MouseEvent event) {
        PO poSelecionado = tableTipoPO.getSelectionModel().getSelectedItem();

        if (poSelecionado != null) {
            Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
            alertVoltar.setTitle("Atenção!");
            alertVoltar.setHeaderText("Os dados referentes à este Tipo P.O. serão perdidos, deseja continuar?");
            alertVoltar.getButtonTypes().clear();
            alertVoltar.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

            //Deactivate Defaultbehavior for yes-Button:
            Button yesButton = (Button) alertVoltar.getDialogPane().lookupButton(ButtonType.YES);
            yesButton.setDefaultButton(false);

            //Activate Defaultbehavior for no-Button:
            Button noButton = (Button) alertVoltar.getDialogPane().lookupButton(ButtonType.NO);
            noButton.setDefaultButton(true);

            //Pega qual opção o usuario pressionou
            final Optional<ButtonType> resultado = alertVoltar.showAndWait();

            if (resultado.get() == ButtonType.YES) {
                daoPO.deletar(poSelecionado);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Tipo P.O. excluído com sucesso!");
                alert.showAndWait();
                carregaDados();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("É necessário selecionar, o tipo P.O.!");
            alert.showAndWait();
        }
    }
}
