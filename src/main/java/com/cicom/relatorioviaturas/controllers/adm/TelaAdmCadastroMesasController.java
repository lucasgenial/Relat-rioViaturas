package com.cicom.relatorioviaturas.controllers.adm;

import com.cicom.relatorioviaturas.DAO.MesaDAO;
import com.cicom.relatorioviaturas.DAO.TipoMesaDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.MainApp;
import com.cicom.relatorioviaturas.model.Instituicao;
import com.cicom.relatorioviaturas.model.Mesa;
import com.cicom.relatorioviaturas.model.PO;
import com.cicom.relatorioviaturas.model.Servidor;
import com.cicom.relatorioviaturas.model.TipoMesa;
import com.cicom.relatorioviaturas.model.Unidade;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
    private List<Unidade> listaDeUnidades;

    Mesa mesaSelecionada;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregaDados();
    }

    private void carregaDados() {
        listaDeMesa = daoMesa.getListAtivos();
        listaDeTipoMesa = daoTipoMesa.getList("From TipoMesa");
        listaDeUnidades = daoUnidade.getListAtivos();

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
                    return new ButtonCell(tableListaMesa);
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

        if (!listaDeUnidades.isEmpty()) {
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
            cbUnidade.setItems(FXCollections.observableList(listaDeUnidades));

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Unidade");
            alert.setHeaderText("Possivelmente não há Unidades cadastradas!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }
    }

//    Define the button cell
    private class ButtonCell extends TableCell<Mesa, Boolean> {

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

        ButtonCell(final TableView<Mesa> tblView) {

            //BOTAO VISUALIZAR
            imagemVisualizar.fitHeightProperty().set(16);
            imagemVisualizar.fitWidthProperty().set(16);
            botaoVisualizar.setGraphic(imagemVisualizar);
            botaoVisualizar.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    Mesa mesaVisualizar = (Mesa) tblView.getItems().get(getIndex());
                    System.out.println("MESA SELECIONADA: " + mesaVisualizar);

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
                    Mesa mesaEditar = (Mesa) tblView.getItems().get(getIndex());
                    System.out.println("MESA SELECIONADA: " + mesaEditar);

                    if (mesaEditar != null) {
                        txtNome.setText(mesaEditar.getNome());
                        cbTipoMesa.setValue(mesaEditar.getTipoMesa());
                        tableUnidades.getItems().setAll(mesaEditar.getUnidades());

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

            botaoRemover.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    Mesa mesaRemover = (Mesa) tblView.getItems().get(getIndex());
                    System.out.println("MESA SELECIONADA: " + mesaRemover);

                    if (mesaRemover != null) {
                        Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
                        alertVoltar.setTitle("Atenção!");
                        alertVoltar.setHeaderText("Os dados referentes à esta Mesa serão perdidos, deseja continuar?");
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
                            mesaRemover.setAtivo(false);

                            daoMesa.alterar(mesaRemover);

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Sucesso!");
                            alert.setHeaderText("Tipo Mesa excluído com sucesso!");
                            alert.showAndWait();
                            carregaDados();
                        }
                    }
                }
            });
        }
//        Display button if the row is not empty

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

    @FXML
    private void clickedBtnCadastrarMesa(MouseEvent event) {
    }
    
    @FXML
    private void clickedBtnNovo(MouseEvent event) {
        txtNome.setText(null);
        tableUnidades.getItems().clear();
        btnNovo.setDisable(true);
    }

    @FXML
    private void clickedBtnVoltar(MouseEvent event) {
    }

    @FXML
    private void clickedBtnInserirUnidade(MouseEvent event) {
    }

    @FXML
    private void exitCbTipoBusca(MouseEvent event) {
    }

}
