package com.cicom.relatorioefetivos;

import com.cicom.relatorioefetivos.DAO.FuncaoDAO;
import com.cicom.relatorioefetivos.DAO.InstituicaoDAO;
import com.cicom.relatorioefetivos.DAO.MesaDAO;
import com.cicom.relatorioefetivos.DAO.PoDAO;
import com.cicom.relatorioefetivos.DAO.ServidorDAO;
import com.cicom.relatorioefetivos.DAO.TipoMesaDAO;
import com.cicom.relatorioefetivos.DAO.UnidadeDAO;
import com.cicom.relatorioefetivos.model.Caracteristica;
import com.cicom.relatorioefetivos.model.Funcao;
import com.cicom.relatorioefetivos.model.Funcionalidade;
import com.cicom.relatorioefetivos.model.Instituicao;
import com.cicom.relatorioefetivos.model.Mesa;
import com.cicom.relatorioefetivos.model.PO;
import com.cicom.relatorioefetivos.model.Servidor;
import com.cicom.relatorioefetivos.model.Sexo;
import com.cicom.relatorioefetivos.model.TipoMesa;
import com.cicom.relatorioefetivos.model.Unidade;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

//        lancarDados();
        setUserAgentStylesheet(STYLESHEET_MODENA);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SplashScreen.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void lancarDados() {
        
        Set<Funcionalidade> funcionalidades = new HashSet<>();
        
        funcionalidades.add(new Funcionalidade("GPS",true));
        funcionalidades.add(new Funcionalidade("Audio",true));
        funcionalidades.add(new Funcionalidade("Camera",true));
        
        //PO
        PoDAO daoPO = new PoDAO();
        Set<PO> listaDePOs = new HashSet<>();
        listaDePOs.add(new PO("A PÉ", funcionalidades, new Caracteristica("A pé", true), true));
        listaDePOs.add(new PO("VIATURA 4 RODAS", funcionalidades, new Caracteristica("Motorizado", true),  true));
        listaDePOs.add(new PO("VIATURA 2 RODAS", funcionalidades, new Caracteristica("Montado", true),  true));
        listaDePOs.add(new PO("BARCO", funcionalidades, new Caracteristica("Marinho", true),  true));
        listaDePOs.add(new PO("DRONE", null, new Caracteristica("Aéreo", true), true));
        listaDePOs.add(new PO("MÓDULO", null, new Caracteristica("Posto Fixo", true), true));

        for (PO item : listaDePOs) {
            daoPO.salvar(item);
        }
        
        //FUNCAO
        Funcao func1 = new Funcao("Motorista", true);
        Funcao func2 = new Funcao("Patrulheiro", true);
        Funcao func3 = new Funcao("Supervisor", true);
        Funcao func4 = new Funcao("Operador", true);
        Funcao func5 = new Funcao("Comandante", true);

        FuncaoDAO daoFuncao = new FuncaoDAO();
        daoFuncao.salvar(func1);
        daoFuncao.salvar(func2);
        daoFuncao.salvar(func3);
        daoFuncao.salvar(func4);
        daoFuncao.salvar(func5);
        
        InstituicaoDAO daoInstituicao = new InstituicaoDAO();

        Instituicao inst1 = new Instituicao("PM", true);
        Instituicao inst2 = new Instituicao("BPM", true);
        Instituicao inst3 = new Instituicao("PC", true);
        Instituicao inst4 = new Instituicao("DPT", true);

        daoInstituicao.salvar(inst1);
        daoInstituicao.salvar(inst2);
        daoInstituicao.salvar(inst3);
        daoInstituicao.salvar(inst4);

//       CADASTRA COMANDANTE
        ServidorDAO daoServidor = new ServidorDAO();
        daoServidor.getListAtivos();
        
        Sexo sex1 = Sexo.Masculino;
        Sexo sex2 = Sexo.Feminino;
        
//        public Servidor(String nome, String matricula, Instituicao instituicao, String grauHierarquico, Sexo sexo, String observacao, Boolean ativo);
        Servidor ser1 = new Servidor("SERGIO ANTONIO", "15975", inst1, "SOLDADO", sex1, "INSERIDO NO MAIN", true);
        Servidor ser2 = new Servidor("PEDRO ORLANDO SARDA FILHO", "342432", inst1, "SOLDADO", sex1, "INSERIDO NO MAIN", true);
        Servidor ser3 = new Servidor("ANNA KARINA DO NASCIMENTO BONATO", "89492", inst1, "TENENTE", sex2, "INSERIDO NO MAIN", true);
        Servidor ser4 = new Servidor("MONICA GONCALVES PETRY", "8748", inst1, "TENENTE", sex2, "INSERIDO NO MAIN", true);
        Servidor ser5 = new Servidor("LEANDRO LEONIDAS DE BRITO", "42421", inst2, "TENENTE", sex1, "INSERIDO NO MAIN", true);
        Servidor ser6 = new Servidor("JULIANA MIRANDA MARTINS", "82719", inst2, "CABO", sex2, "INSERIDO NO MAIN", true);
        Servidor ser7 = new Servidor("TAMARA PADILHA DE SOUZA", "552", inst2, "CABO", sex2, "INSERIDO NO MAIN", true);
        Servidor ser8 = new Servidor("RAIMIR PEREIRA DA SILVA", "653", inst2, "SOLDADO", sex1, "INSERIDO NO MAIN", true);
        Servidor ser9 = new Servidor("MAYKON LUIZ ZORZAN DE LIMA", "654", inst2, "SOLDADO", sex1, "INSERIDO NO MAIN", true);
        Servidor ser10 = new Servidor("LUCAS INOCENCIO DOS SANTOS", "655", inst2, "SOLDADO", sex1, "INSERIDO NO MAIN", true);
        Servidor ser11 = new Servidor("ARILSON ZARELLI CUSTODIO DA SILVA", "656", inst2, "SOLDADO", sex1, "INSERIDO NO MAIN", true);
        Servidor ser12 = new Servidor("IRWING CESAR BONDAR", "666", inst2, "SOLDADO", sex1, "INSERIDO NO MAIN", true);

        daoServidor.salvar(ser1);
        daoServidor.salvar(ser2);
        daoServidor.salvar(ser3);
        daoServidor.salvar(ser4);
        daoServidor.salvar(ser5);
        daoServidor.salvar(ser6);
        daoServidor.salvar(ser7);
        daoServidor.salvar(ser8);
        daoServidor.salvar(ser9);
        daoServidor.salvar(ser10);
        daoServidor.salvar(ser11);
        daoServidor.salvar(ser12);

//        //Unidade
        Unidade und1 = new Unidade("POLICIA MILITAR", "8º BPM", true);
        Unidade und2 = new Unidade("POLICIA MILITAR", "7º CIPM", true);
        Unidade und3 = new Unidade("POLICIA MILITAR", "CIPPA", true);
        Unidade und4 = new Unidade("POLICIA MILITAR", "5º PEL/CIPRV - ITABUNA", true);
        Unidade und5 = new Unidade("POLICIA MILITAR", "CIPT/SUL", true);
        Unidade und6 = new Unidade("CORPO DE BOMBEIROS MILITAR", "6º GBM", true);
        Unidade und7 = new Unidade("DPT", "24º CRPT", true);
        Unidade und8 = new Unidade("DELEGACIA", "23º COORPIN", true);

        und1.setPos(listaDePOs);
        und2.setPos(listaDePOs);
        und3.setPos(listaDePOs);

        UnidadeDAO daoUnidade = new UnidadeDAO();
        daoUnidade.salvar(und1);
        daoUnidade.salvar(und2);
        daoUnidade.salvar(und3);
        daoUnidade.salvar(und4);
        daoUnidade.salvar(und5);
        daoUnidade.salvar(und6);
        daoUnidade.salvar(und7);
        daoUnidade.salvar(und8);

        Set<Unidade> listaUnid = new HashSet<>();
        listaUnid.add(und1);
        listaUnid.add(und2);
        listaUnid.add(und3);
        listaUnid.add(und4);
        listaUnid.add(und5);
        listaUnid.add(und6);
        listaUnid.add(und7);
        listaUnid.add(und8);

        TipoMesa tipoMesa = new TipoMesa("Fixa", true);
        TipoMesa tipoMesa2 = new TipoMesa("Evento", true);
        TipoMesaDAO daoTipoMesa = new TipoMesaDAO();

        daoTipoMesa.salvar(tipoMesa);
        daoTipoMesa.salvar(tipoMesa2);

        MesaDAO daoMesa = new MesaDAO();
        daoMesa.salvar(new Mesa("CICOM - COSTA DO DESCOBRIMENTO", listaUnid, tipoMesa, true));
    }
}
