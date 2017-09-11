package com.cicom.relatorioefetivos.controllers.adm;

import com.cicom.relatorioefetivos.DAO.InstituicaoDAO;
import com.cicom.relatorioefetivos.DAO.ServidorDAO;
import com.cicom.relatorioefetivos.DAO.SexoDAO;
import com.cicom.relatorioefetivos.model.Instituicao;
import com.cicom.relatorioefetivos.model.Servidor;
import com.cicom.relatorioefetivos.model.Sexo;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Lucas Matos e Souza
 */
public class TelaAdmCadastroServidorController implements Initializable {

    @FXML
    private TabPane root;
    @FXML
    private Tab tabCadastro;
    @FXML
    private TextField txtMatricula;
    @FXML
    private ComboBox<Instituicao> cbInstituicao;
    @FXML
    private TextField txtGrauHierarquico;
    @FXML
    private ComboBox<Sexo> cbSexo;
    @FXML
    private TextField txtNome;
    @FXML
    private TextArea txtObservacao;
    @FXML
    private Button btnAdicionaFoto;
    @FXML
    private ImageView imagemFoto;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnVoltar;
    @FXML
    private Tab tabListagem;
    @FXML
    private TextField txtNomeBusca;

    @FXML
    private ComboBox<Instituicao> cbInstituicaoBusca;
    @FXML
    private TableView<Servidor> tableListaServidores;
    @FXML
    private TableColumn<Servidor, Integer> tbColumnId;
    @FXML
    private TableColumn<Servidor, String> tbColumnMatricula;
    @FXML
    private TableColumn<Servidor, String> tbColumnGrauHierarquico;
    @FXML
    private TableColumn<Servidor, String> tbColumnNome;
    @FXML
    private Button btnVerDetalhesServidor;
    @FXML
    private Button btnVoltar2;

    /*
    EXTRA
     */
    private ServidorDAO daoServidor = new ServidorDAO();
    private InstituicaoDAO daoInstituicao = new InstituicaoDAO();
    private SexoDAO daoSexo = new SexoDAO();
    private List<Servidor> listaDeServidor;
    private List<Instituicao> listaDeInstituicao;
    private List<Sexo> listaDeSexo;
    private boolean editar = false;
    private String tituloMensagem;
    private String corpoMensagem;
    private Servidor novoServidor;
    private Servidor servidorSelecionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        carregaDados();
        btnExcluir.setDisable(true);
    }

    private void carregaDados() {
        listaDeServidor = daoServidor.getListAtivos();
        listaDeInstituicao = daoInstituicao.getList("From Instituicao");
        listaDeSexo = daoSexo.getList("From Sexo");

        if (!listaDeServidor.isEmpty()) {

            ObservableList<Servidor> dadosIniciais = FXCollections.observableList(listaDeServidor);

            // 1. Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<Servidor> filtroDeDados = new FilteredList<>(dadosIniciais, p -> true);

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

            cbInstituicaoBusca.valueProperty().addListener((observable, oldValue, newValue) -> {
                filtroDeDados.setPredicate(dado -> {
                    // Se o combobox estiver vazio, exiba todas as pessoas.
                    if (newValue == null) {
                        return true;
                    }

                    // Compare o primeiro valor.
                    Instituicao valorDigitado = newValue;

                    if (dado.getInstituicao().equals(valorDigitado)) {
                        return true; // Filtro corresponde ao primeiro nome.
                    }

                    return false; // Does not match.
                });
            });

            // 3. Wrap the FilteredList in a SortedList. 
            SortedList<Servidor> sortedData = new SortedList<>(filtroDeDados);

            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(tableListaServidores.comparatorProperty());

            tbColumnMatricula.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Servidor, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Servidor, String> data) {
                    return new SimpleStringProperty(data.getValue().getMatricula());
                }
            });

            tbColumnGrauHierarquico.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Servidor, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Servidor, String> data) {
                    return new SimpleStringProperty(data.getValue().getGrauHierarquico());
                }
            });

            tbColumnNome.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Servidor, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Servidor, String> data) {
                    return new SimpleStringProperty(data.getValue().getNome());
                }
            });

            // 5. Add sorted (and filtered) data to the table.
            tableListaServidores.setItems(sortedData);

        }

        if (!listaDeInstituicao.isEmpty()) {
            //Converte as opções para o modo String
            cbInstituicao.setConverter(new StringConverter<Instituicao>() {
                @Override
                public String toString(Instituicao item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public Instituicao fromString(String string) {
                    return daoInstituicao.buscaPorNome(string);
                }
            });

            //Lança no combobox para busca
            cbInstituicao.setItems(FXCollections.observableList(listaDeInstituicao));

            cbInstituicaoBusca.setConverter(new StringConverter<Instituicao>() {
                @Override
                public String toString(Instituicao item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public Instituicao fromString(String string) {
                    return daoInstituicao.buscaPorNome(string);
                }
            });

            //Lança todos os servidores cadastrados para a escolha do usuário do Supervisor
            cbInstituicaoBusca.setItems(FXCollections.observableList(listaDeInstituicao));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Instituição");
            alert.setHeaderText("Possivelmente não há Instituições cadastradas/diponiveis na unidade!\n"
                    + "Entre em contato com o administrador do sistema!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }

        if (!listaDeSexo.isEmpty()) {
            //Converte as opções para o modo String
            cbSexo.setConverter(new StringConverter<Sexo>() {
                @Override
                public String toString(Sexo item) {
                    if (item != null) {
                        return item.name();
                    }
                    return "";
                }

                @Override
                public Sexo fromString(String string) {
                    return daoSexo.buscaPorNome(string);
                }
            });

            //Lança todos os servidores cadastrados para a escolha do usuário do Supervisor
            cbSexo.setItems(FXCollections.observableList(listaDeSexo));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Sexo");
            alert.setHeaderText("Possivelmente não há Tipo de Sexo cadastradas/diponiveis na unidade!\n"
                    + "Entre em contato com o administrador do sistema!");
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }
    }

    @FXML
    private void clickedAdicionarFoto(MouseEvent event) {

    }

    @FXML
    private void clickedCadastrarServidor(MouseEvent event) throws IOException {

        if (btnCadastrar.getText().equals("EDITAR")) {
            //Habilita os campos
            txtMatricula.setDisable(false);
            cbInstituicao.setDisable(false);
            txtGrauHierarquico.setDisable(false);
            cbSexo.setDisable(false);
            txtNome.setDisable(false);
            txtObservacao.setDisable(false);
            btnAdicionaFoto.setDisable(true);

            btnCadastrar.setText("SALVAR");
            //Um novo Servidor
        } else if (btnCadastrar.getText().equals("CADASTRAR")) {
            if (verificaDados()) {
                novoServidor = new Servidor();

                novoServidor.setMatricula(txtMatricula.getText());
                novoServidor.setInstituicao(cbInstituicao.getSelectionModel().getSelectedItem());
                novoServidor.setGrauHierarquico(txtGrauHierarquico.getText());
                novoServidor.setSexo(cbSexo.getSelectionModel().getSelectedItem());
                novoServidor.setNome(txtNome.getText());
                novoServidor.setObservacao(txtObservacao.getText());

                novoServidor.setStatus(true);

                daoServidor.salvar(novoServidor);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Servidor cadastrado com sucesso!");
                alert.showAndWait();
                carregaDados();
                limparDados();

                root.getSelectionModel().select(tabListagem);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }

            //Modificando Servidor
        } else if (btnCadastrar.getText().equals("SALVAR")) {
            if (verificaDados()) {
                servidorSelecionado.setMatricula(txtMatricula.getText());
                servidorSelecionado.setInstituicao(cbInstituicao.getSelectionModel().getSelectedItem());
                servidorSelecionado.setGrauHierarquico(txtGrauHierarquico.getText());
                servidorSelecionado.setSexo(cbSexo.getSelectionModel().getSelectedItem());
                servidorSelecionado.setNome(txtNome.getText());
                servidorSelecionado.setObservacao(txtObservacao.getText());

                servidorSelecionado.setStatus(true);

                daoServidor.alterar(servidorSelecionado);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Servidor modificado com sucesso!");
                alert.showAndWait();
                carregaDados();
                limparDados();

                root.getSelectionModel().select(tabListagem);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void clickedExcluirServidor(MouseEvent event) {
        if (servidorSelecionado != null) {
            Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
            alertAntesExcluir.setTitle("Atenção!");
            alertAntesExcluir.setHeaderText("Os dados referentes à este servidor serão perdidos, deseja continuar?");
            alertAntesExcluir.getButtonTypes().clear();
            alertAntesExcluir.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

            //Deactivate Defaultbehavior for yes-Button:
            Button yesButton = (Button) alertAntesExcluir.getDialogPane().lookupButton(ButtonType.YES);
            yesButton.setDefaultButton(false);

            //Activate Defaultbehavior for no-Button:
            Button noButton = (Button) alertAntesExcluir.getDialogPane().lookupButton(ButtonType.NO);
            noButton.setDefaultButton(true);

            //Pega qual opção o usuario pressionou
            final Optional<ButtonType> resultado = alertAntesExcluir.showAndWait();

            if (resultado.get() == ButtonType.YES) {
                servidorSelecionado.setStatus(false);
                daoServidor.alterar(servidorSelecionado);

                servidorSelecionado = null;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Servidor excluído com sucesso!");
                alert.showAndWait();
                carregaDados();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("É necessário selecionar, o Servidor!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedVoltar() {
        if (btnVoltar.getText().equalsIgnoreCase("VOLTAR")) {
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
        } else if (btnVoltar.getText().equalsIgnoreCase("NOVO")) {
            carregaDados();
            limparDados();
            txtMatricula.setDisable(false);
            cbInstituicao.setDisable(false);
            txtGrauHierarquico.setDisable(false);
            cbSexo.setDisable(false);
            txtNome.setDisable(false);
            txtObservacao.setDisable(false);
            btnAdicionaFoto.setDisable(true);

            btnCadastrar.setText("CADASTRAR");
            btnVoltar.setText("VOLTAR");
            btnExcluir.setDisable(true);
        }
    }

    @FXML
    private void clickedVoltar2(MouseEvent event) {
        root.getScene().getWindow().hide();
    }

    private boolean verificaDados() {
        if (txtMatricula.getText().isEmpty()) {
            tituloMensagem = "Erro Matricula";
            corpoMensagem = "A matricula é obrigatória!";
            return false;
        }

        if (cbInstituicao.getSelectionModel().getSelectedItem() == null) {
            tituloMensagem = "Erro Instituição";
            corpoMensagem = "É obrigatório a seleção de uma instituição!";
            return false;
        }

        if (txtGrauHierarquico.getText().isEmpty()) {
            tituloMensagem = "Erro Nome de Guerra";
            corpoMensagem = "O Nome de Guerra é obrigatório!";
            return false;
        }

        if (cbSexo.getSelectionModel().getSelectedItem() == null) {
            tituloMensagem = "Erro Sexo";
            corpoMensagem = "É obrigatório a seleção de um sexo!";
            return false;
        }

        if (txtNome.getText().isEmpty()) {
            tituloMensagem = "Erro Nome Completo";
            corpoMensagem = "O Nome completo é obrigatório!";
            return false;
        }

        if (btnCadastrar.getText().equalsIgnoreCase("CADASTRAR")) {
            if (daoServidor.buscaPorMatricula(txtMatricula.getText()) != null && !editar) {
                tituloMensagem = "Erro Matricula";
                corpoMensagem = "Matricula inválida, por já está em uso!";
                return false;
            }
        }

        if (btnCadastrar.getText().equalsIgnoreCase("SALVAR")) {
            if (daoServidor.buscaPorObjeto(servidorSelecionado) != null && !editar) {
                tituloMensagem = "Erro ao salvar";
                corpoMensagem = "Já existe um servidor com estes dados!\n"
                        + "Verifique e tente novamente";
                return false;
            }
        }

        return true;
    }

    @FXML
    private void clickedBtnVerDetalhesServidor() throws IOException {
        servidorSelecionado = (Servidor) tableListaServidores.getSelectionModel().getSelectedItem();

        if (servidorSelecionado != null) {
            //Desabilita os campos
            txtMatricula.setDisable(true);
            cbInstituicao.setDisable(true);
            txtGrauHierarquico.setDisable(true);
            cbSexo.setDisable(true);
            txtNome.setDisable(true);
            txtObservacao.setDisable(true);
            btnAdicionaFoto.setDisable(true);

            //Coloca os valores nos campos
            txtMatricula.setText(servidorSelecionado.getMatricula());
            cbInstituicao.setValue(servidorSelecionado.getInstituicao());
            txtGrauHierarquico.setText(servidorSelecionado.getGrauHierarquico());
            cbSexo.setValue(servidorSelecionado.getSexo());
            txtNome.setText(servidorSelecionado.getNome());
            txtObservacao.setText(servidorSelecionado.getObservacao());

            //Converte a imagem
            btnCadastrar.setText("EDITAR");
            btnVoltar.setText("NOVO");
            btnExcluir.setDisable(false);

            root.getSelectionModel().select(tabCadastro);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("É necessário selecionar, o Servidor!");
            alert.showAndWait();
        }
    }

    private void limparDados() {
        txtMatricula.setText("");
        cbInstituicao.setValue(null);
        txtGrauHierarquico.setText("");
        cbSexo.setValue(null);
        txtNome.setText("");
        txtObservacao.setText("");
        btnAdicionaFoto.setDisable(true);
    }

    @FXML
    private void exitCbInstituicaoBusca() {
        cbInstituicaoBusca.setValue(null);
    }
}
