package com.cicom.relatorioviaturas;

import com.cicom.relatorioviaturas.DAO.FuncaoDAO;
import com.cicom.relatorioviaturas.DAO.InstituicaoDAO;
import com.cicom.relatorioviaturas.DAO.MesaDAO;
import com.cicom.relatorioviaturas.DAO.PoDAO;
import com.cicom.relatorioviaturas.DAO.ServidorDAO;
import com.cicom.relatorioviaturas.DAO.SexoDAO;
import com.cicom.relatorioviaturas.DAO.TipoMesaDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.model.Funcao;
import com.cicom.relatorioviaturas.model.Instituicao;
import com.cicom.relatorioviaturas.model.Mesa;
import com.cicom.relatorioviaturas.model.PO;
import com.cicom.relatorioviaturas.model.Servidor;
import com.cicom.relatorioviaturas.model.Sexo;
import com.cicom.relatorioviaturas.model.TipoMesa;
import com.cicom.relatorioviaturas.model.Unidade;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        lancarDados();
        setUserAgentStylesheet(STYLESHEET_MODENA);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SplashScreen.fxml"));

        Image iconApp = new Image(getClass().getResourceAsStream("/Imagens/ic_app_sisef.png"));
        stage.getIcons().add(iconApp);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void lancarDados() {
        //PO
        PoDAO daoPO = new PoDAO();
        Set<PO> listaDePOs = new HashSet<>();
        listaDePOs.add(new PO("A PÉ", true));
        listaDePOs.add(new PO("VIATURA 4 RODAS", true));
        listaDePOs.add(new PO("VIATURA 2 RODAS", true));
        listaDePOs.add(new PO("BARCO", true));
        listaDePOs.add(new PO("MÓDULO", true));

        System.out.println("SALVAR OS POs \n\n");
        for (PO item : listaDePOs) {
            daoPO.salvar(item);
        }

        System.out.println("POS SALVOS \n\n");

        System.out.println("SALVAR FUNCAO \n\n");
        //FUNCAO
        Funcao func1 = new Funcao("Motorista");
        Funcao func2 = new Funcao("Patrulheiro");
        Funcao func3 = new Funcao("Supervisor");
        Funcao func4 = new Funcao("Operador");
        Funcao func5 = new Funcao("Comandante");

        FuncaoDAO daoFuncao = new FuncaoDAO();
        daoFuncao.salvar(func1);
        daoFuncao.salvar(func2);
        daoFuncao.salvar(func3);
        daoFuncao.salvar(func4);
        daoFuncao.salvar(func5);

        System.out.println("FUNCAO SALVAS \n\n");

        System.out.println("SALVAR SEXO");
        SexoDAO daoSexo = new SexoDAO();

        Sexo sex1 = new Sexo("Masculino");
        Sexo sex2 = new Sexo("Feminino");

        daoSexo.salvar(sex1);
        daoSexo.salvar(sex2);

        System.out.println("SALVAR SEXO\n\n");

        System.out.println("SALVAR INSTITUCIONAL");
        InstituicaoDAO daoInstituicao = new InstituicaoDAO();

        Instituicao inst1 = new Instituicao("PM");
        Instituicao inst2 = new Instituicao("BPM");
        Instituicao inst3 = new Instituicao("PC");
        Instituicao inst4 = new Instituicao("DPT");

        daoInstituicao.salvar(inst1);
        daoInstituicao.salvar(inst2);
        daoInstituicao.salvar(inst3);
        daoInstituicao.salvar(inst4);

        System.out.println("SALVAR INSTITUCIONAL\n\n");

//        //CADASTRA COMANDANTE
//        System.out.println("SALVAR SERVIDOR \n\n");
        ServidorDAO daoServidor = new ServidorDAO();
        daoServidor.getListAtivos();

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
//        System.out.println("SALVAR UNIDADE \n\n");
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

        System.out.println("UNIDADES SALVAS \n\n");

        //Cadastra MESAS
        System.out.println("SALVAR MESA \n\n");

        Set<Unidade> listaUnid = new HashSet<>();
        listaUnid.add(und1);
        listaUnid.add(und2);
        listaUnid.add(und3);
        listaUnid.add(und4);
        listaUnid.add(und5);
        listaUnid.add(und6);
        listaUnid.add(und7);
        listaUnid.add(und8);

        TipoMesa tipoMesa = new TipoMesa("Fixa");
        TipoMesa tipoMesa2 = new TipoMesa("Evento");
        TipoMesaDAO daoTipoMesa = new TipoMesaDAO();

        daoTipoMesa.salvar(tipoMesa);
        daoTipoMesa.salvar(tipoMesa2);

        MesaDAO daoMesa = new MesaDAO();
        daoMesa.salvar(new Mesa("CICOM - COSTA DO DESCOBRIMENTO", listaUnid, tipoMesa, true));

        System.out.println("MESAS SALVAS \n\n");
    }
}
