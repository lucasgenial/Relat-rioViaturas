package com.cicom.relatorioefetivos.controllers.adm;

import com.cicom.relatorioefetivos.DAO.CaracteristicaDAO;
import com.cicom.relatorioefetivos.DAO.FuncionalidadeDAO;
import com.cicom.relatorioefetivos.DAO.PoDAO;
import com.cicom.relatorioefetivos.model.Caracteristica;
import com.cicom.relatorioefetivos.model.Funcionalidade;
import com.cicom.relatorioefetivos.model.PO;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Lucas Matos
 */
public class TelaAdmCadastroTipoPOController implements Initializable {

    @FXML
    private TabPane root;

    @FXML
    private Tab tabCadastro;

    @FXML
    private TextField txtNomePO;

    @FXML
    private Button btnVoltar;

    @FXML
    private ComboBox<String> cbSituacaoPO;

    @FXML
    private Tab tabListagem;

    @FXML
    private ComboBox<Caracteristica> cbCaracteristica;

    @FXML
    private ComboBox<Funcionalidade> cbFuncionalidadesPO;

    @FXML
    private Button btnInserir;

    @FXML
    private TableView<Funcionalidade> tableFuncionalidadesPO;

    @FXML
    private TableColumn<Funcionalidade, Integer> columnIDFuncionalidades;

    @FXML
    private TableColumn<Funcionalidade, String> columnNomeFuncionalidades;

    @FXML
    private TableColumn<Funcionalidade, Boolean> columnAcaoFuncionalidade;

    @FXML
    private ComboBox<String> cbSituacaoBuscaPO;

    @FXML
    private TextField txtNomeBuscaPo;

    @FXML
    private TableView<PO> tablePO;

    @FXML
    private TableColumn<PO, Integer> columnIdPO;

    @FXML
    private TableColumn<PO, String> columnNomePO;

    @FXML
    private TableColumn<PO, String> columnSituacaoPO;

    @FXML
    private TableColumn<PO, Boolean> columnAcaoPO;

    @FXML
    private Button btnCadastrarPo;
    private Stage dialogStage;

    private PoDAO daoPO = new PoDAO();
    private CaracteristicaDAO daoCaracateristica = new CaracteristicaDAO();
    private FuncionalidadeDAO daoFuncionalidade = new FuncionalidadeDAO();

    private Set<PO> listaPOS = new HashSet<>();
    private Set<Caracteristica> listaCaracteristica = new HashSet<>();
    private Set<Funcionalidade> funcionalidadesPO, listaFuncionalidades = new HashSet<>();

    private PO novoPO, poEditar;
    private String tituloMensagem = "";
    private String corpoMensagem = "";
    private Caracteristica caracteristica;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregaDados();
    }

    private void carregaDados() {

        //Carrega o ComboBox Situação Cadastro
        cbSituacaoPO.getItems().clear();
        cbSituacaoPO.getItems().addAll("ATIVADO", "DESATIVADO");
        //Carrega o ComboBox Situação Busca
        cbSituacaoBuscaPO.getItems().clear();
        cbSituacaoBuscaPO.getItems().addAll("ATIVADO", "DESATIVADO");

        //Carrega a Lista de POS
        listaPOS.addAll(daoPO.getList("From PO"));

        System.out.println("LISTA PO: \n" + listaPOS);

        if (!listaPOS.isEmpty()) {
            atualizarTabelaPO(listaPOS);
        }

        //Carrega as Caracteristicas
        listaCaracteristica.addAll(daoCaracateristica.getList("From Caracteristica"));

        if (!listaCaracteristica.isEmpty()) {
            cbCaracteristica.getItems().clear();
            cbCaracteristica.getItems().addAll(listaCaracteristica);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Caracteristica");
            alert.setHeaderText("Ocorreu uma inconsistência no Banco de Dados\n"
                    + "Possivelmente não há Caracteristicas cadastradas/diponiveis no Banco de Dados!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }

        //Carrega as Funcionalidades
        listaFuncionalidades.addAll(daoFuncionalidade.getList("From Funcionalidade"));

        if (!listaFuncionalidades.isEmpty()) {
            cbFuncionalidadesPO.getItems().clear();
            cbFuncionalidadesPO.getItems().addAll(listaFuncionalidades);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Funcionalidade");
            alert.setHeaderText("Ocorreu uma inconsistência no Banco de Dados\n"
                    + "Possivelmente não há Funcionalidades cadastradas/diponiveis no Banco de Dados!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }
    }

    private void atualizarTabelaPO(Set<PO> dados) {

        columnNomePO.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PO, String> data) {
                return new SimpleStringProperty(data.getValue().getNome().toUpperCase());
            }
        });

        columnSituacaoPO.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PO, String> data) {
                if (data.getValue().getStatus()) {
                    return new SimpleStringProperty("ATIVADO");
                } else {
                    return new SimpleStringProperty("DESATIVADO");
                }
            }
        });

        columnAcaoPO.setSortable(false);

        columnAcaoPO.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PO, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<PO, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        columnAcaoPO.setCellFactory(new Callback<TableColumn<PO, Boolean>, TableCell<PO, Boolean>>() {
            @Override
            public TableCell<PO, Boolean> call(TableColumn<PO, Boolean> data) {
                return new ButtonCellPO(tablePO);
            }
        });

        tablePO.getItems().setAll(FXCollections.observableSet(dados));
    }

    private void clickedBtnCadastrarPO(MouseEvent event) {
        if (btnCadastrarPo.getText().equalsIgnoreCase("NOVO")) {
            limparDados();
            carregaDados();
        } else if (btnCadastrarPo.getText().equalsIgnoreCase("SALVAR")) {
            if (verificaDados()) {

                poEditar.setStatus(cbSituacaoPO.getValue().equalsIgnoreCase("ATIVADO"));
                poEditar.setCaracteristica(caracteristica);
                poEditar.setFuncionalidades(funcionalidadesPO);
                poEditar.setNome(txtNomePO.getText().toUpperCase());

                daoPO.alterar(poEditar);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("PO alterado com sucesso!");
                alert.showAndWait();
                limparDados();
                carregaDados();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }
        } else {
            if (verificaDados()) {
                PO novoPO = new PO();
                novoPO.setStatus(cbSituacaoPO.getValue().equalsIgnoreCase("ATIVADO"));
                novoPO.setCaracteristica(caracteristica);
                novoPO.setFuncionalidades(funcionalidadesPO);
                novoPO.setNome(txtNomePO.getText().toUpperCase());

                daoPO.salvar(novoPO);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("PO cadastrado com sucesso!");
                alert.showAndWait();
                limparDados();
                carregaDados();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }
        }
    }

    private void limparDados() {
        listaPOS.clear();
        funcionalidadesPO.clear();

        poEditar = null;
        txtNomePO.setDisable(true);
        cbSituacaoPO.setDisable(true);

        txtNomePO.clear();
        cbSituacaoPO.setValue("");
        btnCadastrarPo.setText("CADASTRAR");
    }

    private boolean verificaDados() {
        if (txtNomePO.getText().isEmpty()) {
            tituloMensagem = "Erro PO";
            corpoMensagem = "O nome do PO é necessário!";
            return false;
        }

        if (cbSituacaoPO.getSelectionModel().isEmpty()) {
            tituloMensagem = "Erro Situação PO";
            corpoMensagem = "A situação do PO é necessário!";
            return false;
        }

        if (caracteristica == null) {
            tituloMensagem = "Erro Caracteristica P.O.";
            corpoMensagem = "O PO possui uma caracteristica!";
            return false;
        }

        if (btnCadastrarPo.getText().equalsIgnoreCase("SALVAR")) {
            if (!(txtNomePO.getText().equals(poEditar.getNome())) && (daoPO.buscaPorNome(txtNomePO.getText()) != null)) {
                tituloMensagem = "Erro Salvar PO";
                corpoMensagem = "Este PO já está cadastrada no banco!";
                return false;
            }
        }

        if (btnCadastrarPo.getText().equalsIgnoreCase("CADASTRAR")) {
            if ((daoPO.buscaPorNome(txtNomePO.getText()) != null)) {
                tituloMensagem = "Erro Salvar Unidade";
                corpoMensagem = "Esta Unidade já está cadastrada no banco!";
                return false;
            }
        }

        return true;
    }

    private void cadastraFuncionalidades() {
        if (ccGPS.isSelected()) {
            funcionalidadesPO.add(Funcionalidade.GPS);
        }

        if (ccAudio.isSelected()) {
            funcionalidadesPO.add(Funcionalidade.Audio);
        }

        if (ccCamera.isSelected()) {
            funcionalidadesPO.add(Funcionalidade.Camera);
        }
    }

    private boolean verificaCaracteristica() {
        boolean select = false;

        if (ccPostoFixo.isSelected()) {
            caracteristica = Caracteristica.PostoFixo;
        } else if (ccAereo.isSelected()) {
            caracteristica = Caracteristica.Aereo;
        } else if (ccMotorizado.isSelected()) {
            caracteristica = Caracteristica.Motorizado;
        } else if (ccMontado.isSelected()) {
            caracteristica = Caracteristica.Montado;
        } else if (ccApe.isSelected()) {
            caracteristica = Caracteristica.Ape;
        }

        switch (caracteristica.name()) {
            case "PostoFixo":
                ccPostoFixo.setSelected(true);
                ccAereo.setSelected(false);
                ccMotorizado.setSelected(false);
                ccMontado.setSelected(false);
                ccApe.setSelected(false);

                ccGPS.setDisable(false);
                ccAudio.setDisable(false);
                ccCamera.setDisable(false);
                select = true;
                break;

            case "Aereo":
                ccPostoFixo.setSelected(false);
                ccAereo.setSelected(true);
                ccMotorizado.setSelected(false);
                ccMontado.setSelected(false);
                ccApe.setSelected(false);

                ccGPS.setDisable(false);
                ccAudio.setDisable(false);
                ccCamera.setDisable(false);
                select = true;
                break;

            case "Motorizado":
                ccPostoFixo.setSelected(false);
                ccAereo.setSelected(false);
                ccMotorizado.setSelected(true);
                ccMontado.setSelected(false);
                ccApe.setSelected(false);

                ccGPS.setDisable(false);
                ccAudio.setDisable(false);
                ccCamera.setDisable(false);
                select = true;
                break;

            case "Montado":
                ccPostoFixo.setSelected(false);
                ccAereo.setSelected(false);
                ccMotorizado.setSelected(false);
                ccMontado.setSelected(true);
                ccApe.setSelected(false);

                ccGPS.setDisable(false);
                ccAudio.setDisable(false);
                ccCamera.setDisable(false);
                select = true;
                break;

            case "Ape":
                ccPostoFixo.setSelected(false);
                ccAereo.setSelected(false);
                ccMotorizado.setSelected(false);
                ccMontado.setSelected(false);
                ccApe.setSelected(true);

                if (btnCadastrarPo.getText().equalsIgnoreCase("NOVO")) {
                    ccGPS.setDisable(true);
                    ccAudio.setDisable(true);
                    ccCamera.setDisable(true);
                }
                select = true;
                break;

            default:
                ccPostoFixo.setSelected(false);
                ccAereo.setSelected(false);
                ccMotorizado.setSelected(false);
                ccMontado.setSelected(false);
                ccApe.setSelected(false);

                ccGPS.setDisable(false);
                ccAudio.setDisable(false);
                ccCamera.setDisable(false);
                select = false;
                break;
        }

        return select;
    }

    private void clickedBtnVoltar(MouseEvent event) {
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
    private void clickedCbSituacaoBuscaPO(ActionEvent event) {
    }

    @FXML
    private void clickedBtnCadastrarPo(MouseEvent event) {
    }

    private class ButtonCellPO extends TableCell<PO, Boolean> {

        //BOTAO EDITAR
        private Button botaoEditar = new Button();
        private final ImageView imagemEditar = new ImageView(new Image(getClass().getResourceAsStream("/icons/editar.png")));

        //BOTAO EDITAR
        private Button botaoVisualizar = new Button();
        private final ImageView imagemVisualizar = new ImageView(new Image(getClass().getResourceAsStream("/icons/visualizar.png")));

        private ButtonCellPO(TableView<PO> tblView) {
            //BOTAO VISUALIZAR
            imagemVisualizar.fitHeightProperty().set(16);
            imagemVisualizar.fitWidthProperty().set(16);
            botaoVisualizar.setGraphic(imagemVisualizar);
            botaoVisualizar.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    PO poVisualizar = (PO) tblView.getItems().get(getIndex());
                    if (poVisualizar != null) {
                        txtNomePO.setText(poVisualizar.getNome().toUpperCase());
                        if (poVisualizar.getStatus()) {
                            //Ativado
                            cbSituacaoPO.getSelectionModel().select(0);
                        } else {
                            //Desativado
                            cbSituacaoPO.getSelectionModel().select(1);
                        }

                        txtNomePO.setDisable(true);
                        cbSituacaoPO.setDisable(true);
                        ccPostoFixo.setDisable(true);
                        ccAereo.setDisable(true);
                        ccMotorizado.setDisable(true);
                        ccMontado.setDisable(true);
                        ccApe.setDisable(true);

                        ccGPS.setDisable(true);
                        ccAudio.setDisable(true);
                        ccCamera.setDisable(true);

                        //Define a caracteristica
                        caracteristica = poVisualizar.getCaracteristica();
                        System.out.println("CARACTERISTICA : " + caracteristica.name());
                        verificaCaracteristica();

                        //define as funcionalidades
                        ccGPS.setSelected(false);
                        ccAudio.setSelected(false);
                        ccCamera.setSelected(false);

                        for (Funcionalidade funcionalidade : poVisualizar.getFuncionalidades()) {

                            System.out.println("FUNCIONALIDADE : " + funcionalidade.name());
                            if (funcionalidade.name().equalsIgnoreCase(Funcionalidade.GPS.name())) {
                                ccGPS.setSelected(true);
                            }

                            if (funcionalidade.name().equalsIgnoreCase(Funcionalidade.Audio.name())) {
                                ccAudio.setSelected(true);
                            }

                            if (funcionalidade.name().equalsIgnoreCase(Funcionalidade.Camera.name())) {
                                ccCamera.setSelected(true);
                            }
                        }

                        btnCadastrarPo.setText("NOVO");

                        root.getSelectionModel().select(tabCadastro);
                    }
                }
            });

            //BOTAO EDITAR
            imagemEditar.fitHeightProperty().set(16);
            imagemEditar.fitWidthProperty().set(16);

            botaoEditar.setGraphic(imagemEditar);

            botaoEditar.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    poEditar = (PO) tblView.getItems().get(getIndex());
                    if (poEditar != null) {
                        txtNomePO.setText(poEditar.getNome().toUpperCase());
                        if (poEditar.getStatus()) {
                            //Ativado
                            cbSituacaoPO.getSelectionModel().select(0);
                        } else {
                            //Desativado
                            cbSituacaoPO.getSelectionModel().select(1);
                        }

                        txtNomePO.setDisable(false);
                        cbSituacaoPO.setDisable(false);
                        ccPostoFixo.setDisable(false);
                        ccAereo.setDisable(false);
                        ccMotorizado.setDisable(false);
                        ccMontado.setDisable(false);
                        ccApe.setDisable(false);

                        ccGPS.setDisable(false);
                        ccAudio.setDisable(false);
                        ccCamera.setDisable(false);

                        //Define a Caracteristica
                        caracteristica = poEditar.getCaracteristica();
                        verificaCaracteristica();

                        //Define as funcionalidades
                        ccGPS.setSelected(false);
                        ccAudio.setSelected(false);
                        ccCamera.setSelected(false);

                        for (Funcionalidade funcionalidade : poEditar.getFuncionalidades()) {
                            if (funcionalidade.name().equalsIgnoreCase(Funcionalidade.GPS.name())) {
                                ccGPS.setSelected(true);
                            }

                            if (funcionalidade.name().equalsIgnoreCase(Funcionalidade.Audio.name())) {
                                ccAudio.setSelected(true);
                            }

                            if (funcionalidade.name().equalsIgnoreCase(Funcionalidade.Camera.name())) {
                                ccCamera.setSelected(true);
                            }
                        }

                        btnCadastrarPo.setText("SALVAR");

                        root.getSelectionModel().select(tabCadastro);
                    }
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                HBox hb = new HBox(3);
                hb.setAlignment(Pos.CENTER);
                hb.getChildren().add(botaoVisualizar);
                hb.getChildren().add(botaoEditar);
                setGraphic(hb);
            } else {
                setGraphic(null);
            }
        }
    }

}
