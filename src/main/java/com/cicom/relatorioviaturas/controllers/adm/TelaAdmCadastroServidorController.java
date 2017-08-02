/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cicom.relatorioviaturas.controllers.adm;

import com.cicom.relatorioviaturas.DAO.ServidorDAO;
import com.cicom.relatorioviaturas.model.Instituicao;
import com.cicom.relatorioviaturas.model.Servidor;
import com.cicom.relatorioviaturas.model.Sexo;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author estatistica
 */
public class TelaAdmCadastroServidorController implements Initializable {

    @FXML
    private Tab tabCadastro;
    @FXML
    private TextField txtMatricula;
    @FXML
    private ComboBox<Instituicao> cbOrgao;
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
    private ComboBox<Instituicao> cbOrgaoBusca;
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

    /*
    EXTRA
     */
    private ServidorDAO daoServidor = new ServidorDAO();
    private List<Servidor> listaDeServidor;
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
    }

    private void carregaDados() {
        listaDeServidor = daoServidor.getListAtivos();

        if (!listaDeServidor.isEmpty()) {
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

            tableListaServidores.getItems().setAll(FXCollections.observableList(listaDeServidor));
        }
    }

    @FXML
    private void clickedAdicionarFoto(MouseEvent event) {
    }

    @FXML
    private void clickedCadastrarServidor(MouseEvent event) throws IOException {

        if (btnCadastrar.getText().equals("EDITAR")) {
            //Habilita os campos
            txtMatricula.setDisable(true);
            cbOrgao.setDisable(true);
            txtGrauHierarquico.setDisable(true);
            cbSexo.setDisable(true);
            txtNome.setDisable(true);
            txtObservacao.setDisable(true);
            btnAdicionaFoto.setDisable(true);

            btnCadastrar.setText("SALVAR");
            //Um novo Servidor
        } else if (btnCadastrar.getText().equals("CADASTRAR")) {
            if (verificaDados()) {
                novoServidor = new Servidor();

                novoServidor.setMatricula(txtMatricula.getText());
                novoServidor.setInstituicao(cbOrgao.getSelectionModel().getSelectedItem());
                novoServidor.setGrauHierarquico(txtGrauHierarquico.getText());
                novoServidor.setSexo(cbSexo.getSelectionModel().getSelectedItem());
                novoServidor.setNome(txtNome.getText());
                novoServidor.setObservacao(txtObservacao.getText());

                //Converte a imagem
                if (imagemFoto.getImage() != null) {
                    BufferedImage bImage = SwingFXUtils.fromFXImage(imagemFoto.getImage(), null);
                    ByteArrayOutputStream s = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", s);
                    byte[] res = s.toByteArray();
                    s.close();
                    novoServidor.setFoto(res);
                }
                novoServidor.setAtivo(true);

                daoServidor.salvar(novoServidor);

                novoServidor = null;
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Servidor cadastrado com sucesso!");
                alert.showAndWait();
                carregaDados();

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
                servidorSelecionado.setInstituicao(cbOrgao.getSelectionModel().getSelectedItem());
                servidorSelecionado.setGrauHierarquico(txtGrauHierarquico.getText());
                servidorSelecionado.setSexo(cbSexo.getSelectionModel().getSelectedItem());
                servidorSelecionado.setNome(txtNome.getText());
                servidorSelecionado.setObservacao(txtObservacao.getText());

                //Converte a imagem
                if (imagemFoto.getImage() != null) {
                    BufferedImage bImage = SwingFXUtils.fromFXImage(imagemFoto.getImage(), null);
                    ByteArrayOutputStream s = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", s);
                    byte[] res = s.toByteArray();
                    s.close();
                    novoServidor.setFoto(res);
                }
                servidorSelecionado.setAtivo(true);

                daoServidor.salvar(servidorSelecionado);

                servidorSelecionado = null;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso!");
                alert.setHeaderText("Servidor modificado com sucesso!");
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

    @FXML
    private void clickedExcluirServidor(MouseEvent event) {
    }

    @FXML
    private void clickedVoltar(MouseEvent event) {
    }

    private boolean verificaDados() {
        if (txtMatricula.getText().isEmpty()) {
            tituloMensagem = "Erro Matricula";
            corpoMensagem = "A matricula é obrigatória!";
            return false;
        }

        if (cbOrgao.getSelectionModel().getSelectedItem() == null) {
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

        if (daoServidor.buscaPorMatricula(servidorSelecionado.getMatricula()) != null && !editar) {
            tituloMensagem = "Erro Matricula";
            corpoMensagem = "Matricula inválida, por já está em uso!";
            return false;
        }

        return true;
    }

}
