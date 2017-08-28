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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
public class TelaCadastroRelatorioMesaController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ComboBox<Mesa> cbMesa;
    @FXML
    private DatePicker dataInicial;
    @FXML
    private LocalTimeTextField horaInicial;
    @FXML
    private DatePicker dataFinal;
    @FXML
    private LocalTimeTextField horaFinal;
    @FXML
    private TextField txtNomeServidor;
    @FXML
    private ComboBox<Funcao> cbFuncao;
    @FXML
    private LocalTimeTextField horaInicialPlantao;
    @FXML
    private LocalTimeTextField horaFinalPlantao;
    @FXML
    private LocalTimeTextField horaPausa1;
    @FXML
    private LocalTimeTextField horaPausa2;
    @FXML
    private Button btnAdicionarServidor;
    @FXML
    private TableView<ServidorFuncao> tableServidorMesa;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnNomeServidor;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnFuncaoServidor;
    @FXML
    private TableColumn<ServidorFuncao, Boolean> tbColumnAcaoServidor;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnVoltar;

    private Stage dialogStage;
    private String mensagemErroTela = "";
    private String mensagemErroCorpo = "";
    private ServidorFuncao servidorEditar;

    //DAO
    private final MesaDAO daoMesa = new MesaDAO();
    private final FuncaoDAO daoFuncao = new FuncaoDAO();
    private final ServidorDAO daoServidor = new ServidorDAO();
    private final RelatorioDiarioMesasDAO daoRelatorioDiarioMesas = new RelatorioDiarioMesasDAO();
    private List<Mesa> listaDeMesas = new ArrayList<>();
    private List<Funcao> listaDeFuncao = new ArrayList<>();
    private Set<ServidorFuncao> listaDeServidores = new HashSet<>();

    private Servidor servidor = new Servidor();
    private Funcao funcao = new Funcao();
    private RelatorioDiarioMesas relatorio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregaDadosIniciais();

        cbFuncao.setDisable(true);
    }

    private void carregaDadosIniciais() {
        // CarregaFuncionários
        listaDeMesas.addAll(daoMesa.getList("FROM Mesa"));
        listaDeFuncao.addAll(daoFuncao.getList("FROM Funcao"));

        if (verificaDadosIniciais(listaDeMesas, listaDeFuncao)) {
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
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(mensagemErroTela);
            alert.setHeaderText(mensagemErroCorpo);
            alert.showAndWait();
            root.getScene().getWindow().hide();
        }
    }

    private void carregaDadosTableServidoresMesa(Set<ServidorFuncao> dados) {
        if (!dados.isEmpty()) {
            tbColumnNomeServidor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServidorFuncao, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ServidorFuncao, String> data) {
                    return new SimpleStringProperty(data.getValue().getServidor().getNome());
                }
            });

            tbColumnFuncaoServidor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServidorFuncao, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ServidorFuncao, String> data) {
                    return new SimpleStringProperty(data.getValue().getFuncao().getNome());
                }
            });

            tbColumnAcaoServidor.setSortable(false);

            tbColumnAcaoServidor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServidorFuncao, Boolean>, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<ServidorFuncao, Boolean> data) {
                    return new SimpleBooleanProperty(data.getValue() != null);
                }
            });

            tbColumnAcaoServidor.setCellFactory(new Callback<TableColumn<ServidorFuncao, Boolean>, TableCell<ServidorFuncao, Boolean>>() {
                @Override
                public TableCell<ServidorFuncao, Boolean> call(TableColumn<ServidorFuncao, Boolean> data) {
                    return new ButtonCellServidorMesa(tableServidorMesa);
                }
            });

            tableServidorMesa.getItems().addAll(FXCollections.observableSet(dados));
            tableServidorMesa.setDisable(false);
        } else {
            tableServidorMesa.setDisable(true);
        }
    }

    @FXML
    private void clickedCbMesa() {
        if (cbMesa.getSelectionModel().getSelectedItem() != null) {
            btnSalvar.setDisable(false);
        } else {
            btnSalvar.setDisable(true);
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

    private void clickedBuscarServidor(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                clickedBuscarServidor();
            }
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

    @FXML
    private void clickedBtnAdicionar(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            clickedBtnAdicionar();
        }
    }

    @FXML
    private void clickedBtnAdicionar() {

        if (btnAdicionarServidor.getText().equalsIgnoreCase("novo")) {
            cbFuncao.setValue(null);
            cbFuncao.setDisable(true);
            horaInicialPlantao.setLocalTime(null);
            horaFinalPlantao.setLocalTime(null);
            horaPausa1.setLocalTime(null);
            horaPausa2.setLocalTime(null);
            txtNomeServidor.clear();
            btnAdicionarServidor.setText("Adicionar");
            servidorEditar = null;
            servidor = null;
            funcao = null;
        } else {
            if ((servidor != null && funcao != null) && verificaHora()) {
                if (btnAdicionarServidor.getText().equalsIgnoreCase("salvar")) {
                    servidorEditar.setAtivo(true);
                    servidorEditar.setFuncao(cbFuncao.getValue());
                    servidorEditar.setHoraInicialPlantao(horaInicialPlantao.getLocalTime());
                    servidorEditar.setHoraFinalPlantao(horaFinalPlantao.getLocalTime());
                    servidorEditar.setHoraPausa1(horaPausa1.getLocalTime());
                    servidorEditar.setHoraPausa2(horaPausa2.getLocalTime());
                    
                    //Carrega os dados na tabela Servidores
                    carregaDadosTableServidoresMesa(listaDeServidores);
                    
                    //Desabilitar e Limpa dados para um novo ServidorFunção
                    cbFuncao.setValue(null);
                    cbFuncao.setDisable(true);
                    txtNomeServidor.clear();
                    horaInicialPlantao.setLocalTime(null);
                    horaFinalPlantao.setLocalTime(null);
                    horaPausa1.setLocalTime(null);
                    horaPausa2.setLocalTime(null);
                    servidorEditar = null;
                    servidor = null;
                    funcao = null;

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Servidor Modificado");
                    alert.setHeaderText("Servidor Modificado!");
                    alert.showAndWait();
                    
                    
                } else if (btnAdicionarServidor.getText().equalsIgnoreCase("Adicionar")){
                    listaDeServidores.add(new ServidorFuncao(servidor, funcao, horaInicialPlantao.getLocalTime(),
                            horaFinalPlantao.getLocalTime(), horaPausa1.getLocalTime(), horaPausa2.getLocalTime(), true));

                    //Carrega os dados na tabela Servidores
                    carregaDadosTableServidoresMesa(listaDeServidores);

                    //Desabilitar e Limpa dados para um novo ServidorFunção
                    cbFuncao.setValue(null);
                    cbFuncao.setDisable(true);
                    txtNomeServidor.clear();
                    horaInicialPlantao.setLocalTime(null);
                    horaFinalPlantao.setLocalTime(null);
                    horaPausa1.setLocalTime(null);
                    horaPausa2.setLocalTime(null);
                    servidorEditar = null;
                    servidor = null;
                    funcao = null;

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Servidor Adicionado");
                    alert.setHeaderText("Servidor adicionado!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(mensagemErroTela);
                alert.setHeaderText(mensagemErroCorpo);
                alert.showAndWait();
            }
        }
    }

    private boolean verificaHora() {
        if (horaInicialPlantao.getLocalTime() == null) {
            //Pop-up informando o cadastro
            mensagemErroTela = "Erro Hora Incial Plantão";
            mensagemErroCorpo = "É necessário Informar o horario de entrada deste servidor!";
            return false;
        }

        if (horaFinalPlantao.getLocalTime() == null) {
            //Pop-up informando o cadastro
            mensagemErroTela = "Erro Hora Final Plantão";
            mensagemErroCorpo = "É necessário Informar o horario de saída do plantão deste servidor!";
            return false;
        }

        if (horaPausa1.getLocalTime() == null && horaPausa2.getLocalTime() != null) {
            //Pop-up informando o cadastro
            mensagemErroTela = "Erro Hora Pausa 2";
            mensagemErroCorpo = "Não é possivel cadastrar pausa2 sem hora da pausa1!";
            return false;
        }

        //É antes
        if ((horaFinalPlantao.getLocalTime() != null && horaInicialPlantao.getLocalTime()!= null) && horaFinalPlantao.getLocalTime().isBefore(horaInicialPlantao.getLocalTime())) {
            if (dataFinal.getValue().equals(dataInicial.getValue())) {
                mensagemErroTela = "Data Inválida";
                mensagemErroCorpo = "Corrija o campo Hora Plantão Inicial!\nEsta não poderá anterior a Hora de saída!";
                return false;
            }
        }

        if ((horaPausa2.getLocalTime() != null && horaPausa1.getLocalTime() != null) && horaPausa2.getLocalTime().isBefore(horaPausa1.getLocalTime())) {
            if (dataFinal.getValue().equals(dataInicial.getValue())) {
                mensagemErroTela = "Data Inválida";
                mensagemErroCorpo = "Corrija o campo Hora da Pausa2!\nEsta não poderá anterior a Hora da Pausa2!";
                return false;
            }
        }
        return true;
    }

    @FXML
    private void clickedBtnSalvar(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            clickedBtnSalvar();
        }
    }

    @FXML
    private void clickedBtnSalvar(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                clickedBtnSalvar();
            }
        }
    }

    @FXML
    private void clickedBtnSalvar() {
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

    @FXML
    private void clickedBtnVoltar(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            clickedBtnSalvar();
        }
    }

    @FXML
    private void clickedBtnVoltar(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                clickedBtnVoltar();
            }
        }
    }

    @FXML
    private void clickedBtnVoltar() {
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

    //    Define os botões de ação da MESA
    private class ButtonCellServidorMesa extends TableCell<ServidorFuncao, Boolean> {

        //BOTAO REMOVER
        private Button botaoRemover = new Button();
        private final ImageView imagemRemover = new ImageView(new Image(getClass().getResourceAsStream("/icons/remover.png")));

        //BOTAO EDITAR
        private Button botaoEditar = new Button();
        private final ImageView imagemEditar = new ImageView(new Image(getClass().getResourceAsStream("/icons/editar.png")));

        //BOTAO EDITAR
        private Button botaoVisualizar = new Button();
        private final ImageView imagemVisualizar = new ImageView(new Image(getClass().getResourceAsStream("/icons/visualizar.png")));

        private ButtonCellServidorMesa(TableView<ServidorFuncao> tblView) {
            //BOTAO VISUALIZAR
            imagemVisualizar.fitHeightProperty().set(16);
            imagemVisualizar.fitWidthProperty().set(16);
            botaoVisualizar.setGraphic(imagemVisualizar);
            botaoVisualizar.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    ServidorFuncao servidorVisualizar = (ServidorFuncao) tblView.getItems().get(getIndex());
                    if (servidorVisualizar != null) {
                        txtNomeServidor.setText(servidorVisualizar.getServidor().getNome());
                        cbFuncao.setValue(servidorVisualizar.getFuncao());
                        horaInicialPlantao.setLocalTime(servidorVisualizar.getHoraInicialPlantao());
                        horaFinalPlantao.setLocalTime(servidorVisualizar.getHoraFinalPlantao());
                        horaPausa1.setLocalTime(servidorVisualizar.getHoraPausa1());
                        horaPausa2.setLocalTime(servidorVisualizar.getHoraPausa2());

                        //Desabilita Tudo
                        txtNomeServidor.setDisable(true);
                        cbFuncao.setDisable(true);
                        horaInicialPlantao.setDisable(true);
                        horaFinalPlantao.setDisable(true);
                        horaPausa1.setDisable(true);
                        horaPausa2.setDisable(true);

                        btnAdicionarServidor.setText("Novo");
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
                    servidorEditar = (ServidorFuncao) tblView.getItems().get(getIndex());

                    if (servidorEditar != null) {
                        txtNomeServidor.setText(servidorEditar.getServidor().getNome());
                        txtNomeServidor.setDisable(true);
                        cbFuncao.setValue(servidorEditar.getFuncao());
                        horaInicialPlantao.setLocalTime(servidorEditar.getHoraInicialPlantao());
                        horaFinalPlantao.setLocalTime(servidorEditar.getHoraFinalPlantao());
                        horaPausa1.setLocalTime(servidorEditar.getHoraPausa1());
                        horaPausa2.setLocalTime(servidorEditar.getHoraPausa2());

                        btnAdicionarServidor.setText("Salvar");
                    }
                }
            });

            //BOTAO REMOVER
            imagemRemover.fitHeightProperty().set(16);
            imagemRemover.fitWidthProperty().set(16);

            botaoRemover.setGraphic(imagemRemover);

            botaoRemover.setOnAction((ActionEvent t) -> {
                ServidorFuncao servidorRemover = (ServidorFuncao) tblView.getItems().get(getIndex());

                if (servidorRemover != null) {
                    Alert alertVoltar = new Alert(Alert.AlertType.CONFIRMATION);
                    alertVoltar.setTitle("Atenção!");
                    alertVoltar.setHeaderText("Os dados referentes à este Servidor serão perdidos, deseja continuar?");
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
                        servidorRemover.setAtivo(false);
                        listaDeServidores.remove(servidorRemover);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sucesso!");
                        alert.setHeaderText("Servidor removido com sucesso!");
                        alert.showAndWait();
                        carregaDadosTableServidoresMesa(listaDeServidores);
                    }
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(botaoEditar);
                setGraphic(botaoRemover);
                setAlignment(Pos.CENTER);
            } else {
                setGraphic(null);
            }
        }
    }

}
