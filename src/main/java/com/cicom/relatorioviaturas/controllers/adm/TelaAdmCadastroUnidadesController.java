package com.cicom.relatorioviaturas.controllers.adm;

import com.cicom.relatorioviaturas.DAO.PoDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.model.PO;
import com.cicom.relatorioviaturas.model.Unidade;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 *
 * @author Lucas Matos
 */
public class TelaAdmCadastroUnidadesController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TableView<Unidade> tableUnidades;
    @FXML
    private TableColumn<Unidade, Integer> tbColumnId;
    @FXML
    private TableColumn<Unidade, String> tbColumnUnidade;
    @FXML
    private TableColumn<Unidade, String> tbColumnComandoArea;
    @FXML
    private TextField txtNomeUnidade;
    @FXML
    private TextField txtComandoArea;
    @FXML
    private ComboBox<PO> cbTipoPO;
    @FXML
    private TableView<PO> tableTipoPO;
    @FXML
    private TableColumn<PO, Integer> tbColumnIdPO;
    @FXML
    private TableColumn<PO, String> tbColumnTipoPO;
    @FXML
    private Button btnAdicionaTipoPO;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnRemoveTipoPO;
    @FXML
    private Label labelNumeroTipoPO;

    /*
    Extras
     */
    private PoDAO daoPO = new PoDAO();
    private UnidadeDAO daoUnidade = new UnidadeDAO();
    private Unidade novaUnidade;
    private Set<PO> listaPOs;
    private List<PO> listaPOsCombo;
    private List<Unidade> listaUnidade;
    private boolean editar = false;
    private String tituloMensagem;
    private String corpoMensagem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregaDados();
    }

    @FXML
    private void clickedTableUnidades() {
        novaUnidade = tableUnidades.getSelectionModel().getSelectedItem();
        txtNomeUnidade.setText(novaUnidade.getNome());
        txtComandoArea.setText(novaUnidade.getComandoDeArea());
        labelNumeroTipoPO.setText((novaUnidade.getPos() != null) ? (novaUnidade.getPos().size() + " Tipo P.O.") : ("0 Tipo P.O."));
        listaPOs = (novaUnidade.getPos() != null) ? novaUnidade.getPos() : null;
        tableTipoPO.getItems().setAll(FXCollections.observableSet(listaPOs));

        btnSalvar.setText("Editar");
        btnSalvar.setDisable(false);

        txtNomeUnidade.setDisable(true);
        txtComandoArea.setDisable(true);
        cbTipoPO.setDisable(true);
        btnAdicionaTipoPO.setDisable(true);
        editar = false;
    }

    @FXML
    private void clickedBtnSalvar() {
        //Ver a ação no botão editar
        if (btnSalvar.getText().equals("Editar")) {
            txtNomeUnidade.setDisable(false);
            txtComandoArea.setDisable(false);
            cbTipoPO.setDisable(false);
            btnSalvar.setDisable(false);
            btnAdicionaTipoPO.setDisable(false);
            btnSalvar.setText("Salvar");
            editar = true;

            //Se o botão salvar for ativado e o editar for verdadeiro
            //Estamos modificando um Tipo P.O. Já existente
        } else if (btnSalvar.getText().equals("Salvar") && editar) {
            if (verificaDados()) {
                //Altera o nome do PO
                novaUnidade.setNome(txtNomeUnidade.getText());
                novaUnidade.setComandoDeArea(txtComandoArea.getText());
                novaUnidade.setPos(listaPOs);

                daoUnidade.alterar(novaUnidade);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Unidade alterado com sucesso!");
                alert.showAndWait();
                carregaDados();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }
            //Adicionando novo tipo P.O.
        } else if (btnSalvar.getText().equals("Salvar") && !editar) {
            if (verificaDados()) {
                //Altera o nome do PO
                novaUnidade = new Unidade();
                novaUnidade.setNome(txtNomeUnidade.getText());
                novaUnidade.setComandoDeArea(txtComandoArea.getText());
                novaUnidade.setPos(listaPOs);

                daoUnidade.salvar(novaUnidade);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Unidade alterado com sucesso!");
                alert.showAndWait();
                carregaDados();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void clickedBtnNovo() {
        btnSalvar.setText("Salvar");
        txtComandoArea.setText("");
        txtNomeUnidade.setText("");
        txtComandoArea.setDisable(false);
        txtNomeUnidade.setDisable(false);
        cbTipoPO.setDisable(false);
        btnAdicionaTipoPO.setDisable(false);
        btnSalvar.setDisable(false);
        editar = false;
        novaUnidade = null;
    }

    @FXML
    private void clickedBtnVoltar() {
        Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
        alertVoltar.setTitle("Atenção!");
        alertVoltar.setHeaderText("Os dados informados serão perdidos, deseja continuar?");
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
            root.getScene().getWindow().hide();
        }
    }

    @FXML
    private void clickedCbTipoPO() {
    }

    @FXML
    private void clickedTableTipoPO() {
        btnRemoveTipoPO.setDisable(false);
    }

    private void carregaDados() {
        txtComandoArea.setDisable(true);
        txtNomeUnidade.setDisable(true);
        cbTipoPO.setDisable(true);
        btnAdicionaTipoPO.setDisable(true);
        btnSalvar.setDisable(true);
        btnSalvar.setText("Editar");

        //Carrega a lista de Funções
        listaPOsCombo = daoPO.getList("FROM PO");
        listaUnidade = daoUnidade.getList("FROM Unidade");

        if (!listaPOsCombo.isEmpty()) {
            //Carrega os dados dos POs no ComboBox
            cbTipoPO.setConverter(new StringConverter<PO>() {
                @Override
                public String toString(PO item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public PO fromString(String string) {
                    return daoPO.buscaPorNome(string);
                }

            });
            cbTipoPO.getItems().setAll(FXCollections.observableList(listaPOsCombo));
        }

        //Carrega as Unidades cadastradas no banco de dados
        if (!listaUnidade.isEmpty()) {
            tableUnidades.getItems().setAll(FXCollections.observableList(listaUnidade));
        }
    }

    private boolean verificaDados() {
        if (txtNomeUnidade.getText().isEmpty()) {
            tituloMensagem = "Erro Nome Unidade";
            corpoMensagem = "O nome da Unidade é necessário!";
            txtNomeUnidade.setFocusTraversable(true);
            return false;
        }

        if (txtComandoArea.getText().isEmpty()) {
            tituloMensagem = "Erro Comando de Area Unidade";
            corpoMensagem = "O Comando de Área é necessário!";
            txtComandoArea.setFocusTraversable(true);
            return false;
        }

        if (listaPOs.isEmpty()) {
            tituloMensagem = "Erro Tipo P.O. Unidade";
            corpoMensagem = "A unidade precisará gerenciar algum tipo de P.O.!";
            cbTipoPO.setFocusTraversable(true);
            return false;
        }

        if (daoUnidade.buscaPorObjeto(novaUnidade) != null) {
            tituloMensagem = "Erro Salvar Unidade";
            corpoMensagem = "Esta Unidade já está cadastrada no banco!";
            return false;
        }

        return true;
    }

    @FXML
    private void clickedBtnAdicionaTipoPO() {
        PO poSelecionado = cbTipoPO.getSelectionModel().getSelectedItem();

        if (poSelecionado != null) {
            //Carrega o item na lista de POS que comporá a tabela/Unidade
            listaPOs.add(poSelecionado);

            //Adiciono o item na Tabela
            tableTipoPO.getItems().setAll(FXCollections.observableSet(listaPOs));

            labelNumeroTipoPO.setText(tableTipoPO.getItems().size() + " Tipo P.O.");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário selecionar um P.O.!");
            alert.showAndWait();
        }
        tableTipoPO.setFocusTraversable(true);
    }

    @FXML
    private void clickedBtnRemoveTipoPO(MouseEvent event) {
        PO poSelecionado = tableTipoPO.getSelectionModel().getSelectedItem();

        if (poSelecionado != null) {
            //Carrega o item na lista de POS que comporá a tabela/Unidade
            listaPOs.remove(poSelecionado);

            //Adiciono o item na Tabela
            tableTipoPO.getItems().setAll(FXCollections.observableSet(listaPOs));

            labelNumeroTipoPO.setText(tableTipoPO.getItems().size() + " Tipo P.O.");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário selecionar um P.O.!");
            alert.showAndWait();
        }
        tableTipoPO.setFocusTraversable(true);
    }

}
