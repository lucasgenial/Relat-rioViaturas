package com.cicom.relatorioefetivos.controllers.adm;

import com.cicom.relatorioefetivos.DAO.CaracteristicaDAO;
import com.cicom.relatorioefetivos.DAO.FuncionalidadeDAO;
import com.cicom.relatorioefetivos.DAO.PoDAO;
import com.cicom.relatorioefetivos.model.Caracteristica;
import com.cicom.relatorioefetivos.model.Funcionalidade;
import com.cicom.relatorioefetivos.model.PO;
import com.cicom.relatorioefetivos.model.TipoMesa;
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
import javafx.util.Callback;
import javafx.util.StringConverter;

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
    private Button btnCadastrar;

    private final PoDAO daoPO = new PoDAO();
    private final CaracteristicaDAO daoCaracateristica = new CaracteristicaDAO();
    private final FuncionalidadeDAO daoFuncionalidade = new FuncionalidadeDAO();

    private Set<PO> listaPOS = new HashSet<>();
    private Set<Caracteristica> listaCaracteristica = new HashSet<>();
    private Set<Funcionalidade> funcionalidadesPO = new HashSet<>();
    private Set<Funcionalidade> listaFuncionalidades = new HashSet<>();

    private PO novoPO, poEditar;
    private String tituloMensagem = "";
    private String corpoMensagem = "";

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
            cbCaracteristica.setConverter(new StringConverter<Caracteristica>() {
                @Override
                public String toString(Caracteristica item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public Caracteristica fromString(String string) {
                    return daoCaracateristica.buscaPorNome(string);
                }
            });
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
            cbFuncionalidadesPO.setConverter(new StringConverter<Funcionalidade>() {
                @Override
                public String toString(Funcionalidade item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public Funcionalidade fromString(String string) {
                    return daoFuncionalidade.buscaPorNome(string);
                }
            });
        } else {
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

    private void atualizarTabelaFuncionalidadesPO(Set<Funcionalidade> dados) {

        columnNomeFuncionalidades.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionalidade, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Funcionalidade, String> data) {
                return new SimpleStringProperty(data.getValue().getNome().toUpperCase());
            }
        });

        columnAcaoFuncionalidade.setSortable(false);

        columnAcaoFuncionalidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Funcionalidade, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Funcionalidade, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        columnAcaoFuncionalidade.setCellFactory(new Callback<TableColumn<Funcionalidade, Boolean>, TableCell<Funcionalidade, Boolean>>() {
            @Override
            public TableCell<Funcionalidade, Boolean> call(TableColumn<Funcionalidade, Boolean> data) {
                return new ButtonCellFuncionalidade(tableFuncionalidadesPO);
            }
        });

        tableFuncionalidadesPO.getItems().setAll(FXCollections.observableSet(dados));
    }

    @FXML
    private void clickedBtnInserirFuncionalidade(MouseEvent event) {
        Funcionalidade funcionalidadeSelecionada = cbFuncionalidadesPO.getSelectionModel().getSelectedItem();

        if (funcionalidadeSelecionada != null) {
            //Carrega o item na lista de POS que comporá a tabela/Unidade
            funcionalidadesPO.add(funcionalidadeSelecionada);

            //Adiciono o item na Tabela
            atualizarTabelaFuncionalidadesPO(funcionalidadesPO);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário selecionar uma Funcionalidade!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedBtnCadastrar(MouseEvent event) {
        if (btnCadastrar.getText().equalsIgnoreCase("NOVO")) {
            limparDados();
            carregaDados();
        } else if (btnCadastrar.getText().equalsIgnoreCase("SALVAR")) {
            if (verificaDados()) {
                poEditar.setNome(txtNomePO.getText().toUpperCase());
                poEditar.setStatus(cbSituacaoPO.getValue().equalsIgnoreCase("ATIVADO"));
                poEditar.setCaracteristica(cbCaracteristica.getSelectionModel().getSelectedItem());
                poEditar.setFuncionalidades(funcionalidadesPO);

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
                novoPO.setNome(txtNomePO.getText().toUpperCase());
                novoPO.setStatus(cbSituacaoPO.getValue().equalsIgnoreCase("ATIVADO"));
                novoPO.setCaracteristica(cbCaracteristica.getSelectionModel().getSelectedItem());
                novoPO.setFuncionalidades(funcionalidadesPO);

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
        listaCaracteristica.clear();
        listaFuncionalidades.clear();
        funcionalidadesPO.clear();
        tableFuncionalidadesPO.getItems().clear();
        
        poEditar = null;
        txtNomePO.setDisable(false);
        cbSituacaoPO.setDisable(false);
        cbFuncionalidadesPO.setDisable(false);
        cbCaracteristica.setDisable(false);
        btnInserir.setDisable(false);

        txtNomePO.clear();
        cbSituacaoPO.getSelectionModel().select("");
        btnCadastrar.setText("CADASTRAR");
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

        if (cbCaracteristica.getSelectionModel().isEmpty()) {
            tituloMensagem = "Erro Caracteristica P.O.";
            corpoMensagem = "O PO possui uma caracteristica!";
            return false;
        }

        if (btnCadastrar.getText().equalsIgnoreCase("SALVAR")) {
            if (!(txtNomePO.getText().equals(poEditar.getNome())) && (daoPO.buscaPorNome(txtNomePO.getText()) != null)) {
                tituloMensagem = "Erro Salvar PO";
                corpoMensagem = "Este PO já está cadastrada no banco!";
                return false;
            }
        }

        if (btnCadastrar.getText().equalsIgnoreCase("CADASTRAR")) {
            if ((daoPO.buscaPorNome(txtNomePO.getText()) != null)) {
                tituloMensagem = "Erro Salvar Unidade";
                corpoMensagem = "Esta Unidade já está cadastrada no banco!";
                return false;
            }
        }

        return true;
    }

    @FXML
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

    private class ButtonCellFuncionalidade extends TableCell<Funcionalidade, Boolean> {

        //BOTAO REMOVER
        private Button botaoRemover = new Button();
        private final ImageView imagemRemover = new ImageView(new Image(getClass().getResourceAsStream("/icons/remover.png")));

        private ButtonCellFuncionalidade(TableView<Funcionalidade> tblView) {

            //BOTAO REMOVER
            imagemRemover.fitHeightProperty().set(16);
            imagemRemover.fitWidthProperty().set(16);

            botaoRemover.setGraphic(imagemRemover);
            botaoRemover.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    Funcionalidade funcionalidadeRemover = (Funcionalidade) tblView.getItems().get(getIndex());

                    if (funcionalidadeRemover != null) {
                        Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
                        alertVoltar.setTitle("Atenção!");
                        alertVoltar.setHeaderText("Os dados referentes à este serão perdidos, deseja continuar?");
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
                            funcionalidadesPO.remove(funcionalidadeRemover);
                            atualizarTabelaFuncionalidadesPO(funcionalidadesPO);
                        }
                    }
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty && !btnCadastrar.getText().equalsIgnoreCase("NOVO")) {
                setGraphic(botaoRemover);
                setAlignment(Pos.CENTER);
            } else {
                setGraphic(null);
            }
        }
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

                        cbCaracteristica.getSelectionModel().select(poVisualizar.getCaracteristica());

                        if (!funcionalidadesPO.isEmpty()) {
                            funcionalidadesPO.clear();
                        }
                        funcionalidadesPO.addAll(poVisualizar.getFuncionalidades());

                        atualizarTabelaFuncionalidadesPO(funcionalidadesPO);

                        txtNomePO.setDisable(true);
                        cbSituacaoPO.setDisable(true);
                        cbCaracteristica.setDisable(true);
                        cbFuncionalidadesPO.setDisable(true);
                        btnInserir.setDisable(true);

                        btnCadastrar.setText("NOVO");

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

                        cbCaracteristica.getSelectionModel().select(poEditar.getCaracteristica());
                        if (!funcionalidadesPO.isEmpty()) {
                            funcionalidadesPO.clear();
                        }
                        funcionalidadesPO.addAll(poEditar.getFuncionalidades());

                        atualizarTabelaFuncionalidadesPO(funcionalidadesPO);

                        txtNomePO.setDisable(false);
                        cbSituacaoPO.setDisable(false);
                        cbCaracteristica.setDisable(false);
                        cbFuncionalidadesPO.setDisable(false);
                        btnInserir.setDisable(false);

                        btnCadastrar.setText("SALVAR");

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
