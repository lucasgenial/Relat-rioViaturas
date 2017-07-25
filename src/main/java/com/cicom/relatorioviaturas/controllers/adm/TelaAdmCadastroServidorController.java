package com.cicom.relatorioviaturas.controllers.adm;

import com.cicom.relatorioviaturas.DAO.ServidorDAO;
import com.cicom.relatorioviaturas.model.Servidor;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Lucas Matos
 */
public class TelaAdmCadastroServidorController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtMatricula;
    @FXML
    private TextField txtNomeCompleto;
    @FXML
    private TextField txtNomeGuerra;
    @FXML
    private TextField txtHierarquia;
    @FXML
    private TableView<Servidor> tableServidor;
    @FXML
    private TableColumn<Servidor, Integer> tbColumnId;
    @FXML
    private TableColumn<Servidor, String> tbColumnMatricula;
    @FXML
    private TableColumn<Servidor, String> tbColumnNomeCompleto;
    @FXML
    private TableColumn<Servidor, String> tbColumnNomeGuerra;
    @FXML
    private TableColumn<Servidor, String> tbColumnHierarquia;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnExcluir;

    /*
    EXTRA
     */
    private ServidorDAO daoServidor = new ServidorDAO();
    private List<Servidor> listaDeServidor;
    private boolean editar = false;
    private String tituloMensagem;
    private String corpoMensagem;
    private Servidor servidorSelecionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregaDados();
    }

    @FXML
    private void clickedBtnExcluir() {
        servidorSelecionado = (Servidor) tableServidor.getSelectionModel().getSelectedItem();

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
                daoServidor.deletar(servidorSelecionado);

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
    private void clickedBtnNovo() {
        btnSalvar.setText("Salvar");
        txtMatricula.setText("");
        txtNomeCompleto.setText("");
        txtNomeGuerra.setText("");
        txtHierarquia.setText("");

        txtMatricula.setDisable(false);
        txtNomeCompleto.setDisable(false);
        txtNomeGuerra.setDisable(false);
        txtHierarquia.setDisable(false);
        btnSalvar.setDisable(false);
        editar = false;
        servidorSelecionado = null;
    }

    @FXML
    private void clickedTableServidor() {
        servidorSelecionado = (Servidor) tableServidor.getSelectionModel().getSelectedItem();
        txtMatricula.setText(servidorSelecionado.getMatricula());
        txtNomeCompleto.setText(servidorSelecionado.getNome());
        txtNomeGuerra.setText(servidorSelecionado.getNomeGuerra());
        txtHierarquia.setText(servidorSelecionado.getHierarquia());

        btnSalvar.setText("Editar");
        btnSalvar.setDisable(false);

        //Habilita o disable
        txtMatricula.setDisable(true);
        txtNomeCompleto.setDisable(true);
        txtNomeGuerra.setDisable(true);
        txtHierarquia.setDisable(true);

        editar = false;
    }

    @FXML
    private void clickedBtnSalvar() {
        //Ver a ação no botão editar
        if (btnSalvar.getText().equals("Editar")) {
            txtMatricula.setDisable(false);
            txtNomeCompleto.setDisable(false);
            txtNomeGuerra.setDisable(false);
            txtHierarquia.setDisable(false);
            btnSalvar.setDisable(false);
            btnSalvar.setText("Salvar");

            editar = true;

            //Se o botão salvar for ativado e o editar for verdadeiro
            //Estamos modificando um Tipo P.O. Já existente
        } else if (btnSalvar.getText().equals("Salvar") && editar) {
            if (verificaDados()) {
                //Altera o nome do PO
                servidorSelecionado.setMatricula(txtMatricula.getText());
                servidorSelecionado.setNome(txtNomeCompleto.getText());
                servidorSelecionado.setNomeGuerra(txtNomeGuerra.getText());
                servidorSelecionado.setHierarquia(txtHierarquia.getText());

                daoServidor.alterar(servidorSelecionado);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Servidor alterado com sucesso!");
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
            //Altera o nome do PO
            servidorSelecionado = new Servidor();
            servidorSelecionado.setMatricula(txtMatricula.getText());
            servidorSelecionado.setNome(txtNomeCompleto.getText());
            servidorSelecionado.setNomeGuerra(txtNomeGuerra.getText());
            servidorSelecionado.setHierarquia(txtHierarquia.getText());

            if (verificaDados()) {
                daoServidor.salvar(servidorSelecionado);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Novo Servidor cadastrado!");
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

    private void carregaDados() {
        listaDeServidor = daoServidor.getListAtivos();

        txtMatricula.setDisable(true);
        txtNomeCompleto.setDisable(true);
        txtNomeGuerra.setDisable(true);
        txtHierarquia.setDisable(true);
        btnSalvar.setDisable(true);
        btnSalvar.setText("Editar");

        if (!listaDeServidor.isEmpty()) {
            tableServidor.getItems().setAll(FXCollections.observableList(listaDeServidor));
        }
    }

    private boolean verificaDados() {
        if (txtMatricula.getText().isEmpty()) {
            tituloMensagem = "Erro Matricula";
            corpoMensagem = "A matricula é obrigatória!";
            txtMatricula.setFocusTraversable(true);
            return false;
        }

        if (txtNomeCompleto.getText().isEmpty()) {
            tituloMensagem = "Erro Nome Completo";
            corpoMensagem = "O Nome completo é obrigatório!";
            txtNomeCompleto.setFocusTraversable(true);
            return false;
        }

        if (txtNomeGuerra.getText().isEmpty()) {
            tituloMensagem = "Erro Nome de Guerra";
            corpoMensagem = "O Nome de Guerra é obrigatório!";
            txtNomeGuerra.setFocusTraversable(true);
            return false;
        }

        if (txtHierarquia.getText().isEmpty()) {
            tituloMensagem = "Erro Hierarquia";
            corpoMensagem = "O grau Hierarquico é obrigatório!";
            txtHierarquia.setFocusTraversable(true);
            return false;
        }

        if (daoServidor.buscaPorMatricula(servidorSelecionado.getMatricula()) != null && !editar) {
            tituloMensagem = "Erro Matricula";
            corpoMensagem = "Matricula inválida, por já está em uso!";
            txtMatricula.setFocusTraversable(true);
            return false;
        }

        return true;
    }
}
