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
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class TelaCadastroMesaController implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private DatePicker dataMesa;
    @FXML
    private ComboBox<String> cbTurno;
    @FXML
    private ComboBox<Mesa> cbMesa;
    @FXML
    private ComboBox<Servidor> cbSupervisor;
    @FXML
    private ComboBox<Servidor> cbOperador;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lbDia;
    @FXML
    private Label lbTurno;
    @FXML
    private Label lbMesa;
    @FXML
    private Label lbSupervisor;
    @FXML
    private Label lbOperador;

    private Stage dialogStage;
    private String mensagemErroTela = null;
    private String mensagemErroCorpo = null;

    //DAO
    private final MesaDAO daoMesa = new MesaDAO();
    private final ServidorDAO daoServidor = new ServidorDAO();
    private final RelatorioDiarioMesasDAO daoRelatorioDiarioMesas = new RelatorioDiarioMesasDAO();
    private List<Mesa> listaDeMesas;
    private RelatorioDiarioMesas relatorio;

    public TelaCadastroMesaController() {
    }

    public RelatorioDiarioMesas getRelatorio() {
        return this.relatorio;
    }

    public void setRelatorio(RelatorioDiarioMesas value) {
        this.relatorio = value;
        
        //Carrega os dados para os campos
        this.dataMesa.setValue(value.getDia());
        this.cbTurno.getSelectionModel().select(value.getTurno());
        this.cbMesa.getSelectionModel().select(value.getMesa());
        this.cbSupervisor.getSelectionModel().select(value.getSupervisor().getServidor());
        clickedCbSupervisor();
        this.cbOperador.getSelectionModel().select(value.getOperador().getServidor());
        
        //Bloqueia a edição para os campos necessários
        this.cbOperador.setDisable(false);
        this.dataMesa.setDisable(true);
        this.cbMesa.setDisable(true);
        
        this.btnCadastrar.setText("Salvar");
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
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void carregaDadosIniciais() {
        // CarregaFuncionários
        listaDeMesas = (ArrayList<Mesa>) daoMesa.getList("FROM Mesa");
        List<Servidor> listaDeServidores = (ArrayList<Servidor>) daoServidor.getListAtivos();

        if (!verificaDadosIniciais(listaDeMesas, listaDeServidores)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(mensagemErroTela);
            alert.setHeaderText(mensagemErroCorpo);
            alert.showAndWait();
            root.getScene().getWindow().hide();
        } else {
            //Converte as opções para o modo String
            List<String> listaDeTurnos = new ArrayList<>();
            listaDeTurnos.add("TURNO 1");
            listaDeTurnos.add("TURNO 2");
            cbTurno.setItems(FXCollections.observableList(listaDeTurnos));

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
            cbSupervisor.setConverter(new StringConverter<Servidor>() {
                @Override
                public String toString(Servidor item) {
                    if (item != null) {
                        return item.getNome() + " - " + item.getMatricula();
                    }
                    return "";
                }

                @Override
                public Servidor fromString(String string) {
                    return daoServidor.buscaPorNome(string);
                }
            });

            //Lança todos os servidores cadastrados para a escolha do usuário do Supervisor
            cbSupervisor.setItems(FXCollections.observableList(listaDeServidores));
        }
    }

    private boolean verificaDadosIniciais(List<Mesa> listaDeMesas, List<Servidor> listaDeServidores) {
        //Verifica Se Há Mesas Disponiveis
        if (listaDeMesas.isEmpty()) {
            //Pop-up informando o cadastro
            mensagemErroTela = "Erro Mesas";
            mensagemErroCorpo = "Possivelmente não há mesas cadastradas/diponiveis no sitema!";
            return false;
        }

        //Verifica Se Há Supervisor Disponiveis
        if (listaDeServidores.isEmpty()) {
            //Pop-up informando o cadastro
            mensagemErroTela = "Erro Servidores";
            mensagemErroCorpo = "Possivelmente não há Servidores cadastradas/diponiveis no sitema!";
            return false;
        }

        return true;
    }

    private boolean verificaDadosDigitados() {
        if (!dataMesa.getValue().isEqual(LocalDate.now())) {
            mensagemErroTela = "Data Inválida";
            mensagemErroCorpo = "Corrija o campo DIA!";
            return false;
        }

        if (cbTurno.getSelectionModel().getSelectedItem().isEmpty()) {
            mensagemErroTela = "Turno Inválido";
            mensagemErroCorpo = "Corrija o campo TURNO!";
            return false;
        }

        if (cbMesa.getSelectionModel().getSelectedItem() == null) {
            mensagemErroTela = "Mesa Inválida";
            mensagemErroCorpo = "O campo MESA é obrigatório!";
            return false;
        }

        if (cbSupervisor.getSelectionModel().getSelectedItem() == null) {
            mensagemErroTela = "Supervisor Inválida";
            mensagemErroCorpo = "O campo SUPERVISOR é obrigatório!";
            return false;
        }

        if (cbOperador.getSelectionModel().getSelectedItem() == null) {
            mensagemErroTela = "Operador Inválida";
            mensagemErroCorpo = "O campo OPERADOR é obrigatório!";
            return false;
        }
        return true;
    }

    @FXML
    private void clickedCadastrar() {
        if (verificaDadosDigitados()) {
            if (relatorio == null) {
                //Cadastra nova mesa
                relatorio = new RelatorioDiarioMesas();
                
                relatorio.setDia(dataMesa.getValue());
                relatorio.setTurno(cbTurno.getSelectionModel().getSelectedItem());
                relatorio.setMesa((Mesa) cbMesa.getSelectionModel().getSelectedItem());

                FuncaoDAO daoFuncao = new FuncaoDAO();

                Funcao func1 = daoFuncao.buscaPorNome("Supervisor");
                Funcao func2 = daoFuncao.buscaPorNome("Operador");

                relatorio.setSupervisor(new ServidorFuncao(cbSupervisor.getSelectionModel().getSelectedItem(), func1));
                relatorio.setOperador(new ServidorFuncao(cbOperador.getSelectionModel().getSelectedItem(), func2));

                //Cadastra o resumo no banco
                daoRelatorioDiarioMesas.salvar(relatorio);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Mesa cadastrada com Sucesso");
                alert.showAndWait();
                root.getScene().getWindow().hide();
            } else{
                //Edita a mesa
                relatorio.setTurno(cbTurno.getSelectionModel().getSelectedItem());
                
                //Modifico o Supervisor
                relatorio.getSupervisor().setServidor(cbSupervisor.getSelectionModel().getSelectedItem());
                
                //Modifico o Operador
                relatorio.getOperador().setServidor(cbOperador.getSelectionModel().getSelectedItem());
                
                //Cadastra o resumo no banco
                daoRelatorioDiarioMesas.alterar(relatorio);

                //
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
    private void clickedCancelar() {
        root.getScene().getWindow().hide();
    }

    @FXML
    private void clickedCbSupervisor() {
        Servidor ser = cbSupervisor.getSelectionModel().getSelectedItem();
        System.out.println("Selecionado Supervisor!");
        if (ser != null) {
            //Habilita o campo Operador, caso o Supervisor Seja selecionado
            cbOperador.setDisable(false);
            if (cbOperador.getItems().isEmpty()) {
                cbOperador.getItems().clear();
            }
            carregaDadosIniciaisCbOperador(ser);
        }
    }

    @FXML
    private void clickedCbOperador() {
    }

    private void carregaDadosIniciaisCbOperador(Servidor supervisorSelecionado) {
        List<Servidor> listaDeOperadores = (ArrayList<Servidor>) daoServidor.getList("FROM Servidor");

        cbOperador.setConverter(new StringConverter<Servidor>() {
            @Override
            public String toString(Servidor item) {
                if (item != null) {
                    return item.getNome();
                }
                return "";
            }

            @Override
            public Servidor fromString(String string) {
                return daoServidor.buscaPorNome(string);
            }
        });

        //Carrega os Servidores sem o Servidor Selecionado para ser o Supervisor
        cbOperador.setItems(FXCollections.observableList(listaDeOperadores));

        cbOperador.getItems().remove(supervisorSelecionado);
    }
}
