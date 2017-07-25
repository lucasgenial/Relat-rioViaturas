package com.cicom.relatorioviaturas.controllers.adm;

import com.cicom.relatorioviaturas.DAO.MesaDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.model.Mesa;
import com.cicom.relatorioviaturas.model.Servidor;
import com.cicom.relatorioviaturas.model.Unidade;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 *
 * @author Lucas Matos
 */
public class TelaAdmCadastroMesasController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private SplitPane splitPanePrincipal;
    @FXML
    private TableView<Mesa> tableMesa;
    @FXML
    private TableColumn<Mesa, Integer> tbColumnIdMesa;
    @FXML
    private TableColumn<Mesa, String> tbColumnNomeMesa;
    @FXML
    private TextField txtNomeMesa;
    @FXML
    private ComboBox<Unidade> cbUnidades;
    @FXML
    private TableView<Unidade> tableUnidadesControladas;
    @FXML
    private TableColumn<Unidade, Integer> tbColumnIdUnidade;
    @FXML
    private TableColumn<Unidade, String> tbColumnNomeUnidade;
    @FXML
    private Label labelNumeroUnidade;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnAdicionarUnidade;
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnRemoverUnidade;
    @FXML
    private TextField txtBuscaMesa;

    /*
    Extra
     */
    private UnidadeDAO daoUnidade = new UnidadeDAO();
    private MesaDAO daoMesa = new MesaDAO();
    private Mesa novaMesa;
    private Set<Unidade> listaDeUnidades;
    private List<Unidade> listaDeUnidadesCombo;
    private List<Mesa> listaDeMesas;
    private boolean editar = false;
    private String tituloMensagem;
    private String corpoMensagem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregaDados();
    }

    @FXML
    private void clickedTableMesa() {
        novaMesa = tableMesa.getSelectionModel().getSelectedItem();
        txtNomeMesa.setText(novaMesa.getNome());
        labelNumeroUnidade.setText((novaMesa.getUnidades() != null) ? (novaMesa.getUnidades().size() + " Unidades Controladas") : ("0 Unidade Controlada"));
        listaDeUnidades = (novaMesa.getUnidades() != null) ? novaMesa.getUnidades() : null;
        tableUnidadesControladas.getItems().setAll(FXCollections.observableSet(listaDeUnidades));

        btnSalvar.setText("Editar");
        btnSalvar.setDisable(false);

        txtNomeMesa.setDisable(true);
        cbUnidades.setDisable(true);
        btnAdicionarUnidade.setDisable(true);
        editar = false;
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
    private void clickedBtnSalvar() {
        //Ver a ação no botão editar
        if (btnSalvar.getText().equals("Editar")) {
            txtNomeMesa.setDisable(false);
            cbUnidades.setDisable(false);
            btnSalvar.setDisable(false);
            btnAdicionarUnidade.setDisable(false);
            btnSalvar.setText("Salvar");
            editar = true;

            //Se o botão salvar for ativado e o editar for verdadeiro
            //Estamos modificando um Tipo P.O. Já existente
        } else if (btnSalvar.getText().equals("Salvar") && editar) {
            if (verificaDados()) {
                //Altera o nome do PO
                novaMesa.setNome(txtNomeMesa.getText());
                novaMesa.setUnidades(listaDeUnidades);

                daoMesa.alterar(novaMesa);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Mesa alterada com sucesso!");
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
                novaMesa = new Mesa();
                novaMesa.setNome(txtNomeMesa.getText());
                novaMesa.setUnidades(listaDeUnidades);

                daoMesa.salvar(novaMesa);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Nova Mesa cadastrada!");
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
        txtNomeMesa.setText("");
        txtNomeMesa.setDisable(false);
        cbUnidades.setDisable(false);
        btnAdicionarUnidade.setDisable(false);
        btnSalvar.setDisable(false);
        editar = false;
        novaMesa = null;
    }

    @FXML
    private void clickedCbUnidades() {
    }

    @FXML
    private void clickedTableUnidadesControladas() {
        btnRemoverUnidade.setDisable(false);
    }

    private void carregaDados() {
        txtNomeMesa.setDisable(true);
        cbUnidades.setDisable(true);
        btnAdicionarUnidade.setDisable(true);
        btnSalvar.setDisable(true);
        btnSalvar.setText("Editar");

        //Carrega a lista de Funções
        listaDeUnidadesCombo = daoUnidade.getList("FROM Unidade");
        listaDeMesas = daoMesa.getList("FROM Mesa");

        if (!listaDeUnidadesCombo.isEmpty()) {
            //Carrega os dados dos POs no ComboBox
            cbUnidades.setConverter(new StringConverter<Unidade>() {
                @Override
                public String toString(Unidade item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public Unidade fromString(String string) {
                    return daoUnidade.buscaPorNome(string);
                }

            });
            cbUnidades.getItems().setAll(FXCollections.observableList(listaDeUnidadesCombo));

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Unidade");
            alert.setHeaderText("Possivelmente não há Unidades cadastradas/diponiveis no sistema!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }

        //Carrega as Unidades cadastradas no banco de dados
        if (!listaDeMesas.isEmpty()) {
            ObservableList<Mesa> dadosIniciais = FXCollections.observableList(listaDeMesas);

            // 1. Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<Mesa> filtroDeDados = new FilteredList<>(dadosIniciais, p -> true);

            txtBuscaMesa.textProperty().addListener((observable, oldValue, newValue) -> {
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
            SortedList<Mesa> sortedData = new SortedList<>(filtroDeDados);

            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(tableMesa.comparatorProperty());

            // 5. Add sorted (and filtered) data to the table.
            tableMesa.getItems().setAll(FXCollections.observableList(listaDeMesas));
        }
    }

    private boolean verificaDados() {
        if (txtNomeMesa.getText().isEmpty()) {
            tituloMensagem = "Erro Nome Mesa";
            corpoMensagem = "O nome da Mesa é necessário!";
            txtNomeMesa.setFocusTraversable(true);
            return false;
        }

        if (listaDeUnidades.isEmpty()) {
            tituloMensagem = "Erro Unidades Mesa";
            corpoMensagem = "A Mesa precisará gerenciar alguma Unidade!";
            cbUnidades.setFocusTraversable(true);
            return false;
        }

        if (daoMesa.buscaPorObjeto(novaMesa) != null) {
            tituloMensagem = "Erro Salvar Mesa";
            corpoMensagem = "Esta Mesa encontra-se cadastrada no banco!";
            return false;
        }

        return true;
    }

    @FXML
    private void clickedBtnAdicionarUnidade() {
        Unidade unidadeSelecionada = cbUnidades.getSelectionModel().getSelectedItem();

        if (unidadeSelecionada != null) {
            //Carrega o item na lista de POS que comporá a tabela/Unidade
            listaDeUnidades.add(unidadeSelecionada);

            //Adiciono o item na Tabela
            tableUnidadesControladas.getItems().setAll(FXCollections.observableSet(listaDeUnidades));

            labelNumeroUnidade.setText(tableUnidadesControladas.getItems().size() + " Unidades Controladas");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário selecionar uma Unidade!");
            alert.showAndWait();
        }
        tableUnidadesControladas.setFocusTraversable(true);
    }

    @FXML
    private void clickedBtnRemoverUnidade() {
        Unidade unidadeSelecionada = tableUnidadesControladas.getSelectionModel().getSelectedItem();

        if (unidadeSelecionada != null) {
            //Carrega o item na lista de POS que comporá a tabela/Unidade
            listaDeUnidades.remove(unidadeSelecionada);

            //Adiciono o item na Tabela
            tableUnidadesControladas.getItems().setAll(FXCollections.observableSet(listaDeUnidades));

            labelNumeroUnidade.setText(tableUnidadesControladas.getItems().size() + " Unidades Controladas");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário selecionar uma Unidade na tabela abaixo!");
            alert.showAndWait();
        }
        tableUnidadesControladas.setFocusTraversable(true);
        btnRemoverUnidade.setDisable(true);
    }
}
