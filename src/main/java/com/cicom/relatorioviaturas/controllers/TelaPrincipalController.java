package com.cicom.relatorioviaturas.controllers;

import com.cicom.relatorioviaturas.DAO.RelatorioDiarioMesasDAO;
import com.cicom.relatorioviaturas.DAO.ViaturaDAO;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroMesasController;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroServidorController;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroTipoPOController;
import com.cicom.relatorioviaturas.controllers.adm.TelaAdmCadastroUnidadesController;
import com.cicom.relatorioviaturas.model.*;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
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
    private TableColumn<RelatorioDiarioMesas, String> tbColumnTurnoResumo;
    @FXML
    private TableColumn<RelatorioDiarioMesas, String> tbColumnNomeMesaResumo;
    @FXML
    private TableColumn<RelatorioDiarioMesas, String> tbColumnSupervisorResumo;
    @FXML
    private TableColumn<RelatorioDiarioMesas, String> tbColumnNomeOperadorResumo;

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
    private final ViaturaDAO daoViatura = new ViaturaDAO();
    private Stage dialogStage;
    private RelatorioDiarioMesas relatorioDiarioMesasSelecionado;
    private RelatorioDiarioViaturas relatorioViaturasSelecionado;
    private Viatura viaturaSelecionada;
    private int i = 0;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregaDadosTabelaResumo();
        tabDadosUnidade.closableProperty();
    }

    private void carregaDadosTabelaResumo() {
        List<RelatorioDiarioMesas> itens = DataLoader.getList("FROM RelatorioDiarioMesas");

        tbColumnDiaResumo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioMesas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioMesas, String> data) {
                return new SimpleStringProperty(data.getValue().getDia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        });

        tbColumnNomeMesaResumo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioMesas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioMesas, String> data) {
                return data.getValue().getMesa().nomeProperty();
            }
        });

        tbColumnSupervisorResumo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioMesas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioMesas, String> param) {
                return param.getValue().getSupervisor().getServidor().nomeProperty();
            }

        });

        /* COLUNA NOME OPERADOR */
        tbColumnNomeOperadorResumo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RelatorioDiarioMesas, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RelatorioDiarioMesas, String> param) {
                return param.getValue().getOperador().getServidor().nomeProperty();
            }
        });

        tableResumo.getItems().setAll(FXCollections.observableList(itens));

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
    private void clikedRowtableResumo() {
        relatorioDiarioMesasSelecionado = tableResumo.getSelectionModel().getSelectedItem();

        if (relatorioDiarioMesasSelecionado != null) {
            //Ativa a Tabela Unidade
            tableUnidade.setDisable(false);

            //Ativa o Botão de Adicionar Unidade
            btnAdicionarUnidade.setDisable(false);

            //limpar dados antigos da unidade
            limparDados();

            Set<RelatorioDiarioViaturas> relatorioDeViaturas = tableResumo.getSelectionModel().getSelectedItem().getRelatorioDiarioViaturas();

            if (relatorioDeViaturas != null) {

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
                tableUnidade.getItems().setAll(FXCollections.observableSet(relatorioDeViaturas));

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

        relatorioViaturasSelecionado = tableUnidade.getSelectionModel().getSelectedItem();
        Unidade unidadeSelecionada = null;

        if (relatorioViaturasSelecionado != null) {
            unidadeSelecionada = relatorioViaturasSelecionado.getUnidade();
        }

        if (relatorioViaturasSelecionado != null) {
//        if (unidadeSelecionada != null) {
            //Ativa o Paine de Abas
            painelTab.setDisable(false);

            //limpar dados antigos da unidade
            limparDados();

            //Carrega os dados da Unidade
            txtIdUnidade.setText(String.valueOf(relatorioViaturasSelecionado.getUnidade().getId()));
            txtCIPMUnidade.setText(relatorioViaturasSelecionado.getUnidade().getNome());
            txtCmdAreaUnidade.setText(relatorioViaturasSelecionado.getUnidade().getComandoDeArea());

            //Ativa e carrega a tabela de POS disponiveis
            tablePO.setDisable(false);

            //Verifica se existem VIATURAS cadastrados na Unidade
            if (!relatorioViaturasSelecionado.getViaturas().isEmpty()) {
                //Verifica quais os POs tem cadastrados já neste relatório
//                List<PO> listaPOsCadastrados = new ArrayList<>();
//                String[][] listaPOsCadastrados = new String();
                Map<PO, Integer> listaPOsCadastrados = new HashMap<>();

                for (Viatura viatura : relatorioViaturasSelecionado.getViaturas()) {
                    listaPOsCadastrados.put(viatura.getTipoPO(), viatura.getGuarnicao().size());
                }

//                TableColumn<Map.Entry<String, String>, String> column1 = new TableColumn<>("Key");
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

                tablePO.getItems().setAll(FXCollections.observableArrayList(listaPOsCadastrados.entrySet()));

            }
            //Ativa o botão 'Adicionar Operacional' 
            btnAdicionaOperacional.setDisable(false);

            /*
            Verifica se existem VIATURAS cadastrados na Unidade para isso
            FAÇO UMA BUSCA NO BANCO POR TODAS AS VIATURAS DA UNIDADE E RESUMODIARIO SELECIONADO
             */
            carregaDadosTabelaViatura();
        }
    }

    @FXML
    public void clikedRowTabelaViatura() {
        Set<ServidorFuncao> guarnicaoSelecionada = new HashSet<>();

        if (tableViatura.getSelectionModel().getSelectedItem() != null) {
            guarnicaoSelecionada = tableViatura.getSelectionModel().getSelectedItem().getGuarnicao();
        } else {
            guarnicaoSelecionada.clear();
        }

        if (!guarnicaoSelecionada.isEmpty()) {
            //Ativa a tabela Servidor
            btnEditarOperacional.setDisable(false);
            btnRemoverOperacional.setDisable(false);
            tableGuarnicao.setDisable(false);

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

            tableGuarnicao.getItems().setAll(FXCollections.observableSet(guarnicaoSelecionada));
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
            loader.setLocation(getClass().getResource("/fxml/TelaCadastroMesa.fxml"));
            Parent page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Mesas");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            // Setando o cliente no Controller.
            TelaCadastroMesaController controller = loader.getController();
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
        relatorioDiarioMesasSelecionado = tableResumo.getSelectionModel().getSelectedItem();

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
        relatorioDiarioMesasSelecionado = tableResumo.getSelectionModel().getSelectedItem();

        if (relatorioDiarioMesasSelecionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/TelaCadastroMesa.fxml"));
                Parent page = loader.load();

                Scene scene = new Scene(page);

                //Criando um Estágio de Diologo (Stage Dialog)
                Stage dialogStageAtual = new Stage();
                dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
                dialogStageAtual.setTitle("Editar Mesa");
                dialogStageAtual.setResizable(false);
                dialogStageAtual.setScene(scene);

                //Setando o cliente no Controller.
                TelaCadastroMesaController controller = loader.getController();
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
        Mesa mesaSelecionada = tableResumo.getSelectionModel().getSelectedItem().getMesa();

        if (mesaSelecionada != null) {
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

                clikedRowtableResumo();

                carregaDadosTabelaResumo();
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
        relatorioViaturasSelecionado = tableUnidade.getSelectionModel().getSelectedItem();

        if (relatorioViaturasSelecionado != null) {
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
                relatorioDiarioMesasSelecionado.getRelatorioDiarioViaturas().remove(relatorioViaturasSelecionado);

                DataLoader.alterar(relatorioDiarioMesasSelecionado);

                Alert alertDepoisExcluir = new Alert(Alert.AlertType.INFORMATION);
                alertDepoisExcluir.setTitle("Sucesso!");
                alertDepoisExcluir.setHeaderText("Excluído com sucesso!");
                alertDepoisExcluir.showAndWait();

                //limpar dados antigos da unidade
                limparDados();

                clikedRowtableResumo();
            }

        } else {
            Alert alertErroExcluir = new Alert(Alert.AlertType.ERROR);
            alertErroExcluir.setTitle("Erro ao excluir!");
            alertErroExcluir.setHeaderText("É necessário selecionar a Unidade na tabela acima!");
            alertErroExcluir.showAndWait();
        }
    }

    private void carregaDadosTabelaViatura() {
        //Limpa as tabelas
        tableViatura.getItems().clear();
        tableGuarnicao.getItems().clear();

        if (!relatorioViaturasSelecionado.getViaturas().isEmpty()) {
            tableViatura.setDisable(false);

            List<Viatura> listaDeViaturas = relatorioViaturasSelecionado.getViaturas();
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
            tableViatura.getItems().setAll(FXCollections.observableList(listaDeViaturas));

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

    @FXML
    private void clickedAdicionarOperacional() {
        relatorioViaturasSelecionado = tableUnidade.getSelectionModel().getSelectedItem();

        if (relatorioViaturasSelecionado != null) {
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
                controller.setRelatorioDiarioViaturasSelecionado(relatorioViaturasSelecionado);
                controller.setDialogStage(dialogStageAtual);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                if (controller.getViatura() != null) {
                    relatorioViaturasSelecionado.getViaturas().add(controller.getViatura());
                    DataLoader.alterar(relatorioDiarioMesasSelecionado);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Operacional cadastrado com Sucesso");
                    alert.showAndWait();
                }

                carregaDadosTabelaViatura();
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
        viaturaSelecionada = tableViatura.getSelectionModel().getSelectedItem();

        if (viaturaSelecionada != null) {
            Set<ServidorFuncao> lista = viaturaSelecionada.getGuarnicao();
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

                //Setando o cliente no Controller.
                TelaAdicionaOperacionalController controller = loader.getController();
                controller.setRelatorioDiarioViaturasSelecionado(relatorioViaturasSelecionado);
                controller.setViatura(viaturaSelecionada);
                controller.setDialogStage(dialogStageAtual);

                //Mostra a tela ate que o usuario feche
                dialogStageAtual.showAndWait();

                if (!controller.getViatura().equals(viaturaSelecionada)) {
                    //faz a alteração no banco
                    DataLoader.alterar(relatorioDiarioMesasSelecionado);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso!");
                    alert.setHeaderText("Operacional editado com Sucesso");
                    alert.showAndWait();
                } else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Atenção!");
                    alert.setHeaderText("Nenhum dado modificado");
                    alert.showAndWait();
                }

                carregaDadosTabelaViatura();
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
        viaturaSelecionada = tableViatura.getSelectionModel().getSelectedItem();

        if (viaturaSelecionada != null) {
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
                relatorioViaturasSelecionado.getViaturas().remove(viaturaSelecionada);

                DataLoader.alterar(relatorioDiarioMesasSelecionado);

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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedOpMenuMesa(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/adm/TelaAdmCadastroMesas.fxml"));
            AnchorPane page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Mesas");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaAdmCadastroMesasController controller = loader.getController();
//            controller.setDialogStage(dialogStageAtual);

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clickedOpMenuServidor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/adm/TelaAdmCadastroServidor.fxml"));
            AnchorPane page = loader.load();

            Scene scene = new Scene(page);

            //Criando um Estágio de Diologo (Stage Dialog)
            Stage dialogStageAtual = new Stage();
            dialogStageAtual.initModality(Modality.APPLICATION_MODAL);
            dialogStageAtual.setTitle("Cadastro de Unidade");
            dialogStageAtual.setResizable(false);
            dialogStageAtual.setScene(scene);

            //Setando o cliente no Controller.
            TelaAdmCadastroServidorController controller = loader.getController();
//            controller.setDialogStage(dialogStageAtual);

            //Mostra a tela ate que o usuario feche
            dialogStageAtual.showAndWait();
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
    }

    @FXML
    private void clickedOpMenuRelatorioPeriodos(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro ao excecutar!");
        alert.setHeaderText("Funcionalidade não implementada!");
        alert.showAndWait();
    }

    @FXML
    private void clickedOpMenuAjudaSobre(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro ao excecutar!");
        alert.setHeaderText("Funcionalidade não implementada!");
        alert.showAndWait();
    }
}
