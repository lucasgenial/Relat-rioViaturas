/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cicom.relatorioviaturas.controller.adm;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Lucas Matos
 */
public class TelaAdmCadastroServidorController implements Initializable {

    @FXML
    private TabPane root;
    @FXML
    private Tab tabCadastro;
    @FXML
    private TextField txtMatricula;
    @FXML
    private ComboBox<?> cbInstituicao;
    @FXML
    private TextField txtGrauHierarquico;
    @FXML
    private ComboBox<?> cbSexo;
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
    private ComboBox<?> cbOrgaoBusca;
    @FXML
    private TableView<?> tableListaServidores;
    @FXML
    private TableColumn<?, ?> cbColumnId;
    @FXML
    private TableColumn<?, ?> tbColumnMatricula;
    @FXML
    private TableColumn<?, ?> cbColumnGrauHierarquico;
    @FXML
    private TableColumn<?, ?> cbColumnNome;
    @FXML
    private Button btnVerDetalhesServidor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickedAdicionarFoto(MouseEvent event) {
    }

    @FXML
    private void clickedCadastrarServidor(MouseEvent event) {
    }

    @FXML
    private void clickedExcluirServidor(MouseEvent event) {
    }

    @FXML
    private void clickedVoltar(MouseEvent event) {
    }

    @FXML
    private void onTabCadastro(Event event) {
    }

    @FXML
    private void onTabListagem(Event event) {
    }
    
}
