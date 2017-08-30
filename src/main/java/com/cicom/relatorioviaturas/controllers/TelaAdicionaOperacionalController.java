package com.cicom.relatorioviaturas.controllers;

import com.cicom.relatorioviaturas.DAO.FuncaoDAO;
import com.cicom.relatorioviaturas.DAO.PoDAO;
import com.cicom.relatorioviaturas.DAO.RelatorioDiarioMesasDAO;
import com.cicom.relatorioviaturas.DAO.RelatorioDiarioViaturasDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.DAO.ViaturaDAO;
import com.cicom.relatorioviaturas.model.Funcao;
import com.cicom.relatorioviaturas.model.PO;
import com.cicom.relatorioviaturas.model.RelatorioDiarioMesas;
import com.cicom.relatorioviaturas.model.RelatorioDiarioViaturas;
import com.cicom.relatorioviaturas.model.Servidor;
import com.cicom.relatorioviaturas.model.ServidorFuncao;
import com.cicom.relatorioviaturas.model.Unidade;
import com.cicom.relatorioviaturas.model.Viatura;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
public class TelaAdicionaOperacionalController implements Initializable {

    @FXML
    private TabPane root;
    @FXML
    private ComboBox<PO> cbTipoPO = new ComboBox<>();
    @FXML
    private ComboBox<Unidade> cbUnidade = new ComboBox<>();
    @FXML
    private TextField txtPrefixo;
    @FXML
    private ToggleButton tbBCS;
    @FXML
    private ToggleButton tbGPS;
    @FXML
    private ToggleButton tbEstadoGPS;
    @FXML
    private ToggleButton tbHT;
    @FXML
    private ToggleButton tbAudio;
    @FXML
    private ToggleButton tbCamera;
    @FXML
    private ToggleButton tbEstadoCam;
    @FXML
    private TableView<ServidorFuncao> tableServidorGuarnicao;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnNomeServidor;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnFuncaoServidor;
    @FXML
    private TableColumn<ServidorFuncao, Boolean> tbColumnAcaoServidor;
    @FXML
    private ComboBox<Funcao> cbFuncao = new ComboBox<>();
    @FXML
    private TextField txtNomeServidor;
    @FXML
    private Tab tabDadosViatura;
    @FXML
    private Button btnAvancar;
    @FXML
    private TextField txtTomboHT;
    @FXML
    private LocalTimeTextField horaBaixaViatura;
    @FXML
    private ToggleButton tbViaturaAtiva;
    @FXML
    private LocalTimeTextField horaInicialPlantaoViatura;
    @FXML
    private LocalTimeTextField horaFinalPlantaoViatura;
    @FXML
    private LocalTimeTextField horaPausa1Viatura;
    @FXML
    private LocalTimeTextField horaPausa2Viatura;
    @FXML
    private Tab tabDadosGuarnicao;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnAdicionarServidor;

    @FXML
    private LocalTimeTextField horaInicialPlantaoServidor;
    @FXML
    private LocalTimeTextField horaFinalPlantaoServidor;
    @FXML
    private LocalTimeTextField horaPausa1Servidor;
    @FXML
    private LocalTimeTextField horaPausa2Servidor;
    @FXML
    private CheckBox chcMesmoHorario = new CheckBox();

    private final PoDAO daoPO = new PoDAO();
    private final UnidadeDAO daoUnidade = new UnidadeDAO();
    private final FuncaoDAO daoFuncao = new FuncaoDAO();
    private final RelatorioDiarioMesasDAO daoRelatorioMesa = new RelatorioDiarioMesasDAO();
    private final RelatorioDiarioViaturasDAO daoRelatorioViatura = new RelatorioDiarioViaturasDAO();

    private Viatura viatura;
    private Servidor servidor = new Servidor();
    private Funcao funcao = new Funcao();
    private ServidorFuncao servidorFuncao = new ServidorFuncao();
    private Set<ServidorFuncao> listaDeServidores = new HashSet<>();
    private List<Funcao> listaFuncao = new ArrayList<>();
    private List<Unidade> listaUnidade = new ArrayList<>();
    private List<PO> listaPOS = new ArrayList<>();
    private PO tipoPO;
    private String tituloMensagem = "";
    private String corpoMensagem = "";
    private RelatorioDiarioMesas relatorioDeMesa;
    private RelatorioDiarioViaturas relatorioDeViatura;
    private Stage stage;
    private ViaturaDAO daoViatura = new ViaturaDAO();
    private boolean botaoSalvarClicado = false;
    private ServidorFuncao servidorEditar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new CarregaDados().start();
    }

    void setStage(Stage value) {
        stage = value;
    }

    class CarregaDados extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (viatura != null) {
                            tabDadosGuarnicao.setDisable(false);
                            txtNomeServidor.setDisable(false);
                            cbTipoPO.setDisable(false);
                            tableServidorGuarnicao.setDisable(false);
                            btnAvancar.setDisable(true);
                            tbViaturaAtiva.setDisable(true);
                            horaBaixaViatura.setDisable(true);
                            if (viatura.isVtrBaixada()) {
                                btnSalvar.setDisable(true);
                            } else {
                                btnSalvar.setDisable(false);
                            }
                        }

                        if (!listaPOS.isEmpty()) {
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

                            cbTipoPO.setItems(FXCollections.observableList(listaPOS));

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erro P.O.");
                            alert.setHeaderText("Possivelmente não há P.O. cadastradas/diponiveis na unidade!");
                            alert.showAndWait();
                            root.getScene().getWindow().hide();
                        }

                        //Carrega a lista de Funções
                        listaFuncao.addAll(daoFuncao.getList("FROM Funcao"));

                        if (!listaFuncao.isEmpty()) {
                            cbFuncao.setConverter(new StringConverter<Funcao>() {
                                @Override
                                public String toString(Funcao item) {
                                    if (item != null) {
                                        return item.getNome();
                                    }
                                    return "";
                                }

                                @Override
                                public Funcao fromString(String string) {
                                    return daoFuncao.buscaPorNome(string);
                                }

                            });

                            cbFuncao.setItems(FXCollections.observableList(listaFuncao));

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erro Função");
                            alert.setHeaderText("Possivelmente não há Função cadastradas/diponiveis na unidade!");
                            alert.showAndWait();
                            root.getScene().getWindow().hide();
                        }
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(TelaCadastroUnidadeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void carregaDadosTablelaServidorGuarnicao(Set<ServidorFuncao> dados) {

        if (!dados.isEmpty()) {
            tableServidorGuarnicao.getItems().clear();
            tableServidorGuarnicao.setDisable(false);
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
            tbColumnAcaoServidor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ServidorFuncao, Boolean>, ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<ServidorFuncao, Boolean> data) {
                    return new SimpleBooleanProperty(data.getValue() != null);
                }
            });

            tbColumnAcaoServidor.setCellFactory(new Callback<TableColumn<ServidorFuncao, Boolean>, TableCell<ServidorFuncao, Boolean>>() {
                @Override
                public TableCell<ServidorFuncao, Boolean> call(TableColumn<ServidorFuncao, Boolean> data) {
                    return new ButtonCellServidorGuarnicao(tableServidorGuarnicao);
                }
            });

            tableServidorGuarnicao.getItems().addAll(FXCollections.observableSet(dados));
            tableServidorGuarnicao.setDisable(false);
            btnSalvar.setDisable(false);
        } else {
            tableServidorGuarnicao.getItems().clear();
            btnAdicionarServidor.setText("Adicionar");

            limparDadosServidor();

            btnSalvar.setDisable(true);
            tableServidorGuarnicao.setDisable(true);
        }
    }

    private void limparDadosServidor() {
        txtNomeServidor.setText(null);
        cbFuncao.setValue(null);
        horaInicialPlantaoServidor.setLocalTime(null);
        horaFinalPlantaoServidor.setLocalTime(null);
        horaPausa1Servidor.setLocalTime(null);
        horaPausa2Servidor.setLocalTime(null);

        txtNomeServidor.setDisable(false);
        cbFuncao.setDisable(false);
        horaInicialPlantaoServidor.setDisable(false);
        horaFinalPlantaoServidor.setDisable(false);
        horaPausa1Servidor.setDisable(false);
        horaPausa2Servidor.setDisable(false);
    }

    @FXML
    private void clickedCbTipoPO(ActionEvent event) {
        tipoPO = cbTipoPO.getSelectionModel().getSelectedItem();

        //Retorna ao valor padrão
        tbAudio.setSelected(false);
        tbBCS.setSelected(false);
        tbEstadoGPS.setSelected(false);
        tbGPS.setSelected(false);
        tbCamera.setSelected(false);
        tbEstadoCam.setSelected(false);
        tbHT.setSelected(false);
        tbViaturaAtiva.setSelected(false);

        verificaTipoPO(tipoPO);
    }

    @FXML
    private void clickedBtnAvançar(MouseEvent event) {
        if (verificaDadosPaneDadosPO()) {
            tabDadosGuarnicao.setDisable(false);
            root.getSelectionModel().select(tabDadosGuarnicao);
            txtNomeServidor.setDisable(false);
            clickedChcMesmoHorario();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(tituloMensagem);
            alert.setHeaderText(corpoMensagem);
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedChcMesmoHorario() {
        if (chcMesmoHorario.isSelected()) {
            //Pega os memos horários da viatura
            horaInicialPlantaoServidor.setLocalTime(horaInicialPlantaoViatura.getLocalTime());
            horaFinalPlantaoServidor.setLocalTime(horaFinalPlantaoViatura.getLocalTime());
            horaPausa1Servidor.setLocalTime(horaPausa1Viatura.getLocalTime());
            horaPausa2Servidor.setLocalTime(horaPausa2Viatura.getLocalTime());
            horaInicialPlantaoServidor.setDisable(true);
            horaFinalPlantaoServidor.setDisable(true);
            horaPausa1Servidor.setDisable(true);
            horaPausa2Servidor.setDisable(true);
        } else {
            //Novos horarios para cada servidor
            horaInicialPlantaoServidor.setLocalTime(horaInicialPlantaoViatura.getLocalTime());
            horaFinalPlantaoServidor.setLocalTime(horaFinalPlantaoViatura.getLocalTime());
            horaPausa1Servidor.setLocalTime(horaPausa1Viatura.getLocalTime());
            horaPausa2Servidor.setLocalTime(horaPausa2Viatura.getLocalTime());
            horaInicialPlantaoServidor.setDisable(false);
            horaFinalPlantaoServidor.setDisable(false);
            horaPausa1Servidor.setDisable(false);
            horaPausa2Servidor.setDisable(false);
        }
    }

    private boolean verificaDadosPaneDadosPO() {
        if (horaInicialPlantaoViatura.getLocalTime() == null) {
            tituloMensagem = "Erro Horário Viatura";
            corpoMensagem = "É necessário informar o horário de inicio plantão para o PO!";
            return false;
        }

        if (horaFinalPlantaoViatura.getLocalTime() == null) {
            tituloMensagem = "Erro Horário Viatura";
            corpoMensagem = "É necessário informar o horário de fim do plantão para o PO!";
            return false;
        }
        return true;
    }

    private boolean verificaDados() {
        boolean retorno = false;
        if (tipoPO != null) {

            switch (tipoPO.getNome()) {
                case "VIATURA 2 RODAS":
                    if (txtPrefixo.getText().isEmpty()) {
                        tituloMensagem = "Erro Prefixo";
                        corpoMensagem = "O prefixo é necessário!";
                        break;
                    }

                    if (listaDeServidores.isEmpty() && btnSalvar.isPressed()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    } else if (listaDeServidores.size() > 2 && btnSalvar.isPressed()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é possivel cadastrar apenas 2 servidores!";
                        break;
                    }

                    if (tbHT.isSelected() && txtTomboHT.getText() == null) {
                        tituloMensagem = "Erro Tombo HT";
                        corpoMensagem = "A opção HT foi selecionada, devendo ser fornecido o tombo do mesmo!";
                        break;
                    }

                    if (tbViaturaAtiva.isSelected() && horaBaixaViatura.getLocalTime() == null) {
                        tituloMensagem = "Erro Hora Baixa";
                        corpoMensagem = "A opção de Baixa de viatura foi selecionada\n, devendo ser fornecido a hora da baixa!";
                        break;
                    }

                    if (!verificaHoraViatura()) {
                        break;
                    }

                    retorno = true;
                case "BARCO":
                    if (txtPrefixo.getText().isEmpty()) {
                        tituloMensagem = "Erro Prefixo";
                        corpoMensagem = "O prefixo é necessário!";
                        break;
                    }

                    if (listaDeServidores.isEmpty() && btnSalvar.isPressed()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    } else if (listaDeServidores.size() > 6 && btnSalvar.isPressed()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é possivel cadastrar 6 servidores!";
                        break;
                    }

                    if (tbHT.isSelected() && txtTomboHT.getText() == null) {
                        tituloMensagem = "Erro Tombo HT";
                        corpoMensagem = "A opção HT foi selecionada, devendo ser fornecido o tombo do mesmo!";
                        break;
                    }

                    if (tbViaturaAtiva.isSelected() && horaBaixaViatura.getLocalTime() == null) {
                        tituloMensagem = "Erro Hora Baixa";
                        corpoMensagem = "A opção de Baixa de viatura foi selecionada\n, devendo ser fornecido a hora da baixa!";
                        break;
                    }

                    if (!verificaHoraViatura()) {
                        break;
                    }

                    retorno = true;
                case "A PÉ":
                    if (listaDeServidores.isEmpty()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    }

                    if (tbHT.isSelected() && txtTomboHT.getText() == null) {
                        tituloMensagem = "Erro Tombo HT";
                        corpoMensagem = "A opção HT foi selecionada, devendo ser fornecido o tombo do mesmo!";
                        break;
                    }

                    if (tbViaturaAtiva.isSelected() && horaBaixaViatura.getLocalTime() == null) {
                        tituloMensagem = "Erro Hora Baixa";
                        corpoMensagem = "A opção de Baixa de viatura foi selecionada\n, devendo ser fornecido a hora da baixa!";
                        break;
                    }

                    if (!verificaHoraViatura()) {
                        break;
                    }

                    retorno = true;
                case "MÓDULO":
                    if (listaDeServidores.isEmpty()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    }

                    if (tbHT.isSelected() && txtTomboHT.getText() == null) {
                        tituloMensagem = "Erro Tombo HT";
                        corpoMensagem = "A opção HT foi selecionada, devendo ser fornecido o tombo do mesmo!";
                        break;
                    }

                    if (tbViaturaAtiva.isSelected() && horaBaixaViatura.getLocalTime() == null) {
                        tituloMensagem = "Erro Hora Baixa";
                        corpoMensagem = "A opção de Baixa de viatura foi selecionada\n, devendo ser fornecido a hora da baixa!";
                        break;
                    }

                    if (!verificaHoraViatura()) {
                        break;
                    }

                    retorno = true;
                case "VIATURA 4 RODAS":
                    if (txtPrefixo.getText().isEmpty()) {
                        tituloMensagem = "Erro Prefixo";
                        corpoMensagem = "O prefixo é necessário!";
                        break;
                    }

                    if (listaDeServidores.isEmpty() && btnSalvar.isPressed()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    } else if (listaDeServidores.size() > 4 && btnSalvar.isPressed()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é possivel cadastrar apenas 4 servidores!";
                        break;
                    }

                    if (tbHT.isSelected() && txtTomboHT.getText() == null) {
                        tituloMensagem = "Erro Tombo HT";
                        corpoMensagem = "A opção HT foi selecionada, devendo ser fornecido o tombo do mesmo!";
                        break;
                    }

                    if (tbViaturaAtiva.isSelected() && horaBaixaViatura.getLocalTime() == null) {
                        tituloMensagem = "Erro Hora Baixa";
                        corpoMensagem = "A opção de Baixa de viatura foi selecionada\n, devendo ser fornecido a hora da baixa!";
                        break;
                    }
                    if (!verificaHoraViatura()) {
                        break;
                    }

                    retorno = true;
            }
        } else {
            tituloMensagem = "Erro Tipo P.O.";
            corpoMensagem = "O P.O. não foi selecionado!";
        }

        return retorno;
    }

    private boolean verificaHoraViatura() {
        if (horaInicialPlantaoViatura.getLocalTime() == null) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Incial Plantão";
            corpoMensagem = "É necessário Informar o hora do inicio do plantão deste PO!";
            return false;
        }

        if (horaFinalPlantaoViatura.getLocalTime() == null) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Final Plantão";
            corpoMensagem = "É necessário Informar o hora do final do plantão deste PO!";
            return false;
        }

        if (horaPausa1Viatura.getLocalTime() == null && horaPausa2Viatura.getLocalTime() != null) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Pausa 2";
            corpoMensagem = "Não é possivel cadastrar pausa2 antes da hora da pausa1!";
            return false;
        }

        //É antes
        if ((horaFinalPlantaoViatura.getLocalTime() != null && horaInicialPlantaoViatura.getLocalTime() != null)
                && horaFinalPlantaoViatura.getLocalTime().isBefore(horaInicialPlantaoViatura.getLocalTime())) {
            if (relatorioDeMesa.getDataFinal().equals(relatorioDeMesa.getDataInicial())) {
                tituloMensagem = "Data Inválida";
                corpoMensagem = "Corrija o campo Hora Plantão Inicial!\nEsta não poderá anterior a Hora de saída!";
                return false;
            }
        }

        if ((horaPausa2Viatura.getLocalTime() != null && horaPausa1Viatura.getLocalTime() != null)
                && horaPausa2Viatura.getLocalTime().isBefore(horaPausa1Viatura.getLocalTime())) {
            if (relatorioDeMesa.getDataFinal().equals(relatorioDeMesa.getDataInicial())) {
                tituloMensagem = "Data Inválida";
                corpoMensagem = "Corrija o campo Hora da Pausa2!\nEsta não poderá ser anterior a Hora da Pausa1!";
                return false;
            }
        }
        return true;
    }

    private boolean verificaHoraServidor() {
        if (horaInicialPlantaoServidor.getLocalTime() == null) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Incial Plantão";
            corpoMensagem = "É necessário Informar o hora do inicio do plantão deste Servidor!";
            return false;
        }

        if (horaInicialPlantaoServidor.getLocalTime().isBefore(horaInicialPlantaoViatura.getLocalTime())) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Incial Plantão";
            corpoMensagem = "A hora do inicio do plantão deste servidor não poderá ser\n"
                    + "inferior a hora inicial do plantão deste PO!";
            return false;
        }

        if (horaFinalPlantaoServidor.getLocalTime() == null) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Final Plantão";
            corpoMensagem = "É necessário Informar o hora do final do plantão deste Servidor!";
            return false;
        }

        if (horaFinalPlantaoServidor.getLocalTime().isAfter(horaFinalPlantaoViatura.getLocalTime())) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Final Plantão";
            corpoMensagem = "A hora do fim do plantão do servidor não poderá ser\n"
                    + "superior a hora final do plantão deste PO!";
            return false;
        }

        if (horaPausa1Servidor.getLocalTime() == null && horaPausa2Servidor.getLocalTime() != null) {
            //Pop-up informando o cadastro
            tituloMensagem = "Erro Hora Pausa 2";
            corpoMensagem = "Não é possivel cadastrar pausa2 antes da hora da pausa1 deste Servidor!";
            return false;
        }

        //É antes
        if ((horaFinalPlantaoServidor.getLocalTime() != null && horaInicialPlantaoServidor.getLocalTime() != null)
                && horaFinalPlantaoServidor.getLocalTime().isBefore(horaInicialPlantaoServidor.getLocalTime())) {
            if (relatorioDeMesa.getDataFinal().equals(relatorioDeMesa.getDataInicial())) {
                tituloMensagem = "Data Inválida";
                corpoMensagem = "Corrija o campo Hora Plantão Inicial!\nEsta não poderá anterior a Hora de saída deste Servidor!";
                return false;
            }
        }

        if ((horaPausa2Servidor.getLocalTime() != null && horaPausa1Servidor.getLocalTime() != null)
                && horaPausa2Servidor.getLocalTime().isBefore(horaPausa1Servidor.getLocalTime())) {
            if (relatorioDeMesa.getDataFinal().equals(relatorioDeMesa.getDataInicial())) {
                tituloMensagem = "Data Inválida";
                corpoMensagem = "Corrija o campo Hora da Pausa2!\nEsta não poderá ser anterior a Hora da Pausa1 deste Servidor!";
                return false;
            }
        }
        return true;
    }

    @FXML
    private void clickedTxtNomeServidor() {
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
    private void clickedCbFuncao(ActionEvent event) {
        funcao = cbFuncao.getSelectionModel().getSelectedItem();

        if (funcao != null && servidor != null) {
            btnAdicionarServidor.setDisable(false);
        } else {
            btnAdicionarServidor.setDisable(true);
        }
    }

    @FXML
    private void clickedCbUnidadeOperacional(ActionEvent event) {
    }

    @FXML
    private void clickedBtnSalvar(MouseEvent event) {
        if (verificaDados()) {
            /*
                Verifica se está editando ou salvando nova viatura
                se viatura == null ---> Salvando nova
                se Viatura != null ---> Editando
             */
            if (viatura == null) {
                viatura = new Viatura();

                viatura.setTipoPO(tipoPO);
                viatura.setPrefixo(txtPrefixo.getText());

                //horario
                viatura.setHoraInicialPlantao(horaInicialPlantaoViatura.getLocalTime());
                viatura.setHoraFinalPlantao(horaFinalPlantaoViatura.getLocalTime());
                viatura.setHoraPausa1(horaPausa1Viatura.getLocalTime());
                viatura.setHoraPausa2(horaPausa2Viatura.getLocalTime());

                //BCS
                viatura.setBcs(tbBCS.isSelected());

                //GPS
                viatura.setGps(tbGPS.isSelected());
                viatura.setEstadoGPS(tbEstadoGPS.isSelected());

                //HT e Audio
                viatura.setHt(tbHT.isSelected());
                viatura.setAudio(tbAudio.isSelected());
                viatura.setTomboHT(txtTomboHT.getText());

                //Camera
                viatura.setCamera(tbCamera.isSelected());
                viatura.setEstadoCam(tbEstadoCam.isSelected());

                //Baixa Viatura
                viatura.setVtrBaixada(tbViaturaAtiva.isSelected());
                viatura.setHrDaBaixa(horaBaixaViatura.getLocalTime());

                //Guarnicao
                viatura.setGuarnicao(listaDeServidores);

                relatorioDeViatura.getViaturas().add(viatura);

                root.getScene().getWindow().hide();
            } else {
                viatura.setTipoPO(tipoPO);
                viatura.setPrefixo(txtPrefixo.getText());

                //horario
                viatura.setHoraInicialPlantao(horaInicialPlantaoViatura.getLocalTime());
                viatura.setHoraFinalPlantao(horaFinalPlantaoViatura.getLocalTime());
                viatura.setHoraPausa1(horaPausa1Viatura.getLocalTime());
                viatura.setHoraPausa2(horaPausa2Viatura.getLocalTime());

                //BCS
                viatura.setBcs(tbBCS.isSelected());

                //GPS
                viatura.setGps(tbGPS.isSelected());
                viatura.setEstadoGPS(tbEstadoGPS.isSelected());

                //HT e Audio
                viatura.setHt(tbHT.isSelected());
                viatura.setAudio(tbAudio.isSelected());
                viatura.setTomboHT(txtTomboHT.getText());

                //Camera
                viatura.setCamera(tbCamera.isSelected());
                viatura.setEstadoCam(tbEstadoCam.isSelected());

                //Baixa Viatura
                viatura.setVtrBaixada(tbViaturaAtiva.isSelected());
                viatura.setHrDaBaixa(horaBaixaViatura.getLocalTime());

                //Guarnicao
                viatura.setGuarnicao(listaDeServidores);

                root.getScene().getWindow().hide();
            }

            botaoSalvarClicado = true;

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(tituloMensagem);
            alert.setHeaderText(corpoMensagem);
            alert.showAndWait();
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
            viatura = null;
            root.getScene().getWindow().hide();
        }
    }

    @FXML
    private void clickedBtnAdicionarServidor(MouseEvent event) {
        if (btnAdicionarServidor.getText().equalsIgnoreCase("novo")) {
            servidorEditar = null;
            servidor = null;
            funcao = null;

            //Desabilitar e Limpa dados para um novo ServidorFunção
            cbFuncao.setValue(null);
            cbFuncao.setDisable(true);
            txtNomeServidor.clear();
            txtNomeServidor.setDisable(false);
            servidor = null;
            
            btnAdicionarServidor.setDisable(true);
            btnAdicionarServidor.setText("Adicionar");

        } else if (btnAdicionarServidor.getText().equalsIgnoreCase("Salvar")) {
            if (verificaHoraServidor()) {
                servidorEditar.setFuncao(funcao);
                servidorEditar.setHoraInicialPlantao(horaInicialPlantaoServidor.getLocalTime());
                servidorEditar.setHoraFinalPlantao(horaFinalPlantaoServidor.getLocalTime());
                servidorEditar.setHoraPausa1(horaPausa1Servidor.getLocalTime());
                servidorEditar.setHoraPausa2(horaPausa2Servidor.getLocalTime());

                //Carrega os dados na tabela Servidores
                carregaDadosTablelaServidorGuarnicao(listaDeServidores);
                servidorEditar = null;
                servidor = null;
                funcao = null;

                //Desabilitar e Limpa dados para um novo ServidorFunção
                cbFuncao.setValue(null);
                cbFuncao.setDisable(true);
                txtNomeServidor.clear();
                servidor = null;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Servidor Adicionado");
                alert.setHeaderText("Servidor adicionado!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }

        } else {
            if (verificaHoraServidor()) {
                listaDeServidores.add(new ServidorFuncao(servidor, funcao, horaInicialPlantaoServidor.getLocalTime(),
                        horaFinalPlantaoServidor.getLocalTime(), horaPausa1Servidor.getLocalTime(), horaPausa2Servidor.getLocalTime(), true));

                //Carrega os dados na tabela Servidores
                carregaDadosTablelaServidorGuarnicao(listaDeServidores);

                //Desabilitar e Limpa dados para um novo ServidorFunção
                cbFuncao.setValue(null);
                cbFuncao.setDisable(true);
                txtNomeServidor.clear();
                servidor = null;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Servidor Adicionado");
                alert.setHeaderText("Servidor adicionado!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tituloMensagem);
                alert.setHeaderText(corpoMensagem);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void clickedTbGPS() {
        if (tbGPS.isSelected()) {
            tbGPS.setText("Possui GPS");
        } else {
            tbGPS.setText("Sem GPS");
        }
    }

    @FXML
    private void clickedTbBCS() {
        if (tbBCS.isSelected()) {
            tbBCS.setText("Pertence a BCS");
        } else {
            tbBCS.setText("Não Pertence a BCS");
        }
    }

    @FXML
    private void clickedTbEstadoGPS() {
        if (tbGPS.isSelected()) {
            tbGPS.setText("GPS Ativo");
        } else {
            tbGPS.setText("GPS Inativo");
        }
    }

    @FXML
    private void clickedTbCamera() {
        if (tbCamera.isSelected()) {
            tbCamera.setText("Possui Câmera");
        } else {
            tbCamera.setText("Sem Câmera");
        }
    }

    @FXML
    private void clickedTbEstadoCam() {
        if (tbEstadoCam.isSelected()) {
            tbEstadoCam.setText("Câmera Ativa");
        } else {
            tbEstadoCam.setText("Cãmera Inativa");
        }
    }

    @FXML
    private void clickedTbHT() {
        if (tbHT.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atenção!");
            alert.setHeaderText("Informe o tombo do HT");
            alert.getButtonTypes().clear();

            tbHT.setText("Possui HT");
            txtTomboHT.setDisable(false);
        } else {
            txtTomboHT.setDisable(true);
            txtTomboHT.setText("");
            tbHT.setText("Sem HT");
        }
    }

    @FXML
    private void clickedTbAudio() {
        if (tbAudio.isSelected()) {
            tbAudio.setText("Possui Audio");
        } else {
            tbAudio.setText("Sem Audio");
        }
    }

    @FXML
    private void clickedTbViaturaAtiva() {
        if (tbViaturaAtiva.isSelected()) {

            if (horaBaixaViatura.getLocalTime() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Atenção!");
                alert.setHeaderText("Informe o horário da Baixa da Viatura");
                alert.getButtonTypes().clear();

                tbViaturaAtiva.setSelected(false);
                tbViaturaAtiva.setText("Ativa");
            } else {
                Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
                alertAntesExcluir.setTitle("Atenção!");
                alertAntesExcluir.setHeaderText("A viatura selecionada será BAIXADA!. Será necessário"
                        + "\ncriar uma nova viatura com a mesma guarnição?");
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
                    tbViaturaAtiva.setText("Inativa");

                } else {
                    tbViaturaAtiva.setSelected(false);
                }
            }
        } else {
            tbViaturaAtiva.setText("Ativa");
        }
    }

    void setRelatorioDeMesa(RelatorioDiarioMesas value) {
        this.relatorioDeMesa = value;
    }

    public Viatura getViatura() {
        return this.viatura;
    }

    public void setViatura(Viatura value) {
        if (value != null) {

            this.viatura = new Viatura();
            this.viatura.setId(value.getId());

            this.cbUnidade.setConverter(new StringConverter<Unidade>() {
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
            this.cbUnidade.getSelectionModel().select(relatorioDeViatura.getUnidade());

            this.cbTipoPO.setConverter(new StringConverter<PO>() {
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
            this.cbTipoPO.getSelectionModel().select(value.getTipoPO());
            this.tipoPO = value.getTipoPO();

            //Horário
            this.txtPrefixo.setText(value.getPrefixo());
            this.horaInicialPlantaoViatura.setLocalTime(value.getHoraInicialPlantao());
            this.horaFinalPlantaoViatura.setLocalTime(value.getHoraFinalPlantao());
            this.horaPausa1Viatura.setLocalTime(value.getHoraPausa1());
            this.horaPausa2Viatura.setLocalTime(value.getHoraPausa2());

            //BCS
            this.tbBCS.setSelected(value.isBcs());

            //GPS
            this.tbGPS.setSelected(value.isGps());
            this.tbEstadoGPS.setSelected(value.isEstadoGPS());

            //Audio HT
            this.tbHT.setSelected(value.isHt());
            this.tbAudio.setSelected(value.isAudio());
            this.txtTomboHT.setText(value.getTomboHT());

            //CAMERA
            this.tbCamera.setSelected(value.isCamera());
            this.tbEstadoCam.setSelected(value.isCamera());

            //Viatura Baixada
            this.tbViaturaAtiva.setSelected(value.isVtrBaixada());
            this.horaBaixaViatura.setLocalTime(value.getHrDaBaixa());

            verificaTipoPO(value.getTipoPO());

            this.listaDeServidores.addAll(value.getGuarnicao());

            carregaDadosTablelaServidorGuarnicao(value.getGuarnicao());
        }
    }

    public RelatorioDiarioViaturas getRelatorioDeViatura() {
        return this.relatorioDeViatura;
    }

    void setRelatorioDeViatura(RelatorioDiarioViaturas value) {
        this.relatorioDeViatura = value;

        if (value != null) {
            listaPOS.addAll(relatorioDeViatura.getUnidade().getPos());
            listaUnidade.add(relatorioDeViatura.getUnidade());
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
            cbUnidade.getItems().addAll(listaUnidade);
            cbUnidade.getSelectionModel().select(value.getUnidade());
        }
    }

    private void verificaTipoPO(PO tipoPO) {
        if (tipoPO != null) {
            switch (tipoPO.getNome()) {
                case "VIATURA 2 RODAS":
                    txtPrefixo.setDisable(false);
                    //Habilita as horas
                    horaInicialPlantaoViatura.setDisable(false);
                    horaFinalPlantaoViatura.setDisable(false);
                    horaPausa1Viatura.setDisable(false);
                    horaPausa2Viatura.setDisable(false);

                    //Habilita os itens do PO
                    tbBCS.setDisable(false);
                    tbGPS.setDisable(false);
                    tbEstadoGPS.setDisable(false);
                    tbHT.setDisable(false);
                    tbAudio.setDisable(false);
                    tbCamera.setDisable(true);
                    tbEstadoCam.setDisable(true);
                    break;
                case "BARCO":
                    txtPrefixo.setDisable(false);
                    //Habilita as horas
                    horaInicialPlantaoViatura.setDisable(false);
                    horaFinalPlantaoViatura.setDisable(false);
                    horaPausa1Viatura.setDisable(false);
                    horaPausa2Viatura.setDisable(false);

                    //Habilita os itens do PO
                    tbBCS.setDisable(false);
                    tbGPS.setDisable(false);
                    tbEstadoGPS.setDisable(false);
                    tbHT.setDisable(false);
                    tbAudio.setDisable(false);
                    tbCamera.setDisable(false);
                    tbEstadoCam.setDisable(false);
                    break;
                case "A PÉ":
                    txtPrefixo.setDisable(true);
                    //Habilita as horas
                    horaInicialPlantaoViatura.setDisable(false);
                    horaFinalPlantaoViatura.setDisable(false);
                    horaPausa1Viatura.setDisable(false);
                    horaPausa2Viatura.setDisable(false);

                    //Habilita os itens do PO
                    tbBCS.setDisable(false);
                    tbGPS.setDisable(true);
                    tbEstadoGPS.setDisable(true);
                    tbHT.setDisable(false);
                    tbAudio.setDisable(false);
                    tbCamera.setDisable(true);
                    tbEstadoCam.setDisable(true);
                    break;
                case "MÓDULO":
                    txtPrefixo.setDisable(true);
                    //Habilita as horas
                    horaInicialPlantaoViatura.setDisable(false);
                    horaFinalPlantaoViatura.setDisable(false);
                    horaPausa1Viatura.setDisable(false);
                    horaPausa2Viatura.setDisable(false);

                    //Habilita os itens do PO
                    tbBCS.setDisable(false);
                    tbGPS.setDisable(true);
                    tbEstadoGPS.setDisable(true);
                    tbHT.setDisable(false);
                    tbAudio.setDisable(false);
                    tbCamera.setDisable(true);
                    tbEstadoCam.setDisable(true);
                    break;
                case "VIATURA 4 RODAS":
                    txtPrefixo.setDisable(false);
                    //Habilita as horas
                    horaInicialPlantaoViatura.setDisable(false);
                    horaFinalPlantaoViatura.setDisable(false);
                    horaPausa1Viatura.setDisable(false);
                    horaPausa2Viatura.setDisable(false);

                    //Habilita os itens do PO
                    tbBCS.setDisable(false);
                    tbGPS.setDisable(false);
                    tbEstadoGPS.setDisable(false);
                    tbHT.setDisable(false);
                    tbAudio.setDisable(false);
                    tbCamera.setDisable(false);
                    tbEstadoCam.setDisable(false);
                    break;
                default:
                    txtPrefixo.setDisable(true);
                    //Habilita as horas
                    horaInicialPlantaoViatura.setDisable(true);
                    horaFinalPlantaoViatura.setDisable(true);
                    horaPausa1Viatura.setDisable(true);
                    horaPausa2Viatura.setDisable(true);

                    //Habilita os itens do PO
                    tbBCS.setDisable(true);
                    tbGPS.setDisable(true);
                    tbEstadoGPS.setDisable(true);
                    tbHT.setDisable(true);
                    tbAudio.setDisable(true);
                    tbCamera.setDisable(true);
                    tbEstadoCam.setDisable(true);
                    txtTomboHT.setDisable(true);
                    break;
            }

            //Habilita Botão Salvar
            btnAvancar.setDisable(false);
        }

        //Trocar os nomes
        clickedTbAudio();
        clickedTbBCS();
        clickedTbGPS();
        clickedTbEstadoGPS();
        clickedTbCamera();
        clickedTbHT();
        clickedTbViaturaAtiva();
    }

    //    Define os botões de ação do Servidor na Guarnicao
    private class ButtonCellServidorGuarnicao extends TableCell<ServidorFuncao, Boolean> {

        //BOTAO REMOVER
        private Button botaoRemover = new Button();
        private final ImageView imagemRemover = new ImageView(new Image(getClass().getResourceAsStream("/icons/remover.png")));

        //BOTAO EDITAR
        private Button botaoEditar = new Button();
        private final ImageView imagemEditar = new ImageView(new Image(getClass().getResourceAsStream("/icons/editar.png")));

        //BOTAO EDITAR
        private Button botaoVisualizar = new Button();
        private final ImageView imagemVisualizar = new ImageView(new Image(getClass().getResourceAsStream("/icons/visualizar.png")));

        private ButtonCellServidorGuarnicao(TableView<ServidorFuncao> tblView) {
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
                        horaInicialPlantaoServidor.setLocalTime(servidorVisualizar.getHoraInicialPlantao());
                        horaFinalPlantaoServidor.setLocalTime(servidorVisualizar.getHoraFinalPlantao());
                        horaPausa1Servidor.setLocalTime(servidorVisualizar.getHoraPausa1());
                        horaPausa2Servidor.setLocalTime(servidorVisualizar.getHoraPausa2());

                        //Desabilita Tudo
                        txtNomeServidor.setDisable(true);
                        cbFuncao.setDisable(true);
                        horaInicialPlantaoServidor.setDisable(true);
                        horaFinalPlantaoServidor.setDisable(true);
                        horaPausa1Servidor.setDisable(true);
                        horaPausa2Servidor.setDisable(true);

                        btnAdicionarServidor.setDisable(false);
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
                        horaInicialPlantaoServidor.setLocalTime(servidorEditar.getHoraInicialPlantao());
                        horaFinalPlantaoServidor.setLocalTime(servidorEditar.getHoraFinalPlantao());
                        horaPausa1Servidor.setLocalTime(servidorEditar.getHoraPausa1());
                        horaPausa2Servidor.setLocalTime(servidorEditar.getHoraPausa2());

                        cbFuncao.setDisable(false);
                        horaInicialPlantaoServidor.setDisable(false);
                        horaFinalPlantaoServidor.setDisable(false);
                        horaPausa1Servidor.setDisable(false);
                        horaPausa2Servidor.setDisable(false);

                        btnAdicionarServidor.setDisable(false);
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
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Atenção!");
                    alert.setHeaderText("Os dados referentes à este Servidor serão perdidos, deseja continuar?");
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

                    //Deactivate Defaultbehavior for yes-Button:
                    Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
                    yesButton.setDefaultButton(false);

                    //Activate Defaultbehavior for no-Button:
                    Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
                    noButton.setDefaultButton(true);

                    //Pega qual opção o usuario pressionou
                    final Optional<ButtonType> resultado1 = alert.showAndWait();

                    if (resultado1.get() == ButtonType.YES) {
                        listaDeServidores.remove(servidorRemover);

                        btnAdicionarServidor.setDisable(false);
                        btnAdicionarServidor.setText("novo");

                        limparDadosServidor();

                        carregaDadosTablelaServidorGuarnicao(listaDeServidores);
                    }
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);

            if (!empty) {
                HBox hb = new HBox();
                hb.setAlignment(Pos.CENTER);
                hb.getChildren().addAll(botaoEditar);
                hb.getChildren().addAll(botaoVisualizar);
                hb.getChildren().addAll(botaoRemover);
                setGraphic(hb);
                setAlignment(Pos.CENTER);
            } else {
                setGraphic(null);
            }
        }
    }

    public boolean isBotaoSalvarClicado() {
        return botaoSalvarClicado;
    }

    public void setBotaoSalvarClicado(boolean botaoSalvarClicado) {
        this.botaoSalvarClicado = botaoSalvarClicado;
    }
}
