package com.cicom.relatorioviaturas.controllers;

import com.cicom.relatorioviaturas.DAO.FuncaoDAO;
import com.cicom.relatorioviaturas.DAO.PoDAO;
import com.cicom.relatorioviaturas.DAO.RelatorioDiarioMesasDAO;
import com.cicom.relatorioviaturas.DAO.RelatorioDiarioViaturasDAO;
import com.cicom.relatorioviaturas.DAO.ViaturaDAO;
import com.cicom.relatorioviaturas.model.Funcao;
import com.cicom.relatorioviaturas.model.PO;
import com.cicom.relatorioviaturas.model.RelatorioDiarioMesas;
import com.cicom.relatorioviaturas.model.RelatorioDiarioViaturas;
import com.cicom.relatorioviaturas.model.Servidor;
import com.cicom.relatorioviaturas.model.ServidorFuncao;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class TelaAdicionaOperacionalController implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private ComboBox<PO> cbTipoPO = new ComboBox<>();
    @FXML
    private TextField txtPrefixo;
    @FXML
    private TextField txtVtrBaixada;
    @FXML
    private ToggleButton tbBCS;
    @FXML
    private ToggleButton tbGPS;
    @FXML
    private ToggleButton tbCamera;
    @FXML
    private ToggleButton tbHT;
    @FXML
    private ToggleButton tbAudio;
    @FXML
    private ToggleButton tbEstado;
    @FXML
    private ToggleButton tbEstadoCam;
    @FXML
    private ToggleButton tbVtrBaixada;
    @FXML
    private TextField txtBuscaServidor;
    @FXML
    private Button btBuscaServidor;
    @FXML
    private ComboBox<Funcao> cbFuncao = new ComboBox<>();
    @FXML
    private TableView<ServidorFuncao> tableServidorGuarnicao;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnNomeServidor = new TableColumn<>("servidor");
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnFuncaoServidor = new TableColumn<>("funcao");
    @FXML
    private Button btSalvar;
    @FXML
    private Button btCancelar;
    @FXML
    private Button btAdicionarServidor;
    @FXML
    private Button btRemoveServidor;

    private PoDAO daoPO = new PoDAO();
    private FuncaoDAO daoFuncao = new FuncaoDAO();
    private RelatorioDiarioMesasDAO daoRelatorioMesa = new RelatorioDiarioMesasDAO();
    private RelatorioDiarioViaturasDAO daoRelatorioViatura = new RelatorioDiarioViaturasDAO();

    private Viatura viatura;
    private Servidor servidor = new Servidor();
    private Funcao funcao = new Funcao();
    private ServidorFuncao servidorFuncao = new ServidorFuncao();
    private Set<ServidorFuncao> listaDeServidores = new HashSet<>();
    private List<Funcao> listaFuncao = new ArrayList<>();
    private List<PO> listaPOS = new ArrayList<>();
    private PO tipoPO;
    private String tituloMensagem;
    private String corpoMensagem;
    private RelatorioDiarioMesas relatorioDeMesa;
    private RelatorioDiarioViaturas relatorioDeViatura;
    private Stage stage;
    private ViaturaDAO daoViatura = new ViaturaDAO();
    private boolean botaoSalvarClicado = false;

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

                            if (viatura.isVtrBaixada()) {
                                btSalvar.setDisable(true);
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
            btRemoveServidor.setDisable(false);
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
            tableServidorGuarnicao.getItems().setAll(dados);
        } else {
            tableServidorGuarnicao.setDisable(true);
            btRemoveServidor.setDisable(true);
        }
    }

    @FXML
    private void clikedBtBuscaServidor(MouseEvent event) {
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
                txtBuscaServidor.setText(servidor.getNome());
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
            btAdicionarServidor.setDisable(false);
        } else {
            btAdicionarServidor.setDisable(true);
        }
    }

    @FXML
    private void clickedBtAdicionarServidor(MouseEvent event) {

        listaDeServidores.add(new ServidorFuncao(servidor, funcao, null, null, null, null, true));

        //Carrega os dados na tabela Servidores
        carregaDadosTablelaServidorGuarnicao(listaDeServidores);

        //Desabilitar e Limpa dados para um novo ServidorFunção
        cbFuncao.setValue(null);
        cbFuncao.setDisable(true);
        txtBuscaServidor.clear();
        servidor = null;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Servidor Adicionado");
        alert.setHeaderText("Servidor adicionado!");
        alert.showAndWait();
    }

    @FXML
    private void clickedBtRemoveServidor() {
        ServidorFuncao servidorParaExcluir = tableServidorGuarnicao.getSelectionModel().getSelectedItem();

        if (!tableServidorGuarnicao.getItems().isEmpty() && servidorParaExcluir != null) {

            listaDeServidores.remove(servidorParaExcluir);

            //Carrega os dados na tabela Servidores
            carregaDadosTablelaServidorGuarnicao(listaDeServidores);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("É necessário clicar no Servidor abaixo!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedExitedTableServidorGuarnicao(MouseEvent event) {
    }

    @FXML
    private void clickedCbTipoPO(ActionEvent event) {
        tipoPO = cbTipoPO.getSelectionModel().getSelectedItem();

        //Retorna ao valor padrão
        tbAudio.setSelected(false);
        tbBCS.setSelected(false);
        tbEstado.setSelected(false);
        tbGPS.setSelected(false);
        tbCamera.setSelected(false);
        tbEstadoCam.setSelected(false);
        tbHT.setSelected(false);
        tbVtrBaixada.setSelected(false);

        verificaTipoPO(tipoPO);
    }

    @FXML
    private void tbBCS() {
        if (tbBCS.isSelected()) {
            tbBCS.setText("SIM");
        } else {
            tbBCS.setText("NÃO");
        }
    }

    @FXML
    private void tbGPS() {
        if (tbGPS.isSelected()) {
            tbGPS.setText("SIM");
        } else {
            tbGPS.setText("NÃO");
        }
    }

    @FXML
    private void tbCamera() {
        if (tbCamera.isSelected()) {
            tbCamera.setText("SIM");
        } else {
            tbCamera.setText("NÃO");
        }
    }

    @FXML
    private void tbHT() {
        if (tbHT.isSelected()) {
            tbHT.setText("SIM");
        } else {
            tbHT.setText("NÃO");
        }
    }

    @FXML
    private void tbAudio() {
        if (tbAudio.isSelected()) {
            tbAudio.setText("SIM");
        } else {
            tbAudio.setText("NÃO");
        }
    }

    @FXML
    private void tbEstado() {
        if (tbEstado.isSelected()) {
            tbEstado.setText("ATIVO");
        } else {
            tbEstado.setText("INATIVO");
        }
    }

    @FXML
    private void tbEstadoCam() {
        if (tbEstadoCam.isSelected()) {
            tbEstadoCam.setText("ATIVO");
        } else {
            tbEstadoCam.setText("INATIVO");
        }
    }

    @FXML
    private void tbVtrBaixada() {
        if (tbVtrBaixada.isSelected()) {

            Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
            alertAntesExcluir.setTitle("Atenção!");
            alertAntesExcluir.setHeaderText("A viatura selecionada será BAIXADA!. Será necessário"
                    + "\ncriar uma nova viatura com a mesma guarnição,\nvocê tem certeza que deseja continuar?");
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
                tbVtrBaixada.setText("SIM");

            } else {
                tbVtrBaixada.setSelected(false);
            }

        } else {
            tbVtrBaixada.setText("NÃO");
        }
    }

    @FXML
    private void clickedBtSalvar(MouseEvent event) {

        if (verificaDados()) {
            /*
                Verifica se está editando ou salvando nova viatura
                se viatura == null ---> Salvando nova
                se Viatura != null ---> Editando
             */
            if (viatura == null) {
                viatura = new Viatura();

                viatura.setAudio(tbAudio.isSelected());
                viatura.setBcs(tbBCS.isSelected());
                viatura.setGps(tbGPS.isSelected());
                viatura.setCamera(tbCamera.isSelected());
                viatura.setVtrBaixada(tbVtrBaixada.isSelected());

                //Verifica estado GPS
                if (tbEstado.isSelected()) {
                    viatura.setEstado("ATIVO");
                } else {
                    viatura.setEstado("INATIVO");
                }

                //Verifica estado Camera
                if (tbEstadoCam.isSelected()) {
                    viatura.setEstadoCam("ATIVO");
                } else {
                    viatura.setEstadoCam("INATIVO");
                }

                //Verfica estado HT
                if (tbHT.isSelected()) {
                    viatura.setHt("POSSUI");
                } else {
                    viatura.setHt("NÃO POSSUI");
                }

                viatura.setGuarnicao(listaDeServidores);
                viatura.setPrefixo(txtPrefixo.getText());
                viatura.setTipoPO(tipoPO);

                relatorioDeViatura.getViaturas().add(viatura);
                        
                root.getScene().getWindow().hide();
            } else {
                viatura.setAudio(tbAudio.isSelected());
                viatura.setBcs(tbBCS.isSelected());
                viatura.setGps(tbGPS.isSelected());
                viatura.setCamera(tbCamera.isSelected());
                viatura.setVtrBaixada(tbVtrBaixada.isSelected());

                //Verifica estado GPS
                if (tbEstado.isSelected()) {
                    viatura.setEstado("ATIVO");
                } else {
                    viatura.setEstado("INATIVO");
                }

                //Verfica estado HT
                if (tbHT.isSelected()) {
                    viatura.setHt("POSSUI");
                } else {
                    viatura.setHt("NÃO POSSUI");
                }
                //Limpa os servidores
                viatura.getGuarnicao().clear();

                //adiciona os servidores novos
                viatura.setGuarnicao(listaDeServidores);

                viatura.setPrefixo(txtPrefixo.getText());
                viatura.setTipoPO(tipoPO);
                
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
    
    public RelatorioDiarioViaturas getRelatorioDeViatura(){
        return this.relatorioDeViatura;
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

                    if (listaDeServidores.isEmpty()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    } else if (listaDeServidores.size() > 2) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é possivel cadastrar apenas 2 servidores!";
                        break;
                    }

                    retorno = true;
                case "BARCO":
                    if (txtPrefixo.getText().isEmpty()) {
                        tituloMensagem = "Erro Prefixo";
                        corpoMensagem = "O prefixo é necessário!";
                        break;
                    }

                    if (listaDeServidores.isEmpty()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    } else if (listaDeServidores.size() > 6) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é possivel cadastrar 6 servidores!";
                        break;
                    }

                    retorno = true;
                case "A PÉ":
                    if (listaDeServidores.isEmpty()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    }

                    retorno = true;
                case "VIATURA 4 RODAS":
                    if (txtPrefixo.getText().isEmpty()) {
                        tituloMensagem = "Erro Prefixo";
                        corpoMensagem = "O prefixo é necessário!";
                        break;
                    }

                    if (listaDeServidores.isEmpty()) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é necessário cadastrar ao menos 1 servidor!";
                        break;
                    } else if (listaDeServidores.size() > 4) {
                        tituloMensagem = "Erro Guarnição";
                        corpoMensagem = "Para o Tipo P.O. selecionado, \n é possivel cadastrar apenas 4 servidores!";
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

    @FXML
    private void clickedBtVoltar() {
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

    void setRelatorioDeMesa(RelatorioDiarioMesas value) {
        this.relatorioDeMesa = value;
    }

    void setRelatorioDeViatura(RelatorioDiarioViaturas value) {
        this.relatorioDeViatura = value;

        if (value != null) {
            listaPOS.addAll(relatorioDeViatura.getUnidade().getPos());
        }
    }

    public Viatura getViatura() {
        return this.viatura;
    }

    public void setViatura(Viatura value) {
        if (value != null) {

            this.viatura = new Viatura();

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

            this.viatura.setId(value.getId());
            this.tipoPO = value.getTipoPO();
            this.txtPrefixo.setText(value.getPrefixo());
            this.tbBCS.setSelected(value.isBcs());
            this.tbGPS.setSelected(value.isGps());
            this.tbCamera.setSelected(value.isCamera());
            this.tbVtrBaixada.setSelected(value.isVtrBaixada());

            if (value.getHt().equals("POSSUI")) {
                this.tbHT.setSelected(true);

            } else {
                this.tbHT.setSelected(false);
            }

            this.tbAudio.setSelected(value.isAudio());

            if (value.getHt().equals("ATIVO")) {
                this.tbEstado.setSelected(true);
            } else {
                this.tbEstado.setSelected(false);
            }

            verificaTipoPO(value.getTipoPO());

            this.listaDeServidores.addAll(value.getGuarnicao());

            carregaDadosTablelaServidorGuarnicao(value.getGuarnicao());
        }
    }

    private void verificaTipoPO(PO tipoPO) {
        if (tipoPO != null) {
            switch (tipoPO.getNome()) {
                case "VIATURA 2 RODAS":
                    txtPrefixo.setDisable(false);
                    tbAudio.setDisable(false);
                    tbBCS.setDisable(false);
                    tbEstado.setDisable(false);
                    tbGPS.setDisable(false);
                    tbCamera.setDisable(false);
                    tbVtrBaixada.setDisable(false);
                    tbHT.setDisable(false);
                    btBuscaServidor.setDisable(false);
                    break;
                case "BARCO":
                    txtPrefixo.setDisable(false);
                    tbAudio.setDisable(false);
                    tbBCS.setDisable(false);
                    tbEstado.setDisable(false);
                    tbGPS.setDisable(false);
                    tbCamera.setDisable(false);
                    tbVtrBaixada.setDisable(false);
                    tbHT.setDisable(false);
                    btBuscaServidor.setDisable(false);
                    break;
                case "A PÉ":
                    tbAudio.setDisable(false);
                    tbBCS.setDisable(true);
                    tbEstado.setDisable(false);
                    tbGPS.setDisable(false);
                    tbCamera.setDisable(false);
                    tbVtrBaixada.setDisable(false);
                    tbHT.setDisable(false);
                    btBuscaServidor.setDisable(false);
                    break;
                case "VIATURA 4 RODAS":
                    txtPrefixo.setDisable(false);
                    tbAudio.setDisable(false);
                    tbBCS.setDisable(false);
                    tbEstado.setDisable(false);
                    tbGPS.setDisable(false);
                    tbCamera.setDisable(false);
                    tbVtrBaixada.setDisable(false);
                    tbHT.setDisable(false);
                    btBuscaServidor.setDisable(false);
                    break;
                default:
                    txtPrefixo.setDisable(true);
                    tbAudio.setDisable(true);
                    tbBCS.setDisable(true);
                    tbEstado.setDisable(true);
                    tbGPS.setDisable(true);
                    tbCamera.setDisable(false);
                    tbVtrBaixada.setDisable(false);
                    tbHT.setDisable(true);
                    btBuscaServidor.setDisable(true);
                    break;
            }

            //Habilita Botão Salvar
            btSalvar.setDisable(false);
        }

        //Trocar os nomes
        tbAudio();
        tbBCS();
        tbEstado();
        tbGPS();
        tbCamera();
        tbHT();
        tbVtrBaixada();
    }
    
    public boolean isBotaoSalvarClicado() {
        return botaoSalvarClicado;
    }

    public void setBotaoSalvarClicado(boolean botaoSalvarClicado) {
        this.botaoSalvarClicado = botaoSalvarClicado;
    }
    

}
