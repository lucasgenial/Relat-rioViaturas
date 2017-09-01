package com.cicom.relatorioviaturas.controllers.adm;

import com.cicom.relatorioviaturas.DAO.PoDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.model.PO;
import com.cicom.relatorioviaturas.model.Unidade;
import java.net.URL;
import java.util.ArrayList;
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
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class TelaAdmCadastroUnidadesController implements Initializable {

    @FXML
    private TabPane root;
    @FXML
    private Tab tabCadastro;
    @FXML
    private TextField txtNomeUnidade;
    @FXML
    private ComboBox<PO> cbTipoPO = new ComboBox<>();
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnInserirPO;
    @FXML
    private TableView<PO> tablePO;
    @FXML
    private TableColumn<PO, Integer> tbColumnIdPO;
    @FXML
    private TableColumn<PO, String> tbColumnNomePO;
    @FXML
    private TableColumn<PO, Boolean> tbColumnAcaoPO;
    @FXML
    private TextField txtNomeInstituicaoUnidade;
    @FXML
    private Tab tabListagem;
    @FXML
    private TextField txtNomeBusca;
    @FXML
    private ComboBox<String> cbTipoBusca = new ComboBox<>();
    @FXML
    private TableView<Unidade> tableListaUnidades;
    @FXML
    private TableColumn<Unidade, Integer> tbColumnIdUnidade;
    @FXML
    private TableColumn<Unidade, String> tbColumnNomeUnidade;
    @FXML
    private TableColumn<Unidade, String> tbColumnNomeInstituicaoUnidade;
    @FXML
    private TableColumn<Unidade, Boolean> tbColumnAcaoUnidade;

    private PoDAO daoPO = new PoDAO();
    private UnidadeDAO daoUnidade = new UnidadeDAO();
    private Set<PO> listaPO = new HashSet<>();
    private Set<PO> listaPOsDaUnidade = new HashSet<>();
    private List<Unidade> listaUnidade = new ArrayList<>();
    private Unidade novaUnidade;
    private Unidade unidadeEditar;
    private String tituloMensagem = "";
    private String corpoMensagem = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregaDados();
    }

    private void carregaDados() {
        //Recupera todas as unidades
        listaUnidade.addAll(daoUnidade.getListAtivos());

        if (!listaUnidade.isEmpty()) {
            atualizarTabelaUnidade(listaUnidade);
        }

        //Recupera todos os POs
        listaPO.addAll(daoPO.getListAtivos());

        if (!listaPO.isEmpty()) {
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
            cbTipoPO.getItems().setAll(FXCollections.observableSet(listaPO));
        }
    }

    @FXML
    private void clickedBtnCadastrarUnidade(MouseEvent event) {
        //Se o botão salvar for ativado e o editar for verdadeiro
        //Estamos modificando um Tipo P.O. Já existente
        if (btnCadastrar.getText().equals("Novo")) {
            selectedTabListagem();
        } else if (btnCadastrar.getText().equals("Salvar")) {
            if (verificaDados()) {
                //Altera o nome do PO
                unidadeEditar.setNome(txtNomeUnidade.getText());
                unidadeEditar.setComandoDeArea(txtNomeInstituicaoUnidade.getText());
                unidadeEditar.setPos(listaPOsDaUnidade);

                daoUnidade.alterar(unidadeEditar);

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
        } else {
            if (verificaDados()) {
                novaUnidade = new Unidade();
                novaUnidade.setNome(txtNomeUnidade.getText());
                novaUnidade.setComandoDeArea(txtNomeInstituicaoUnidade.getText());
                novaUnidade.setPos(listaPOsDaUnidade);

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

    private boolean verificaDados() {
        if (txtNomeUnidade.getText().isEmpty()) {
            tituloMensagem = "Erro Nome Unidade";
            corpoMensagem = "O nome da Unidade é necessário!";
            txtNomeUnidade.setFocusTraversable(true);
            return false;
        }

        if (txtNomeInstituicaoUnidade.getText().isEmpty()) {
            tituloMensagem = "Erro Comando de Area Unidade";
            corpoMensagem = "O Comando de Área é necessário!";
            txtNomeInstituicaoUnidade.setFocusTraversable(true);
            return false;
        }

        if (listaPOsDaUnidade.isEmpty()) {
            tituloMensagem = "Erro Tipo P.O. Unidade";
            corpoMensagem = "A unidade precisará gerenciar algum tipo de P.O.!";
            cbTipoPO.setFocusTraversable(true);
            return false;
        }

        if (!(txtNomeUnidade.getText().equals(unidadeEditar.getNome())) && (daoUnidade.buscaPorNome(txtNomeUnidade.getText()) != null)) {
            tituloMensagem = "Erro Salvar Unidade";
            corpoMensagem = "Esta Unidade já está cadastrada no banco!";
            return false;
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
    private void clickedBtnInserirPO(MouseEvent event) {
        PO poSelecionado = cbTipoPO.getSelectionModel().getSelectedItem();

        if (poSelecionado != null) {
            //Carrega o item na lista de POS que comporá a tabela/Unidade
            listaPOsDaUnidade.add(poSelecionado);

            //Adiciono o item na Tabela
            atualizarTabelaPO(listaPOsDaUnidade);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário selecionar um P.O.!");
            alert.showAndWait();
        }
    }

    @FXML
    private void exitCbTipoBusca(MouseEvent event) {
    }

    @FXML
    private void selectedTabListagem() {
        txtNomeBusca.clear();
        txtNomeInstituicaoUnidade.clear();
        txtNomeUnidade.clear();
        listaPOsDaUnidade.clear();
        tablePO.getItems().clear();
        unidadeEditar = null;
    }

    private void atualizarTabelaPO(Set<PO> dados) {
        tablePO.getItems().clear();

        tbColumnNomePO.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PO, String> data) {
                return new SimpleStringProperty(data.getValue().getNome());
            }
        });

        tbColumnAcaoPO.setSortable(false);

        tbColumnAcaoPO.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PO, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<PO, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        tbColumnAcaoPO.setCellFactory(new Callback<TableColumn<PO, Boolean>, TableCell<PO, Boolean>>() {
            @Override
            public TableCell<PO, Boolean> call(TableColumn<PO, Boolean> data) {
                return new ButtonCellPO(tablePO);
            }
        });

        // 5. Add sorted (and filtered) data to the table.
        tablePO.getItems().addAll(FXCollections.observableSet(dados));
    }

    private void atualizarTabelaUnidade(List<Unidade> listaUnidade) {
        ObservableList<Unidade> dadosIniciais = FXCollections.observableList(listaUnidade);

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Unidade> filtroDeDados = new FilteredList<>(dadosIniciais, p -> true);

        txtNomeBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            filtroDeDados.setPredicate(dado -> {
                // Se o texto do filtro estiver vazio, exiba todas as pessoas.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String valorDigitado = newValue.toLowerCase();

                if (dado.getNome().toLowerCase().contains(valorDigitado) || dado.getComandoDeArea().toLowerCase().contains(valorDigitado)) {
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
                String valorDigitado = newValue;
                // Does not match.

                return dado.getAtivo().toString().equalsIgnoreCase(valorDigitado);
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Unidade> sortedData = new SortedList<>(filtroDeDados);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableListaUnidades.comparatorProperty());

        tbColumnNomeUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Unidade, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Unidade, String> data) {
                return new SimpleStringProperty(data.getValue().getNome());
            }
        });

        tbColumnNomeInstituicaoUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Unidade, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Unidade, String> data) {
                return new SimpleStringProperty(data.getValue().getComandoDeArea());
            }
        });

        tbColumnAcaoUnidade.setSortable(false);

        tbColumnAcaoUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Unidade, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Unidade, Boolean> data) {
                return new SimpleBooleanProperty(data.getValue() != null);
            }
        });

        tbColumnAcaoUnidade.setCellFactory(new Callback<TableColumn<Unidade, Boolean>, TableCell<Unidade, Boolean>>() {
            @Override
            public TableCell<Unidade, Boolean> call(TableColumn<Unidade, Boolean> data) {
                return new ButtonCellUnidade(tableListaUnidades);
            }
        });

        // 5. Add sorted (and filtered) data to the table.
        tableListaUnidades.setItems(sortedData);
    }

    private class ButtonCellPO extends TableCell<PO, Boolean> {

        //BOTAO REMOVER
        private Button botaoRemover = new Button();
        private final ImageView imagemRemover = new ImageView(new Image(getClass().getResourceAsStream("/icons/remover.png")));

        private ButtonCellPO(TableView<PO> tblView) {

            //BOTAO REMOVER
            imagemRemover.fitHeightProperty().set(16);
            imagemRemover.fitWidthProperty().set(16);

            botaoRemover.setGraphic(imagemRemover);
            botaoRemover.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    PO poRemover = (PO) tblView.getItems().get(getIndex());

                    if (poRemover != null) {
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
                            listaPOsDaUnidade.remove(poRemover);
                            atualizarTabelaPO(listaPOsDaUnidade);
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

    //    Define os botões de ação da MESA
    private class ButtonCellUnidade extends TableCell<Unidade, Boolean> {

        //BOTAO REMOVER
        private Button botaoRemover = new Button();
        private final ImageView imagemRemover = new ImageView(new Image(getClass().getResourceAsStream("/icons/remover.png")));

        //BOTAO EDITAR
        private Button botaoEditar = new Button();
        private final ImageView imagemEditar = new ImageView(new Image(getClass().getResourceAsStream("/icons/editar.png")));

        //BOTAO EDITAR
        private Button botaoVisualizar = new Button();
        private final ImageView imagemVisualizar = new ImageView(new Image(getClass().getResourceAsStream("/icons/visualizar.png")));

        private ButtonCellUnidade(TableView<Unidade> tblView) {
            //BOTAO VISUALIZAR
            imagemVisualizar.fitHeightProperty().set(16);
            imagemVisualizar.fitWidthProperty().set(16);
            botaoVisualizar.setGraphic(imagemVisualizar);
            botaoVisualizar.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    Unidade unidadeVisualizar = (Unidade) tblView.getItems().get(getIndex());
                    if (unidadeVisualizar != null) {
                        txtNomeUnidade.setText(unidadeVisualizar.getNome());
                        txtNomeInstituicaoUnidade.setText(unidadeVisualizar.getComandoDeArea());
                        listaPOsDaUnidade = unidadeVisualizar.getPos();

                        atualizarTabelaPO(listaPOsDaUnidade);

                        txtNomeUnidade.setDisable(true);
                        txtNomeInstituicaoUnidade.setDisable(true);
                        cbTipoPO.setDisable(true);
                        tablePO.setEditable(false);
                        btnCadastrar.setText("Novo");

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
                    unidadeEditar = (Unidade) tblView.getItems().get(getIndex());

                    if (unidadeEditar != null) {
                        txtNomeUnidade.setText(unidadeEditar.getNome());
                        txtNomeInstituicaoUnidade.setText(unidadeEditar.getComandoDeArea());
                        listaPOsDaUnidade = unidadeEditar.getPos();

                        atualizarTabelaPO(listaPOsDaUnidade);

                        txtNomeUnidade.setDisable(false);
                        txtNomeInstituicaoUnidade.setDisable(false);
                        tablePO.setEditable(true);
                        btnCadastrar.setDisable(false);
                        btnCadastrar.setText("Salvar");

                        root.getSelectionModel().select(tabCadastro);
                    }
                }
            });

            //BOTAO REMOVER
            imagemRemover.fitHeightProperty().set(16);
            imagemRemover.fitWidthProperty().set(16);

            botaoRemover.setGraphic(imagemRemover);

            botaoRemover.setOnAction((ActionEvent t) -> {
                Unidade unidadeRemover = (Unidade) tblView.getItems().get(getIndex());

                if (unidadeRemover != null) {
                    Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
                    alertVoltar.setTitle("Atenção!");
                    alertVoltar.setHeaderText("Os dados referentes à esta UNIDADE serão perdidos, deseja continuar?");
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
                        unidadeRemover.setAtivo(false);

                        daoUnidade.alterar(unidadeRemover);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sucesso!");
                        alert.setHeaderText("UNIDADE excluída com sucesso!");
                        alert.showAndWait();
                        carregaDados();
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
                hb.getChildren().add(botaoRemover);
                setGraphic(hb);
            } else {
                setGraphic(null);
            }
        }
    }
}
