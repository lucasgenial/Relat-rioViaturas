package com.cicom.relatorioviaturas.controllers;

import com.cicom.relatorioviaturas.DAO.RelatorioDiarioMesasDAO;
import com.cicom.relatorioviaturas.DAO.RelatorioDiarioViaturasDAO;
import com.cicom.relatorioviaturas.DAO.ViaturaDAO;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroMesasController;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroServidorController;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroTipoPOController;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroUnidadesController;
import com.cicom.relatorioviaturas.model.*;
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
    private TableView<RelatorioDiarioViaturas> tableUnidade;
    @FXML
    private TableColumn<RelatorioDiarioViaturas, Integer> tbColumnIdUnidade;
    @FXML
    private TableColumn<RelatorioDiarioViaturas, String> tbColumnCipmUnidade;
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
    private Tab tabRelatorioOperacional;

    /*
    PARA EDITAR A TABELA VIATURA
     */
    @FXML
    private TableView<Viatura> tableViatura;
    @FXML
    private TableColumn<Viatura, Integer> tbColumnIdViatura;
    @FXML
    private TableColumn<Viatura, String> tbColumnPrefixoViatura;
    @FXML
    private TableColumn<Viatura, String> tbColumnBcsViatura;
    @FXML
    private TableColumn<Viatura, String> tbColumnGpsViatura;
    @FXML
    private TableColumn<Viatura, String> tbColumnHTViatura;
    @FXML
    private TableColumn<Viatura, String> tbColumnAudioViatura;
    @FXML
    private TableColumn<Viatura, String> tbColumnEstadoViatura;
    @FXML
    private Button btnAdicionaOperacional;
    @FXML
    private Button btnEditarOperacional;
    @FXML
    private Button btnRemoverOperacional;

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
    private final RelatorioDiarioViaturasDAO daoRelatorioDeViatura = new RelatorioDiarioViaturasDAO();
    private final ViaturaDAO daoViatura = new ViaturaDAO();

    private List<RelatorioDiarioMesas> itens = new ArrayList<>();
    private Set<Viatura> listaDeViaturas = new HashSet<>();
    private Set<RelatorioDiarioViaturas> listaRelatorioViaturas = new HashSet<>();
    private Set<ServidorFuncao> guarnicaoSelecionada = new HashSet<>();
    private Stage dialogStage;

    private RelatorioDiarioMesas relatorioDiarioMesasSelecionado;
    private RelatorioDiarioViaturas relatorioDiarioViaturaSelecionado;
    private Viatura viaturaSelecionado;
    private int indexResumo, indexUnidade, indexViatura;

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
        tbColumnIdUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioViaturas, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<RelatorioDiarioViaturas, Integer> data) {
                return data.getValue().getUnidade().idProperty().asObject();
            }
        });

        tbColumnCipmUnidade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioViaturas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioViaturas, String> data) {
                return data.getValue().getUnidade().nomeProperty();
            }
        });
        tableUnidade.getItems().addAll(listaRelatorioViaturas);

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

    private void carregaDadosTabelaViatura() {

        //Limpa as tabelas
        tableViatura.getItems().clear();
        tableGuarnicao.getItems().clear();

        if (!listaDeViaturas.isEmpty()) {
            tableViatura.setDisable(false);

            /* COLUNA STATUS VIATURA */
            tbColumnBcsViatura.setCellValueFactory((TableColumn.CellDataFeatures<Viatura, String> data) -> {
                if (data.getValue().isBcs()) {
                    return new SimpleStringProperty("PERTENCE");
                } else {
                    return new SimpleStringProperty("NÃO PERTENCE");
                }
            });

            tbColumnGpsViatura.setCellValueFactory((TableColumn.CellDataFeatures<Viatura, String> data) -> {
                if (data.getValue().isGps()) {
                    return new SimpleStringProperty("POSSUI");
                } else {
                    return new SimpleStringProperty("NÃO POSSUI");
                }
            });

            tbColumnAudioViatura.setCellValueFactory((TableColumn.CellDataFeatures<Viatura, String> data) -> {
                if (data.getValue().isAudio()) {
                    return new SimpleStringProperty("POSSUI");
                } else {
                    return new SimpleStringProperty("NÃO POSSUI");
                }
            });

            //Adiciona os itens
            tableViatura.getItems().addAll(listaDeViaturas);

            ContextMenu listContextMenu = new ContextMenu();
            MenuItem novaViatura = new MenuItem("Novo");
            MenuItem removeViatura = new MenuItem("Excluir");
            MenuItem editaViatura = new MenuItem("Editar");
            novaViatura.setOnAction((ActionEvent event) -> {
                clickedAdicionarOperacional();
            });
            removeViatura.setOnAction((ActionEvent event) -> {
                clickedRemoverOperacional();
            });
            editaViatura.setOnAction((ActionEvent event) -> {
                clickedEditarOperacional();
            });

            listContextMenu.getItems().add(novaViatura);
            listContextMenu.getItems().add(removeViatura);
            listContextMenu.getItems().add(editaViatura);

            tableViatura.setContextMenu(listContextMenu);
        } else {
            btnEditarOperacional.setDisable(true);
            btnRemoverOperacional.setDisable(true);
            tableViatura.setDisable(true);
            tableGuarnicao.setDisable(true);
        }
    }

    private void carregaDadosTabelaGuarnicao() {
        btnEditarOperacional.setDisable(false);
        btnRemoverOperacional.setDisable(false);
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
        tableViatura.getItems().clear();
        txtIdUnidade.clear();
        txtCIPMUnidade.clear();
        txtCmdAreaUnidade.clear();

        //Tabelas
        tableGuarnicao.setDisable(true);
        tableViatura.setDisable(true);
        tablePO.setDisable(true);

        //Botões
        btnEditarOperacional.setDisable(true);
        btnAdicionaOperacional.setDisable(true);
        btnRemoverOperacional.setDisable(true);
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

            listaRelatorioViaturas = relatorioDiarioMesasSelecionado.getRelatorioDiarioViaturas();

            System.out.println("LISTA VIATURA: " + relatorioDiarioMesasSelecionado.getRelatorioDiarioViaturas());

            if (listaRelatorioViaturas != null) {
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

        relatorioDiarioViaturaSelecionado = tableUnidade.getSelectionModel().getSelectedItem();
        indexUnidade = tableUnidade.getSelectionModel().getSelectedIndex();

        if (relatorioDiarioViaturaSelecionado != null) {
            //Ativa o Paine de Abas
            painelTab.setDisable(false);

            //limpar dados antigos da unidade
            limparDados();

            //Carrega os dados da Unidade
            txtIdUnidade.setText(String.valueOf(relatorioDiarioViaturaSelecionado.getUnidade().getId()));
            txtCIPMUnidade.setText(relatorioDiarioViaturaSelecionado.getUnidade().getNome());
            txtCmdAreaUnidade.setText(relatorioDiarioViaturaSelecionado.getUnidade().getComandoDeArea());

            //Ativa e carrega a tabela de POS disponiveis
            tablePO.setDisable(false);

            listaDeViaturas = relatorioDiarioViaturaSelecionado.getViaturas();

            //Verifica se existem VIATURAS cadastrados na Unidade
            if (!relatorioDiarioViaturaSelecionado.getViaturas().isEmpty()) {
                Map<PO, Integer> listaPOsCadastrados = new HashMap<>();

                for (Viatura viatura : relatorioDiarioViaturaSelecionado.getViaturas()) {
                    listaPOsCadastrados.put(viatura.getTipoPO(), viatura.getGuarnicao().size());
                }

                carregaTablePO(listaPOsCadastrados);

                /**
                 *
                 * Verifica se existem VIATURAS cadastrados na Unidade para isso
                 * FAÇO UMA BUSCA NO BANCO POR TODAS AS VIATURAS DA UNIDADE E
                 * RESUMODIARIO SELECIONADO
                 *
                 */
                carregaDadosTabelaViatura();

            }
            //Ativa o botão 'Adicionar Operacional' 
            btnAdicionaOperacional.setDisable(false);
        }
    }

    @FXML
    public void clikedRowTabelaViatura() {

        viaturaSelecionado = tableViatura.getSelectionModel().getSelectedItem();
        indexViatura = tableViatura.getSelectionModel().getSelectedIndex();

        if (viaturaSelecionado != null) {
            guarnicaoSelecionada = viaturaSelecionado.getGuarnicao();
        }

        if (!guarnicaoSelecionada.isEmpty()) {
            //Ativa a tabela Servidor
            carregaDadosTabelaGuarnicao();
        } else {
            btnEditarOperacional.setDisable(true);
            btnRemoverOperacional.setDisable(true);
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
        if (relatorioDiarioViaturaSelecionado != null) {
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
                relatorioDiarioMesasSelecionado.getRelatorioDiarioViaturas().remove(relatorioDiarioViaturaSelecionado);

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
    private void clickedAdicionarOperacional() {
        if (relatorioDiarioViaturaSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/TelaAdicionaOperacional.fxml"));
                Parent page = loader.load();

                Scene scene = new Scene(page);

                //Criando um Estágio de Diologo (Stage Dialog)
                Stage dialogStageAtual = new Stage();
                dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
                dialogStageAtual.setTitle("Cadastro de Operacional");
                dialogStageAtual.setResizable(false);
                dialogStageAtual.setScene(scene);

                //Setando o cliente no Controller.
                TelaAdicionaOperacionalController controller = loader.getController();
                controller.setRelatorioDeMesa(relatorioDiarioMesasSelecionado);
                controller.setRelatorioDeViatura(relatorioDiarioViaturaSelecionado);
                controller.setViatura(null);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                if (controller.isBotaoSalvarClicado()) {
                    relatorioDiarioViaturaSelecionado.getViaturas().add(controller.getViatura());
                    DataLoader.alterar(relatorioDiarioMesasSelecionado);

                    carregaDadosTabelaResumo();
                    tableResumo.getSelectionModel().select(indexResumo);

                    clikedRowTabelaResumo();
                    tableUnidade.getSelectionModel().select(indexResumo);

                    clikedRowTabelaUnidade();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Operacional editado com Sucesso");
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
    private void clickedEditarOperacional() {
        if (relatorioDiarioViaturaSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/TelaAdicionaOperacional.fxml"));
                Parent page = loader.load();

                Scene scene = new Scene(page);

                //Criando um Estágio de Diologo (Stage Dialog)
                Stage dialogStageAtual = new Stage();
                dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
                dialogStageAtual.setTitle("Editar Operacional");
                dialogStageAtual.setResizable(false);
                dialogStageAtual.setScene(scene);
                System.out.println("VIATURA ANTES " + viaturaSelecionado);

                //Setando o cliente no Controller.
                TelaAdicionaOperacionalController controller = loader.getController();
                controller.setRelatorioDeMesa(relatorioDiarioMesasSelecionado);
                controller.setRelatorioDeViatura(relatorioDiarioViaturaSelecionado);
                controller.setViatura(viaturaSelecionado);
                controller.setStage(dialogStageAtual);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                if (controller.isBotaoSalvarClicado()) {
                    daoViatura.alterar(controller.getViatura());
                    DataLoader.alterar(relatorioDiarioMesasSelecionado);

                    carregaDadosTabelaResumo();
                    tableResumo.getSelectionModel().select(indexResumo);
                    clikedRowTabelaResumo();

                    tableUnidade.getSelectionModel().select(indexResumo);
                    clikedRowTabelaUnidade();

                    tableViatura.getSelectionModel().select(indexViatura);
                    clikedRowTabelaViatura();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Operacional editado com Sucesso");
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
            alert.setHeaderText("É necessário a seleção de um Operacional!");
            alert.showAndWait();
        }
    }

    @FXML
    private void clickedRemoverOperacional() {
        if (relatorioDiarioViaturaSelecionado != null) {
            Alert alertAntesExcluir = new Alert(Alert.AlertType.CONFIRMATION);
            alertAntesExcluir.setTitle("Atenção!");
            alertAntesExcluir.setHeaderText("Ao excluir o Operacional, todos os dados referentes a ele\n"
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
                relatorioDiarioViaturaSelecionado.getViaturas().remove(viaturaSelecionado);

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

                carregaDadosTabelaViatura();
            }

        } else {
            Alert alertErroExcluir = new Alert(Alert.AlertType.ERROR);
            alertErroExcluir.setTitle("Erro ao excluir!");
            alertErroExcluir.setHeaderText("É necessário selecionar o Operacional na tabela acima!");
            alertErroExcluir.showAndWait();
        }
    }

    @FXML
    private void clickedOpMenuTipoPO() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/adm/TelaAdmCadastroTipoPO.fxml"));
            AnchorPane page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Tipo de P.O.");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaAdmCadastroTipoPOController controller = loader.getController();
            controller.setDialogStage(dialogStageAtual);

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
            AnchorPane page = loader.load();

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

//    public static void refresh(final TableView<> table, final Set<T> tableList) {
//        //Wierd JavaFX bug 
//        table.setItems(null);
//        table.layout();
//        table.setItems(FXCollections.observableSet(tableList));
//    }
}
