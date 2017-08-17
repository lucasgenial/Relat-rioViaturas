package com.cicom.relatorioviaturas.controllers;

import com.cicom.relatorioviaturas.DAO.FuncaoDAO;
import com.cicom.relatorioviaturas.DAO.MesaDAO;
import com.cicom.relatorioviaturas.DAO.RelatorioDiarioMesasDAO;
import com.cicom.relatorioviaturas.DAO.ServidorDAO;
import com.cicom.relatorioviaturas.model.Funcao;
import com.cicom.relatorioviaturas.model.Mesa;
import com.cicom.relatorioviaturas.model.RelatorioDiarioMesas;
import com.cicom.relatorioviaturas.model.Servidor;
import com.cicom.relatorioviaturas.model.ServidorFuncao;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.scene.control.LocalTimeTextField;

/**
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class TelaCadastroMesaController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ComboBox<Mesa> cbMesa;
    @FXML
    private DatePicker dataInicial;
    @FXML
    private DatePicker dataFinal;
    @FXML
    private TextField txtNomeServidor;
    @FXML
    private LocalTimeTextField horaInicial;
    @FXML
    private LocalTimeTextField horaFinal;
    @FXML
    private ComboBox<Funcao> cbFuncao;
    @FXML
    private Button btnAdicionarServidor;
    @FXML
    private Button btnRemoverServidor;
    @FXML
    private TableView<ServidorFuncao> tableServidorMesa;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnNomeServidor;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnFuncaoServidor;
    @FXML
    private Button btnSalvarMesa;
    @FXML
    private Button btnCancelar;

    private Stage dialogStage;
    private String mensagemErroTela = null;
    private String mensagemErroCorpo = null;

    //DAO
    private final MesaDAO daoMesa = new MesaDAO();
    private final FuncaoDAO daoFuncao = new FuncaoDAO();
    private final ServidorDAO daoServidor = new ServidorDAO();
    private final RelatorioDiarioMesasDAO daoRelatorioDiarioMesas = new RelatorioDiarioMesasDAO();
    private List<Mesa> listaDeMesas;
    private List<Funcao> listaDeFuncao;
    private Set<ServidorFuncao> listaDeServidores = new HashSet<>();

    private Servidor servidor = new Servidor();
    private Funcao funcao = new Funcao();
    private RelatorioDiarioMesas relatorio;

    public TelaCadastroMesaController() {
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public RelatorioDiarioMesas getRelatorio() {
        return this.relatorio;
    }

    public void setRelatorio(RelatorioDiarioMesas value) {
        this.relatorio = value;

        //Carrega os dados para os campos
        this.cbMesa.setValue(value.getMesa());
        this.dataInicial.setValue(value.getDataInicial());
        this.dataFinal.setValue(value.getDataFinal());
        this.horaInicial.setLocalTime(value.getHoraInicial());
        this.horaFinal.setLocalTime(value.getHoraFinal());

        listaDeServidores = value.getServidores();
        this.carregaDadosTableServidoresMesa(listaDeServidores);

        //Desabilita os itens que não poderão ser editados
        cbMesa.setDisable(false);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregaDadosIniciais();

        cbFuncao.setDisable(true);
        btnAdicionarServidor.setDisable(true);
        btnRemoverServidor.setDisable(true);
    }

    private void carregaDadosIniciais() {
        // CarregaFuncionários
        listaDeMesas = (ArrayList<Mesa>) daoMesa.getList("FROM Mesa");
        listaDeFuncao = (ArrayList<Funcao>) daoFuncao.getList("FROM Funcao");

        if (!verificaDadosIniciais(listaDeMesas, listaDeFuncao)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(mensagemErroTela);
            alert.setHeaderText(mensagemErroCorpo);
            alert.showAndWait();
            root.getScene().getWindow().hide();
        } else {

            if (relatorio == null) {
                //Assume que o plantão inicia-se no dia do lançamento
                dataInicial.setValue(LocalDate.now());
                dataInicial.setDisable(true);
            } else {
                btnRemoverServidor.setDisable(false);
            }

            //Converte as opções para o modo String
            cbMesa.setConverter(new StringConverter<Mesa>() {
                @Override
                public String toString(Mesa item) {
                    if (item != null) {
                        return item.getNome();
                    }
                    return "";
                }

                @Override
                public Mesa fromString(String string) {
                    return daoMesa.buscaPorNome(string);
                }

            });
            cbMesa.setItems(FXCollections.observableList(listaDeMesas));

            //Converte as opções para o modo String
            cbFuncao.setConverter(new StringConverter<Funcao>() {
                @Override
                public String toString(Funcao item) {
                    if (item != null) {
                        return item.getNome() + " - " + item.getNome();
                    }
                    return "";
                }

                @Override
                public Funcao fromString(String string) {
                    return daoFuncao.buscaPorNome(string);
                }
            });

            //Lança todos os servidores cadastrados para a escolha do usuário do Supervisor
            cbFuncao.setItems(FXCollections.observableList(listaDeFuncao));
        }
    }

    private boolean verificaDadosIniciais(List<Mesa> listaDeMesas, List<Funcao> listaDeFuncao) {
        //Verifica Se Há Mesas Disponiveis
        if (listaDeMesas.isEmpty()) {
            //Pop-up informando o cadastro
            mensagemErroTela = "Erro Mesas";
            mensagemErroCorpo = "Possivelmente não há mesas cadastradas/diponiveis no sitema!";
            return false;
        }

        //Verifica Se Há Supervisor Disponiveis
        if (listaDeFuncao.isEmpty()) {
            //Pop-up informando o cadastro
            mensagemErroTela = "Erro Funcao";
            mensagemErroCorpo = "Possivelmente não há funções cadastradas/diponiveis no sitema!";
            return false;
        }

        return true;
    }

    private void carregaDadosTableServidoresMesa(Set<ServidorFuncao> dados) {

        if (!dados.isEmpty()) {
            btnRemoverServidor.setDisable(false);
            tableServidorMesa.setDisable(false);
            tbColumnNomeServidor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServidorFuncao, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ServidorFuncao, String> data) {
                    return data.getValue().getServidor().nomeProperty();
                }
            });
            tbColumnFuncaoServidor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServidorFuncao, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ServidorFuncao, String> data) {
                    return data.getValue().getFuncao().nomeProperty();
                }
            });
            tableServidorMesa.getItems().setAll(dados);
        } else {
            tableServidorMesa.setDisable(true);
            btnRemoverServidor.setDisable(true);
        }
    }

    @FXML
    private void clickedCbMesa() {
        if (cbMesa.getSelectionModel().getSelectedItem() != null) {
            btnSalvarMesa.setDisable(false);
        } else {
            btnSalvarMesa.setDisable(true);
        }
    }

    @FXML
    private void clickedCbFuncao() {
        funcao = cbFuncao.getSelectionModel().getSelectedItem();

        if (funcao != null && servidor != null) {
            btnAdicionarServidor.setDisable(false);
        } else {
            btnAdicionarServidor.setDisable(true);
        }
    }

    @FXML
    private void clickedBuscarServidor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/TelaListarServidores.fxml"));
            Parent page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Lista de Servidores Cadastrados");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaListarServidoresController controller = loader.getController();
            controller.setServidor(servidor);
            controller.setDialogStage(dialogStageAtual);

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();

            //Recebe o servidor selecionado???
            servidor = controller.getServidor();

            if (servidor != null) {
                txtNomeServidor.setText(servidor.getNome());
                cbFuncao.setDisable(false);
            } else {
                cbFuncao.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nenhum servidor foi selecionado");
                alert.showAndWait();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedAdicionarServidor() {
        if (servidor != null && funcao != null) {
            listaDeServidores.add(new ServidorFuncao(servidor, funcao));

            //Carrega os dados na tabela Servidores
            carregaDadosTableServidoresMesa(listaDeServidores);

            //Desabilitar e Limpa dados para um novo ServidorFunção
            cbFuncao.setValue(null);
            cbFuncao.setDisable(true);
            txtNomeServidor.clear();
            servidor = null;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Servidor Adicionado");
            alert.setHeaderText("Servidor adicionado!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao adicionar");
            alert.setHeaderText("Servidor não pode ser adicionado!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedRemoverServidor(ActionEvent event) {
        ServidorFuncao servidorParaExcluir = tableServidorMesa.getSelectionModel().getSelectedItem();

        if (!tableServidorMesa.getItems().isEmpty() && servidorParaExcluir != null) {

            listaDeServidores.remove(servidorParaExcluir);

            //Carrega os dados na tabela Servidores
            carregaDadosTableServidoresMesa(listaDeServidores);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário clicar no Servidor abaixo!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedSalvar(ActionEvent event) {
        if (verificaDados()) {
            if (relatorio == null) {
                //Cadastra Nova Mesa
                relatorio = new RelatorioDiarioMesas();

                relatorio.setMesa(cbMesa.getValue());
                relatorio.setDataInicial(dataInicial.getValue());
                relatorio.setHoraInicial(horaInicial.getLocalTime());
                relatorio.setDataFinal(dataFinal.getValue());
                relatorio.setHoraFinal(horaFinal.getLocalTime());
                relatorio.setServidores(listaDeServidores);

                //Cadastra o resumo no banco
                daoRelatorioDiarioMesas.salvar(relatorio);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Mesa cadastrada com Sucesso");
                alert.showAndWait();
                root.getScene().getWindow().hide();
            } else {
                //Edita a mesa
                //relatorio.setMesa(cbMesa.getValue());
                relatorio.setDataInicial(dataInicial.getValue());
                relatorio.setHoraInicial(horaInicial.getLocalTime());
                relatorio.setDataFinal(dataFinal.getValue());
                relatorio.setHoraFinal(horaFinal.getLocalTime());
                relatorio.setServidores(listaDeServidores);

                //Persiste no banco
                daoRelatorioDiarioMesas.alterar(relatorio);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Mesa editada com Sucesso");
                alert.showAndWait();
                root.getScene().getWindow().hide();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(mensagemErroTela);
            alert.setHeaderText(mensagemErroCorpo);
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedCancelar(ActionEvent event) {
        Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
        alertAntesExcluir.setTitle("Atenção!");
        alertAntesExcluir.setHeaderText("Os dados informados serão perdidos, deseja continuar?");
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
            //Para as duas situações é necessário que a viatura assuma o valor nulo para
            //Cancelar alterações e/ou salvamento
            servidor = null;
            root.getScene().getWindow().hide();
        }
    }

    private boolean verificaDados() {
        //Data final é depois da data inicial
        if (cbMesa.getSelectionModel().getSelectedItem() == null) {
            mensagemErroTela = "Mesa Inválida";
            mensagemErroCorpo = "Corrija o campo Mesa!\nÉ necessário selecionar uma mesa";
            return false;
        }
        
        if (dataFinal.getValue().isBefore(dataInicial.getValue())) {
            mensagemErroTela = "Data Inválida";
            mensagemErroCorpo = "Corrija o campo Data Inicial!\nEsta não poderá anterior a data Inicial";
            return false;
        }

        if (dataFinal.getValue().isEqual(dataInicial.getValue()) && horaFinal.getLocalTime().isBefore(horaInicial.getLocalTime())) {
            mensagemErroTela = "Hora Final Inválida";
            mensagemErroCorpo = "Corrija o campo Hora Final!\nNão poderá ser anterior a Hora Inicial";
            return false;
        }

        if (dataFinal.getValue().isEqual(dataInicial.getValue()) && horaInicial.getLocalTime().isAfter(horaFinal.getLocalTime())) {
            mensagemErroTela = "Hora Inicial Inválida";
            mensagemErroCorpo = "Corrija o campo Hora Inicial!\nNão poderá ser posterior a Hora Final";
            return false;
        }

        if (listaDeServidores.size() < 2) {
            mensagemErroTela = "Erro ao salvar";
            mensagemErroCorpo = "É necessário que a mesa possua, pelo menos 2 servidores!\n"
                    + "1 - Operador\n"
                    + "1 - Supervisor";

            return false;
        } else {
            boolean retornoOperador = false;
            boolean retornoSupervisor = false;
            for (ServidorFuncao ser : listaDeServidores) {
                System.out.println(ser);
                if (ser.getFuncao().getNome().equalsIgnoreCase("Operador")) {
                    retornoOperador = true;
                }

                if (ser.getFuncao().getNome().equalsIgnoreCase("Supervisor")) {
                    retornoSupervisor = true;
                }
            }

            //Verifica se há um operador cadastrado
            if (!retornoOperador) {
                mensagemErroTela = "Erro ao salvar";
                mensagemErroCorpo = "É necessário que a mesa possua, pelo menos 1 operador!";
                return false;
            }

            //Verifica se há um supervisor cadastrado
            if (!retornoSupervisor) {
                mensagemErroTela = "Erro ao salvar";
                mensagemErroCorpo = "É necessário que a mesa possua, pelo menos 1 supervisor!";
                return false;
            }
        }
        return true;
    }
}
