package com.cicom.relatorioviaturas.controllers.adm;

import com.cicom.relatorioviaturas.DAO.MesaDAO;
import com.cicom.relatorioviaturas.DAO.TipoMesaDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.model.Mesa;
import com.cicom.relatorioviaturas.model.TipoMesa;
import com.cicom.relatorioviaturas.model.Unidade;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
public class TelaAdmCadastroMesasController implements Initializable {

    @FXML
    private TabPane root;
    @FXML
    private Tab tabCadastro;
    @FXML
    private TextField txtNome;
    @FXML
    private ComboBox<TipoMesa> cbTipoMesa;
    @FXML
    private ComboBox<Unidade> cbUnidade;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnInserirUnidade;
    @FXML
    private Button btnNovo;
    @FXML
    private TableView<Unidade> tableUnidades;
    @FXML
    private TableColumn<Unidade, Integer> tbColumnIdUnidade;
    @FXML
    private TableColumn<Unidade, String> tbColumnNomeUnidade;
    @FXML
    private TableColumn<Unidade, Boolean> tbColumnAcaoUnidade;
    @FXML
    private Tab tabListagem;
    @FXML
    private TextField txtNomeBusca;
    @FXML
    private ComboBox<TipoMesa> cbTipoBusca;
    @FXML
    private TableView<Mesa> tableListaMesa;
    @FXML
    private TableColumn<Mesa, Integer> tbColumnIdMesa;
    @FXML
    private TableColumn<Mesa, String> tbColumnNomeMesa;
    @FXML
    private TableColumn<Mesa, String> tbColumnTipoMesa;
    @FXML
    private TableColumn<Mesa, Boolean> tbColumnAcaoMesa;

    //
    private MesaDAO daoMesa = new MesaDAO();
    private TipoMesaDAO daoTipoMesa = new TipoMesaDAO();
    private UnidadeDAO daoUnidade = new UnidadeDAO();
    private List<Mesa> listaDeMesa;
    private List<TipoMesa> listaDeTipoMesa;
    private List<Unidade> listaDeUnidadesBanco;
    private Set<Unidade> listaDeUnidadeCadastro = new HashSet<>();

    private Mesa mesaEditar;
    private Mesa novaMesa;

    private String tituloMensagem;
    private String corpoMensagem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregaDados();
    }

    private void carregaDados() {
        listaDeMesa = daoMesa.getListAtivos();
        listaDeTipoMesa = daoTipoMesa.getList("From TipoMesa");
        listaDeUnidadesBanco = daoUnidade.getListAtivos();

        if (!listaDeMesa.isEmpty()) {

            ObservableList<Mesa> dadosIniciais = FXCollections.observableList(listaDeMesa);

            // 1. Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<Mesa> filtroDeDados = new FilteredList<>(dadosIniciais, p -> true);

            txtNomeBusca.textProperty().addListener((observable, oldValue, newValue) -> {
                filtroDeDados.setPredicate(dado -> {
                    // Se o texto do filtro estiver vazio, exiba todas as pessoas.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare first name and last name of every person with filter text.
                    String valorDigitado = newValue.toLowerCase();

                    if (dado.getNome().toLowerCase().contains(valorDigitado)) {
                        return true; // Filtro corresponde ao primeiro nome.
                    }
                    return false; // Does not match.
                });
            });

            cbTipoBusca.valueProperty().addListener((observable, oldValue, newValue) -> {
                filtroDeDados.setPredicate(dado -> {
                    // Se o combobox estiver vazio, exiba todas as pessoas.
                    if (newValue == null) {
                        return true;
                    }

                    // Compare o primeiro valor.
                    TipoMesa valorDigitado = newValue;

                    if (dado.getTipoMesa().equals(valorDigitado)) {
                        return true; // Filtro corresponde ao primeiro nome.
                    }

                    return false; // Does not match.
                });
            });

            // 3. Wrap the FilteredList in a SortedList. 
            SortedList<Mesa> sortedData = new SortedList<>(filtroDeDados);

            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(tableListaMesa.comparatorProperty());

            tbColumnNomeMesa.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mesa, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Mesa, String> data) {
                    return new SimpleStringProperty(data.getValue().getNome());
                }
            });

            tbColumnTipoMesa.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mesa, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Mesa, String> data) {
                    return new SimpleStringProperty(data.getValue().getTipoMesa().getNome());
                }
            });

            tbColumnAcaoMesa.setSortable(false);

            tbColumnAcaoMesa.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Mesa, Boolean>, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Mesa, Boolean> data) {
                    return new SimpleBooleanProperty(data.getValue() != null);
                }
            });

            tbColumnAcaoMesa.setCellFactory(new Callback<TableColumn<Mesa, Boolean>, TableCell<Mesa, Boolean>>() {
                @Override
                public TableCell<Mesa, Boolean> call(TableColumn<Mesa, Boolean> data) {
                    return new ButtonCellMesa(tableListaMesa);
                }
            });

            // 5. Add sorted (and filtered) data to the table.
            tableListaMesa.setItems(sortedData);
        }

        if (!listaDeTipoMesa.isEmpty()) {
            //Converte as opções para o modo String
            cbTipoMesa.setConverter(new StringConverter<TipoMesa>() {
                @Override
                public String toString(TipoMesa item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public TipoMesa fromString(String string) {
                    return daoTipoMesa.buscaPorNome(string);
                }
            });

            //Lança no combobox para busca
            cbTipoMesa.setItems(FXCollections.observableList(listaDeTipoMesa));

            cbTipoBusca.setConverter(new StringConverter<TipoMesa>() {
                @Override
                public String toString(TipoMesa item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public TipoMesa fromString(String string) {
                    return daoTipoMesa.buscaPorNome(string);
                }
            });

            cbTipoBusca.setItems(FXCollections.observableList(listaDeTipoMesa));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Tipo Mesa");
            alert.setHeaderText("Possivelmente não há Tipo de Mesa cadastradas!\n"
                    + "Entre em contato com o administrador do sistema!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }

        if (!listaDeUnidadesBanco.isEmpty()) {
            //Converte as opções para o modo String
            cbUnidade.setConverter(new StringConverter<Unidade>() {
                @Override
                public String toString(Unidade item) {
                    if (item != null) {
                        return item.getNome() + " - " + item.getComandoDeArea();
                    }
                    return "";
                }

                @Override
                public Unidade fromString(String string) {
                    return daoUnidade.buscaPorNome(string);
                }
            });

            //Lança no combobox para busca
            cbUnidade.setItems(FXCollections.observableList(listaDeUnidadesBanco));

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Unidade");
            alert.setHeaderText("Possivelmente não há Unidades cadastradas!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }

    }

    @FXML
    private void clickedBtnCadastrarMesa(MouseEvent event) {

        if (verificaDados()) {
            if (btnCadastrar.getText().equalsIgnoreCase("CADASTRAR")) {
                novaMesa = new Mesa();
                novaMesa.setNome(txtNome.getText());
                novaMesa.setTipoMesa(cbTipoMesa.getSelectionModel().getSelectedItem());
                novaMesa.setUnidades(listaDeUnidadeCadastro);
                novaMesa.setAtivo(true);

                daoMesa.salvar(novaMesa);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("MESA cadastrada com sucesso!");
                alert.showAndWait();
                carregaDados();
                limparDados();
            } else if (btnCadastrar.getText().equalsIgnoreCase("SALVAR")) {
                mesaEditar.setNome(txtNome.getText());
                mesaEditar.setTipoMesa(cbTipoMesa.getSelectionModel().getSelectedItem());
                mesaEditar.setUnidades(listaDeUnidadeCadastro);
                mesaEditar.setAtivo(true);

                daoMesa.alterar(mesaEditar);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("MESA modificada com sucesso!");
                alert.showAndWait();
                carregaDados();
                limparDados();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(tituloMensagem);
            alert.setHeaderText(corpoMensagem);
            alert.showAndWait();
        }

    }

    private boolean verificaDados() {
        if (!txtNome.getText().isEmpty()) {
            if (daoMesa.buscaPorNome(txtNome.getText()) != null) {
                tituloMensagem = "Erro Nome Mesa";
                corpoMensagem = "Já existe MESA cadastrada com este nome!";
                return false;
            }
        } else {
            tituloMensagem = "Erro Nome Mesa";
            corpoMensagem = "O Nome da MESA é obrigatório!";
            return false;
        }

        if (listaDeUnidadeCadastro.isEmpty()) {
            tituloMensagem = "Erro Lista Unidade";
            corpoMensagem = "É necessário pelo menos 1 unidade!";
            return false;
        }

        return true;
    }

    @FXML
    private void clickedBtnNovo(MouseEvent event) {
        txtNome.setText(null);
        tableUnidades.getItems().clear();
        cbUnidade.setValue(null);
        cbTipoMesa.setValue(null);
        listaDeUnidadeCadastro.clear();
        mesaEditar = null;

        btnCadastrar.setText("CADASTRAR");
        btnNovo.setDisable(true);
        txtNome.setDisable(false);
        cbTipoMesa.setDisable(false);
        cbUnidade.setDisable(false);
        btnInserirUnidade.setDisable(false);
        tableUnidades.setDisable(false);
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
    private void clickedBtnInserirUnidade() {
        Unidade unidadeSelecionada = cbUnidade.getSelectionModel().getSelectedItem();

        if (unidadeSelecionada != null) {

            if (listaDeUnidadeCadastro == null) {
                listaDeUnidadeCadastro = new HashSet<>();
            }

            //Carrega o item na lista de POS que comporá a tabela/Unidade
            listaDeUnidadeCadastro.add(unidadeSelecionada);
            atualizarTabelaUnidade(listaDeUnidadeCadastro);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário selecionar uma Unidade!");
            alert.showAndWait();
        }
        tableUnidades.setFocusTraversable(true);
    }

    @FXML
    private void exitCbTipoBusca(MouseEvent event) {
    }

    @FXML
    private void selectedTabListagem() {
//        limparDados();
    }

    private void limparDados() {
        novaMesa = null;
        mesaEditar = null;
        listaDeUnidadeCadastro = null;
        txtNome.setText("");
        cbTipoMesa.setValue(null);
        cbUnidade.setValue(null);
        tableUnidades.getItems().clear();

        btnCadastrar.setText("CADASTRAR");
    }

    private void atualizarTabelaUnidade(Set<Unidade> dados) {

        tbColumnNomeUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Unidade, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Unidade, String> data) {
                return new SimpleStringProperty(data.getValue().getNome() + " - " + data.getValue().getComandoDeArea());
            }
        });

        tbColumnAcaoUnidade.setSortable(false);

        tbColumnAcaoUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Unidade, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Unidade, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        tbColumnAcaoUnidade.setCellFactory(new Callback<TableColumn<Unidade, Boolean>, TableCell<Unidade, Boolean>>() {
            @Override
            public TableCell<Unidade, Boolean> call(TableColumn<Unidade, Boolean> data) {
                return new ButtonCellUnidade(tableUnidades);
            }
        });

        tableUnidades.getItems().setAll(FXCollections.observableSet(dados));
    }

    //    Define os botões de ação da MESA
    private class ButtonCellMesa extends TableCell<Mesa, Boolean> {

        HBox hb = new HBox();

        //BOTAO REMOVER
        private Button botaoRemover = new Button();
        private final ImageView imagemRemover = new ImageView(new Image(getClass().getResourceAsStream("/icons/remover.png")));

        //BOTAO EDITAR
        private Button botaoEditar = new Button();
        private final ImageView imagemEditar = new ImageView(new Image(getClass().getResourceAsStream("/icons/editar.png")));

        //BOTAO EDITAR
        private Button botaoVisualizar = new Button();
        private final ImageView imagemVisualizar = new ImageView(new Image(getClass().getResourceAsStream("/icons/visualizar.png")));

        private ButtonCellMesa(TableView<Mesa> tblView) {
            //BOTAO VISUALIZAR
            imagemVisualizar.fitHeightProperty().set(16);
            imagemVisualizar.fitWidthProperty().set(16);
            botaoVisualizar.setGraphic(imagemVisualizar);
            botaoVisualizar.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    Mesa mesaVisualizar = (Mesa) tblView.getItems().get(getIndex());
                    if (mesaVisualizar != null) {
                        txtNome.setText(mesaVisualizar.getNome());
                        cbTipoMesa.setValue(mesaVisualizar.getTipoMesa());

                        tableUnidades.getItems().setAll(mesaVisualizar.getUnidades());

                        txtNome.setDisable(true);
                        cbUnidade.setDisable(true);
                        cbTipoMesa.setDisable(true);
                        tableUnidades.setDisable(false);
                        btnCadastrar.setDisable(true);
                        btnInserirUnidade.setDisable(true);
                        btnNovo.setDisable(false);

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
                    mesaEditar = (Mesa) tblView.getItems().get(getIndex());

                    if (mesaEditar != null) {
                        txtNome.setText(mesaEditar.getNome());
                        cbTipoMesa.setValue(mesaEditar.getTipoMesa());
                        listaDeUnidadeCadastro = mesaEditar.getUnidades();
//                        tableUnidades.getItems().setAll(listaDeUnidadeCadastro);
                        atualizarTabelaUnidade(listaDeUnidadeCadastro);

                        txtNome.setDisable(false);
                        cbUnidade.setDisable(false);
                        cbTipoMesa.setDisable(false);
                        tableUnidades.setDisable(false);
                        btnCadastrar.setDisable(false);
                        btnInserirUnidade.setDisable(false);
                        btnNovo.setDisable(false);
                        btnCadastrar.setText("SALVAR");

                        root.getSelectionModel().select(tabCadastro);
                    }
                }
            });

            //BOTAO REMOVER
            imagemRemover.fitHeightProperty().set(16);
            imagemRemover.fitWidthProperty().set(16);

            botaoRemover.setGraphic(imagemRemover);

            botaoRemover.setOnAction((ActionEvent t) -> {
                Mesa mesaRemover = (Mesa) tblView.getItems().get(getIndex());

                if (mesaRemover != null) {
                    Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
                    alertVoltar.setTitle("Atenção!");
                    alertVoltar.setHeaderText("Os dados referentes à esta MESA serão perdidos, deseja continuar?");
                    alertVoltar.getButtonTypes().clear();
                    alertVoltar.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

                    //Deactivate Defaultbehavior for yes-Button:
                    Button yesButton = (Button) alertVoltar.getDialogPane().lookupButton(ButtonType.YES);
                    yesButton.setDefaultButton(false);

                    //Activate Defaultbehavior for no-Button:
                    Button noButton = (Button) alertVoltar.getDialogPane().lookupButton(ButtonType.NO);
                    noButton.setDefaultButton(true);

                    //Pega qual opção o usuario pressionou
                    final Optional<ButtonType> resultado1 = alertVoltar.showAndWait();

                    if (resultado1.get() == ButtonType.YES) {
                        mesaRemover.setAtivo(false);

                        daoMesa.alterar(mesaRemover);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sucesso!");
                        alert.setHeaderText("MESA excluída com sucesso!");
                        alert.showAndWait();
                        carregaDados();
                    }
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            hb.setAlignment(Pos.CENTER);

            super.updateItem(t, empty);
            if (!empty) {
                hb.getChildren().add(botaoVisualizar);
                hb.getChildren().add(botaoEditar);
                hb.getChildren().add(botaoRemover);

                setGraphic(hb);
            } else {
                setGraphic(null);
            }
        }
    }

    private class ButtonCellUnidade extends TableCell<Unidade, Boolean> {

        //BOTAO REMOVER
        private Button botaoRemover = new Button();
        private final ImageView imagemRemover = new ImageView(new Image(getClass().getResourceAsStream("/icons/remover.png")));

        private ButtonCellUnidade(TableView<Unidade> tblView) {

            //BOTAO REMOVER
            imagemRemover.fitHeightProperty().set(16);
            imagemRemover.fitWidthProperty().set(16);

            botaoRemover.setGraphic(imagemRemover);
            botaoRemover.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    Unidade unidadeRemover = (Unidade) tblView.getItems().get(getIndex());

                    if (unidadeRemover != null) {
                        Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
                        alertVoltar.setTitle("Atenção!");
                        alertVoltar.setHeaderText("Os dados referentes à esta Unidade serão perdidos, deseja continuar?");
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
                            listaDeUnidadeCadastro.remove(unidadeRemover);
                            atualizarTabelaUnidade(listaDeUnidadeCadastro);
                        }
                    }
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(botaoRemover);
                setAlignment(Pos.CENTER);
            } else {
                setGraphic(null);
            }
        }
    }
}
