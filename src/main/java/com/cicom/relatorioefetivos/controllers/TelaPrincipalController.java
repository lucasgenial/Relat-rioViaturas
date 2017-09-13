package com.cicom.relatorioefetivos.controllers;

import com.cicom.relatorioefetivos.model.Efetivo;
import com.cicom.relatorioefetivos.model.PO;
import com.cicom.relatorioefetivos.model.ServidorFuncao;
import com.cicom.relatorioefetivos.model.RelatorioDiarioEfetivo;
import com.cicom.relatorioefetivos.model.RelatorioDiarioMesas;
import com.cicom.relatorioefetivos.DAO.RelatorioDiarioMesasDAO;
import com.cicom.relatorioefetivos.DAO.RelatorioDiarioEfetivoDAO;
import com.cicom.relatorioefetivos.DAO.EfetivoDAO;
import com.cicom.relatorioefetivos.controllers.adm.TelaAdmCadastroMesasController;
import com.cicom.relatorioefetivos.controllers.adm.TelaAdmCadastroServidorController;
import com.cicom.relatorioefetivos.controllers.adm.TelaAdmCadastroTipoPOController;
import com.cicom.relatorioefetivos.controllers.adm.TelaAdmCadastroUnidadesController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author estatistica
 */
public class TelaPrincipalController implements Initializable {

    /*
    MENU PRINCIPAL
     */
    @FXML
    private MenuBar barraDeMenu;
    @FXML
    private Menu opMenuCadastro;
    @FXML
    private MenuItem opMenuTipoPO;
    @FXML
    private MenuItem opMenuUnidade;
    @FXML
    private MenuItem opMenuMesa;
    @FXML
    private MenuItem opMenuServidor;
    @FXML
    private Menu opMenuRelatorio;
    @FXML
    private MenuItem opMenuRelatorioMesas;
    @FXML
    private MenuItem opMenuRelatorioPeriodos;
    @FXML
    private Menu opMenuAjuda;
    @FXML
    private MenuItem opMenuAjudaSobre;

    /*
    PARA EDITAR TABELA DAS MESAS
     */
    @FXML
    private TabPane painelTab;
    @FXML
    private TableView<RelatorioDiarioMesas> tableResumo;
    @FXML
    private TableColumn<RelatorioDiarioMesas, Integer> tbColumnIdResumo;
    @FXML
    private TableColumn<RelatorioDiarioMesas, String> tbColumnDiaResumo;
    @FXML
    private TableColumn<RelatorioDiarioMesas, String> tbColumnHoraResumo;
    @FXML
    private TableColumn<RelatorioDiarioMesas, String> tbColumnNomeMesaResumo;
    @FXML
    private Button btnAdicionarMesa;
    @FXML
    private Button btnEditarMesa;
    @FXML
    private Button btnRemoverMesa;

    /*
    PARA EDITAR TABELA UNIDADE
     */
    @FXML
    private TableView<RelatorioDiarioEfetivo> tableUnidade;
    @FXML
    private TableColumn<RelatorioDiarioEfetivo, Integer> tbColumnIdUnidade;
    @FXML
    private TableColumn<RelatorioDiarioEfetivo, String> tbColumnCipmUnidade;
    @FXML
    private Button btnAdicionarUnidade;

    /*
    PARA EDITAR AS TABS
     */
    @FXML
    private Tab tabDadosUnidade;
    @FXML
    private TextField txtIdUnidade;
    @FXML
    private TextField txtCIPMUnidade;
    @FXML
    private TextField txtCmdAreaUnidade;

    /*
    PARA EDITAR A TABELA PO
     */
    @FXML
    private TableView<Map.Entry<PO, Integer>> tablePO;
    @FXML
    private TableColumn<Map.Entry<PO, Integer>, Integer> tbColumnIdPO;
    @FXML
    private TableColumn<Map.Entry<PO, Integer>, String> tbColumnNomePO;
    @FXML
    private TableColumn<Map.Entry<PO, Integer>, String> tbColumnQtdPO;
    @FXML
    private TableColumn<Map.Entry<PO, Integer>, Integer> tbColumnQtdModuloPO;
    @FXML
    private Tab tabRelatorioEfetivo;

    /*
    PARA EDITAR A TABELA EFETIVO
     */
    @FXML
    private TableView<Efetivo> tableEfetivo;
    @FXML
    private TableColumn<Efetivo, Integer> tbColumnIdEfetivo;
    @FXML
    private TableColumn<Efetivo, String> tbColumnPrefixoEfetivo;
    @FXML
    private TableColumn<Efetivo, String> tbColumnBcsEfetivo;
    @FXML
    private TableColumn<Efetivo, String> tbColumnGpsEfetivo;
    @FXML
    private TableColumn<Efetivo, String> tbColumnHTEfetivo;
    @FXML
    private TableColumn<Efetivo, String> tbColumnAudioEfetivo;
    @FXML
    private TableColumn<Efetivo, String> tbColumnEstadoEfetivo;
    @FXML
    private Button btnAdicionaEfetivo;
    @FXML
    private Button btnEditarEfetivo;
    @FXML
    private Button btnRemoverEfetivo;

    @FXML
    private TableView<ServidorFuncao> tableGuarnicao;
    @FXML
    private TableColumn<ServidorFuncao, Integer> tbColumnIdServidor;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnNomeServidor;
    @FXML
    private TableColumn<ServidorFuncao, String> tbColumnFuncaoServidor;


    /*
    CRIA OS DAOS
     */
    private final RelatorioDiarioMesasDAO DataLoader = new RelatorioDiarioMesasDAO();
    private final RelatorioDiarioEfetivoDAO daoRelatorioDeEfetivo = new RelatorioDiarioEfetivoDAO();
    private final EfetivoDAO daoEfetivo = new EfetivoDAO();

    private List<RelatorioDiarioMesas> itens = new ArrayList<>();
    private Set<Efetivo> listaDeEfetivo = new HashSet<>();
    private Set<RelatorioDiarioEfetivo> listaRelatorioEfetivo = new HashSet<>();
    private Set<ServidorFuncao> guarnicaoSelecionada = new HashSet<>();
    private Stage dialogStage;

    private RelatorioDiarioMesas relatorioDiarioMesasSelecionado;
    private RelatorioDiarioEfetivo relatorioDiarioEfetivoSelecionado;
    private Efetivo efetivoSelecionado;
    private int indexResumo, indexUnidade, indexEfetivo;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregaDadosTabelaResumo();
    }

    public void atualizaDados() {
        itens = DataLoader.getList("FROM RelatorioDiarioMesas");
    }

    private void carregaDadosTabelaResumo() {
        tableResumo.getItems().clear();

        atualizaDados();
        
        tbColumnDiaResumo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioMesas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioMesas, String> data) {
                return new SimpleStringProperty(data.getValue().getDiaMesa());
            }
        });

        tbColumnHoraResumo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioMesas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioMesas, String> data) {
                return new SimpleStringProperty(data.getValue().getHorarioMesa());
            }
        });

        tbColumnNomeMesaResumo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioMesas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioMesas, String> data) {
                return data.getValue().getMesa().nomeProperty();
            }
        });

        tableResumo.getItems().addAll(itens);

        ContextMenu listContextMenu = new ContextMenu();
        MenuItem novaMesa = new MenuItem("Novo");
        MenuItem removerMesa = new MenuItem("Excluir");
        MenuItem editarMesa = new MenuItem("Editar");
        novaMesa.setOnAction((ActionEvent event) -> {
            clickedAdicionarMesa();
        });
        removerMesa.setOnAction((ActionEvent event) -> {
            clickedRemoverMesa();
        });
        editarMesa.setOnAction((ActionEvent event) -> {
            clickedEditarMesa();
        });

        listContextMenu.getItems().add(novaMesa);
        listContextMenu.getItems().add(removerMesa);
        listContextMenu.getItems().add(editarMesa);

        tableResumo.setContextMenu(listContextMenu);
    }

    private void carregaDadosTabelaUnidade() {
        tableUnidade.getItems().clear();
        tbColumnIdUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioEfetivo, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<RelatorioDiarioEfetivo, Integer> data) {
                return data.getValue().getUnidade().idProperty().asObject();
            }
        });

        tbColumnCipmUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioEfetivo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioEfetivo, String> data) {
                return data.getValue().getUnidade().nomeProperty();
            }
        });
        tableUnidade.getItems().addAll(listaRelatorioEfetivo);

        ContextMenu listContextMenu = new ContextMenu();
        MenuItem novaUnidade = new MenuItem("Adicionar");
        MenuItem excluirUnidade = new MenuItem("Excluir");
        novaUnidade.setOnAction((ActionEvent event) -> {
            clickedAdicionarUnidade();
        });
        excluirUnidade.setOnAction((ActionEvent event) -> {
            clickedRemoverUnidade();
        });

        listContextMenu.getItems().add(novaUnidade);
        listContextMenu.getItems().add(excluirUnidade);

        tableUnidade.setContextMenu(listContextMenu);
    }

    private void carregaTablePO(Map<PO, Integer> data) {
        tablePO.getItems().clear();
        tbColumnNomePO.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<PO, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<PO, Integer>, String> param) {
                return param.getValue().getKey().nomeProperty();
            }
        });

        tbColumnQtdPO.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<PO, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<PO, Integer>, String> param) {
                return new SimpleStringProperty(Integer.toString(param.getValue().getValue()));
            }
        });

        tablePO.getItems().addAll(data.entrySet());
    }

    private void carregaDadosTabelaEfetivo() {

        //Limpa as tabelas
        tableEfetivo.getItems().clear();
        tableGuarnicao.getItems().clear();

        if (!listaDeEfetivo.isEmpty()) {
            tableEfetivo.setDisable(false);

            /* COLUNA STATUS EFETIVO */
            tbColumnBcsEfetivo.setCellValueFactory((TableColumn.CellDataFeatures<Efetivo, String> data) -> {
                if (data.getValue().isBcs()) {
                    return new SimpleStringProperty("PERTENCE");
                } else {
                    return new SimpleStringProperty("NÃO PERTENCE");
                }
            });

            tbColumnGpsEfetivo.setCellValueFactory((TableColumn.CellDataFeatures<Efetivo, String> data) -> {
                if (data.getValue().isGps()) {
                    return new SimpleStringProperty("POSSUI");
                } else {
                    return new SimpleStringProperty("NÃO POSSUI");
                }
            });

            tbColumnAudioEfetivo.setCellValueFactory((TableColumn.CellDataFeatures<Efetivo, String> data) -> {
                if (data.getValue().isAudio()) {
                    return new SimpleStringProperty("POSSUI");
                } else {
                    return new SimpleStringProperty("NÃO POSSUI");
                }
            });

            //Adiciona os itens
            tableEfetivo.getItems().addAll(listaDeEfetivo);

            ContextMenu listContextMenu = new ContextMenu();
            MenuItem novoEfetivo = new MenuItem("Novo");
            MenuItem removerEfetivo = new MenuItem("Excluir");
            MenuItem editarEfetivo = new MenuItem("Editar");
            novoEfetivo.setOnAction((ActionEvent event) -> {
                clickedAdicionarEfetivo();
            });
            removerEfetivo.setOnAction((ActionEvent event) -> {
                clickedRemoverEfetivo();
            });
            editarEfetivo.setOnAction((ActionEvent event) -> {
                clickedEditarEfetivo();
            });

            listContextMenu.getItems().add(novoEfetivo);
            listContextMenu.getItems().add(removerEfetivo);
            listContextMenu.getItems().add(editarEfetivo);

            tableEfetivo.setContextMenu(listContextMenu);
        } else {
            btnEditarEfetivo.setDisable(true);
            btnRemoverEfetivo.setDisable(true);
            tableEfetivo.setDisable(true);
            tableGuarnicao.setDisable(true);
        }
    }

    private void carregaDadosTabelaGuarnicao() {
        btnEditarEfetivo.setDisable(false);
        btnRemoverEfetivo.setDisable(false);
        tableGuarnicao.setDisable(false);

        tableGuarnicao.getItems().clear();

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

        tableGuarnicao.getItems().addAll(guarnicaoSelecionada);
    }

    private void limparDados() {
        //TextFields
        tablePO.getItems().clear();
        tableGuarnicao.getItems().clear();
        tableEfetivo.getItems().clear();
        txtIdUnidade.clear();
        txtCIPMUnidade.clear();
        txtCmdAreaUnidade.clear();

        //Tabelas
        tableGuarnicao.setDisable(true);
        tableEfetivo.setDisable(true);
        tablePO.setDisable(true);

        //Botões
        btnEditarEfetivo.setDisable(true);
        btnAdicionaEfetivo.setDisable(true);
        btnRemoverEfetivo.setDisable(true);
    }

    @FXML
    private void clikedRowTabelaResumo() {
        relatorioDiarioMesasSelecionado = tableResumo.getSelectionModel().getSelectedItem();
        indexResumo = tableResumo.getSelectionModel().getSelectedIndex();

        if (relatorioDiarioMesasSelecionado != null) {
            //Ativa a Tabela Unidade
            tableUnidade.setDisable(false);

            //Ativa o Botão de Adicionar Unidade
            btnAdicionarUnidade.setDisable(false);

            //limpar dados antigos da unidade
            tableUnidade.getItems().clear();
            limparDados();

            listaRelatorioEfetivo = relatorioDiarioMesasSelecionado.getListaRelatorioDiarioEfetivo();
            if (listaRelatorioEfetivo != null) {
                carregaDadosTabelaUnidade();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Atenção");
                alert.setHeaderText("Não há unidade cadastradas na mesa!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void clikedRowTabelaUnidade() {

        relatorioDiarioEfetivoSelecionado = tableUnidade.getSelectionModel().getSelectedItem();
        indexUnidade = tableUnidade.getSelectionModel().getSelectedIndex();

        if (relatorioDiarioEfetivoSelecionado != null) {
            //Ativa o Paine de Abas
            painelTab.setDisable(false);

            //limpar dados antigos da unidade
            limparDados();

            //Carrega os dados da Unidade
            txtIdUnidade.setText(String.valueOf(relatorioDiarioEfetivoSelecionado.getUnidade().getId()));
            txtCIPMUnidade.setText(relatorioDiarioEfetivoSelecionado.getUnidade().getNome());
            txtCmdAreaUnidade.setText(relatorioDiarioEfetivoSelecionado.getUnidade().getComandoDeArea());

            //Ativa e carrega a tabela de POS disponiveis
            tablePO.setDisable(false);

            listaDeEfetivo = relatorioDiarioEfetivoSelecionado.getEfetivos();

            //Verifica se existem EFETIVO cadastrados na Unidade
            if (!relatorioDiarioEfetivoSelecionado.getEfetivos().isEmpty()) {
                Map<PO, Integer> listaPOsCadastrados = new HashMap<>();

                for (Efetivo efetivo : relatorioDiarioEfetivoSelecionado.getEfetivos()) {
                    listaPOsCadastrados.put(efetivo.getTipoPO(), efetivo.getGuarnicao().size());
                }

                carregaTablePO(listaPOsCadastrados);

                /**
                 *
                 * Verifica se existem EFETIVOS cadastrados na Unidade para isso
                 * FAÇO UMA BUSCA NO BANCO POR TODAS OS EFETIVOS DA UNIDADE E
                 * RESUMODIARIO SELECIONADO
                 *
                 */
                carregaDadosTabelaEfetivo();

            }
            //Ativa o botão 'Adicionar Efetivo' 
            btnAdicionaEfetivo.setDisable(false);
        }
    }

    @FXML
    public void clikedRowTabelaEfetivo() {

        efetivoSelecionado = tableEfetivo.getSelectionModel().getSelectedItem();
        indexEfetivo = tableEfetivo.getSelectionModel().getSelectedIndex();

        if (efetivoSelecionado != null) {
            guarnicaoSelecionada = efetivoSelecionado.getGuarnicao();
        }

        if (!guarnicaoSelecionada.isEmpty()) {
            //Ativa a tabela Servidor
            carregaDadosTabelaGuarnicao();
        } else {
            btnEditarEfetivo.setDisable(true);
            btnRemoverEfetivo.setDisable(true);
            tableGuarnicao.getItems().clear();
            tableGuarnicao.setDisable(true);
        }
    }

    @FXML
    private void clickedAdicionarMesa() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/TelaCadastroRelatorioMesa.fxml"));
            Parent page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Mesas");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            // Setando o cliente no Controller.
            TelaCadastroRelatorioMesaController controller = loader.getController();
            controller.setDialogStage(dialogStageAtual);

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();

            carregaDadosTabelaResumo();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedRemoverMesa() {
        RelatorioDiarioMesas relatorioDiarioMesasSelecionado = tableResumo.getSelectionModel().getSelectedItem();

        if (relatorioDiarioMesasSelecionado != null) {
            Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
            alertAntesExcluir.setTitle("Atenção!");
            alertAntesExcluir.setHeaderText("Ao excluir a mesa, todos os dados referentes a ela\n"
                    + "serão excluídos. Você deseja continuar?");
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
                DataLoader.deletar(relatorioDiarioMesasSelecionado.getId());

                Alert alertDepoisExcluir = new Alert(Alert.AlertType.INFORMATION);
                alertDepoisExcluir.setTitle("Sucesso!");
                alertDepoisExcluir.setHeaderText("Excluído com sucesso!");
                alertDepoisExcluir.showAndWait();

                //limpar dados antigos da unidade
                tableUnidade.setDisable(true);
                tableUnidade.getItems().clear();
                limparDados();

                carregaDadosTabelaResumo();
            }

        } else {
            Alert alertErroExcluir = new Alert(Alert.AlertType.ERROR);
            alertErroExcluir.setTitle("Erro ao excluir!");
            alertErroExcluir.setHeaderText("É necessário selecionar a\nmesa na tabela acima!");
            alertErroExcluir.showAndWait();
        }
    }

    @FXML
    private void clickedEditarMesa() {
        if (relatorioDiarioMesasSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/TelaCadastroRelatorioMesa.fxml"));
                Parent page = loader.load();

                Scene scene = new Scene(page);

                //Criando um Estágio de Diologo (Stage Dialog)
                Stage dialogStageAtual = new Stage();
                dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
                dialogStageAtual.setTitle("Editar Mesa");
                dialogStageAtual.setResizable(false);
                dialogStageAtual.setScene(scene);

                //Setando o cliente no Controller.
                TelaCadastroRelatorioMesaController controller = loader.getController();
                controller.setRelatorio(relatorioDiarioMesasSelecionado);
                controller.setDialogStage(dialogStageAtual);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                carregaDadosTabelaResumo();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao Editar");
            alert.setHeaderText("É necessário a seleção de uma Mesa!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedAdicionarUnidade() {
        if (relatorioDiarioMesasSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/TelaCadastroUnidade.fxml"));
                Parent page = loader.load();

                Scene scene = new Scene(page);

                //Criando um Estágio de Diologo (Stage Dialog)
                Stage dialogStageAtual = new Stage();
                dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
                dialogStageAtual.setTitle("Cadastro de Unidades");
                dialogStageAtual.setResizable(false);
                dialogStageAtual.setScene(scene);

                //Setando o cliente no Controller.
                TelaCadastroUnidadeController controller = loader.getController();
                controller.setRelatorioMesasSelecionado(relatorioDiarioMesasSelecionado);
                controller.setDialogStage(dialogStageAtual);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                carregaDadosTabelaResumo();
                tableResumo.getSelectionModel().select(indexResumo);

                clikedRowTabelaResumo();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Resumo");
            alert.setHeaderText("É necessário a seleção de uma mesa na tabela ao à direita!");
            alert.showAndWait();
        }
    }

    private void clickedRemoverUnidade() {
        if (relatorioDiarioEfetivoSelecionado != null) {
            Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
            alertAntesExcluir.setTitle("Atenção!");
            alertAntesExcluir.setHeaderText("Ao excluir a Unidade, todos os dados referentes a ela\n"
                    + "serão excluídos. Você deseja continuar?");
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
                relatorioDiarioMesasSelecionado.getListaRelatorioDiarioEfetivo().remove(relatorioDiarioEfetivoSelecionado);

                DataLoader.alterar(relatorioDiarioMesasSelecionado);

                Alert alertDepoisExcluir = new Alert(Alert.AlertType.INFORMATION);
                alertDepoisExcluir.setTitle("Sucesso!");
                alertDepoisExcluir.setHeaderText("Excluído com sucesso!");
                alertDepoisExcluir.showAndWait();

                carregaDadosTabelaResumo();
                tableResumo.getSelectionModel().select(indexResumo);

                clikedRowTabelaResumo();
            }

        } else {
            Alert alertErroExcluir = new Alert(Alert.AlertType.ERROR);
            alertErroExcluir.setTitle("Erro ao excluir!");
            alertErroExcluir.setHeaderText("É necessário selecionar a Unidade na tabela acima!");
            alertErroExcluir.showAndWait();
        }
    }

    @FXML
    private void clickedAdicionarEfetivo() {
        if (relatorioDiarioEfetivoSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/TelaAdicionaEfetivo.fxml"));
                Parent page = loader.load();

                Scene scene = new Scene(page);

                //Criando um Estágio de Diologo (Stage Dialog)
                Stage dialogStageAtual = new Stage();
                dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
                dialogStageAtual.setTitle("Cadastro de Efetivo");
                dialogStageAtual.setResizable(false);
                dialogStageAtual.setScene(scene);

                //Setando o cliente no Controller.
                TelaAdicionaEfetivoController controller = loader.getController();
                controller.setRelatorioDeMesa(relatorioDiarioMesasSelecionado);
                controller.setRelatorioDeEfetivo(relatorioDiarioEfetivoSelecionado);
                controller.setEfetivo(null);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                if (controller.isBotaoSalvarClicado()) {
                    relatorioDiarioEfetivoSelecionado.getEfetivos().add(controller.getEfetivo());
                    DataLoader.alterar(relatorioDiarioMesasSelecionado);

                    carregaDadosTabelaResumo();
                    tableResumo.getSelectionModel().select(indexResumo);

                    clikedRowTabelaResumo();
                    tableUnidade.getSelectionModel().select(indexResumo);

                    clikedRowTabelaUnidade();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Efetivo cadastrado com sucesso");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Atenção!");
                    alert.setHeaderText("Nenhum dado modificado");
                    alert.showAndWait();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro Unidade");
            alert.setHeaderText("É necessário a seleção de uma unidade na tabela à direita!");
            alert.showAndWait();
        }

    }

    @FXML
    private void clickedEditarEfetivo() {
        if (relatorioDiarioEfetivoSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/TelaAdicionaEfetivo.fxml"));
                Parent page = loader.load();

                Scene scene = new Scene(page);

                //Criando um Estágio de Diologo (Stage Dialog)
                Stage dialogStageAtual = new Stage();
                dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
                dialogStageAtual.setTitle("Editar Efetivo");
                dialogStageAtual.setResizable(false);
                dialogStageAtual.setScene(scene);

                //Setando o cliente no Controller.
                TelaAdicionaEfetivoController controller = loader.getController();
                controller.setRelatorioDeMesa(relatorioDiarioMesasSelecionado);
                controller.setRelatorioDeEfetivo(relatorioDiarioEfetivoSelecionado);
                controller.setEfetivo(efetivoSelecionado);
                controller.setStage(dialogStageAtual);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                if (controller.isBotaoSalvarClicado()) {
                    daoEfetivo.alterar(controller.getEfetivo());
                    DataLoader.alterar(relatorioDiarioMesasSelecionado);

                    carregaDadosTabelaResumo();
                    tableResumo.getSelectionModel().select(indexResumo);
                    clikedRowTabelaResumo();

                    tableUnidade.getSelectionModel().select(indexResumo);
                    clikedRowTabelaUnidade();

                    tableEfetivo.getSelectionModel().select(indexEfetivo);
                    clikedRowTabelaEfetivo();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Efetivo editado com Sucesso");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Atenção!");
                    alert.setHeaderText("Nenhum dado modificado");
                    alert.showAndWait();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao Editar");
            alert.setHeaderText("É necessário a seleção de um Efetivo!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedRemoverEfetivo() {
        if (relatorioDiarioEfetivoSelecionado != null) {
            Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
            alertAntesExcluir.setTitle("Atenção!");
            alertAntesExcluir.setHeaderText("Ao excluir o Efetivo, todos os dados referentes a ele\n"
                    + "serão excluídos. Você deseja continuar?");
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
                relatorioDiarioEfetivoSelecionado.getEfetivos().remove(efetivoSelecionado);

                DataLoader.alterar(relatorioDiarioMesasSelecionado);

                carregaDadosTabelaResumo();
                tableResumo.getSelectionModel().select(indexResumo);
                clikedRowTabelaResumo();

                tableUnidade.getSelectionModel().select(indexResumo);
                clikedRowTabelaUnidade();

                Alert alertDepoisExcluir = new Alert(Alert.AlertType.INFORMATION);
                alertDepoisExcluir.setTitle("Sucesso!");
                alertDepoisExcluir.setHeaderText("Excluído com sucesso!");
                alertDepoisExcluir.showAndWait();

                carregaDadosTabelaEfetivo();
            }

        } else {
            Alert alertErroExcluir = new Alert(Alert.AlertType.ERROR);
            alertErroExcluir.setTitle("Erro ao excluir!");
            alertErroExcluir.setHeaderText("É necessário selecionar o Efetivo na tabela acima!");
            alertErroExcluir.showAndWait();
        }
    }

    @FXML
    private void clickedOpMenuTipoPO() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/adm/TelaAdmCadastroTipoPO.fxml"));
            TabPane page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Tipo de P.O.");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaAdmCadastroTipoPOController controller = loader.getController();

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();
            carregaDadosTabelaResumo();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedOpMenuUnidade() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/adm/TelaAdmCadastroUnidades.fxml"));
            TabPane page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Unidade");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaAdmCadastroUnidadesController controller = loader.getController();

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();
            limparDados();
            carregaDadosTabelaResumo();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedOpMenuMesa(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/adm/TelaAdmCadastroMesas.fxml"));
            TabPane page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Mesas");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaAdmCadastroMesasController controller = loader.getController();

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();
            dialogStageAtual.hide();
            limparDados();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedOpMenuServidor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/adm/TelaAdmCadastroServidor.fxml"));
            TabPane page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Servidores");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaAdmCadastroServidorController controller = loader.getController();

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();
            limparDados();
            carregaDadosTabelaResumo();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedOpMenuRelatorioMesas(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro ao excecutar!");
        alert.setHeaderText("Funcionalidade não implementada!");
        alert.showAndWait();
        limparDados();
    }

    @FXML
    private void clickedOpMenuRelatorioPeriodos(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro ao excecutar!");
        alert.setHeaderText("Funcionalidade não implementada!");
        alert.showAndWait();
        limparDados();
    }

    @FXML
    private void clickedOpMenuAjudaSobre(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro ao excecutar!");
        alert.setHeaderText("Funcionalidade não implementada!");
        alert.showAndWait();
        limparDados();
    }

}
