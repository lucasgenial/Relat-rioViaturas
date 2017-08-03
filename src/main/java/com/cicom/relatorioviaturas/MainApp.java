package com.cicom.relatorioviaturas;

import com.cicom.relatorioviaturas.DAO.FuncaoDAO;
import com.cicom.relatorioviaturas.DAO.InstituicaoDAO;
import com.cicom.relatorioviaturas.DAO.MesaDAO;
import com.cicom.relatorioviaturas.DAO.PoDAO;
import com.cicom.relatorioviaturas.DAO.ServidorDAO;
import com.cicom.relatorioviaturas.DAO.SexoDAO;
import com.cicom.relatorioviaturas.DAO.UnidadeDAO;
import com.cicom.relatorioviaturas.model.Funcao;
import com.cicom.relatorioviaturas.model.Instituicao;
import com.cicom.relatorioviaturas.model.Mesa;
import com.cicom.relatorioviaturas.model.PO;
import com.cicom.relatorioviaturas.model.Sexo;
import com.cicom.relatorioviaturas.model.Unidade;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        daoSexo.salvar(new Sexo("Masculino"));
        daoSexo.salvar(new Sexo("Feminino"));

        System.out.println("SALVAR SEXO\n\n");
        
        System.out.println("SALVAR INSTITUCIONAL");
        InstituicaoDAO daoInstituicao = new InstituicaoDAO();
        
        daoInstituicao.salvar(new Instituicao("PM"));
        daoInstituicao.salvar(new Instituicao("BPM"));
        daoInstituicao.salvar(new Instituicao("PC"));
        daoInstituicao.salvar(new Instituicao("DPT"));
        
        System.out.println("SALVAR INSTITUCIONAL\n\n");

//        //CADASTRA COMANDANTE
//        System.out.println("SALVAR SERVIDOR \n\n");
        ServidorDAO daoServidor = new ServidorDAO();
        daoServidor.getListAtivos();
        
//        public Servidor(String nome, String matricula, Instituicao instituicao, String grauHierarquico, Sexo sexo, String observacao, Boolean ativo);
//        Servidor ser1 = new Servidor("SERGIO ANTONIO", "4241", "PM", "Tenente",  true);
//        Servidor ser2 = new Servidor("PEDRO ORLANDO SARDA FILHO","Tenente", "342432", true);
//        Servidor ser3 = new Servidor("ANNA KARINA DO NASCIMENTO BONATO", "Tenente", "89492", true);
//        Servidor ser4 = new Servidor("MONICA GONCALVES PETRY", "Tenente", "8748", true);
//        Servidor ser5 = new Servidor("LEANDRO LEONIDAS DE BRITO","Tenente", "42421", true);
//        Servidor ser6 = new Servidor("DILVO DOS SANTOS BRUM","Tenente", "64323", true);
//        Servidor ser7 = new Servidor("CARLA VALERIA HUERGO DE CARVALHO", "Tenente", "82917", true);
//
//        daoServidor.salvar(ser1);
//        daoServidor.salvar(ser2);
//        daoServidor.salvar(ser3);
//        daoServidor.salvar(ser4);
//        daoServidor.salvar(ser5);
//        daoServidor.salvar(ser6);
//        daoServidor.salvar(ser7);
//
//        System.out.println("FUNCÕES SALVAS \n\n");
//
//        //CADASTRA OPERADOR
//        System.out.println("SALVAR SERVIDOR \n\n");
//        daoServidor.salvar(new Servidor("JULIANA MIRANDA MARTINS", "MIRANDA", "Cabo", "82719", true));
//        daoServidor.salvar(new Servidor("JOAO CLEBER BOPKO", "CLEBER", "Cabo", "1283", true));
//        daoServidor.salvar(new Servidor("ANA PAULA FIOR DAL CASTEL", "FIOR", "Cabo", "1928", true));
//        daoServidor.salvar(new Servidor("FELIPE BITENCOURT LAZEREIS", "LAZEREIS", "Cabo", "3232", true));
//        daoServidor.salvar(new Servidor("TAMARA PADILHA DE SOUZA", "PADILHA", "Cabo", "552", true));
//        daoServidor.salvar(new Servidor("ENEDIR DE PROENCA", "PROENCA", "Cabo", "8321", true));
//
//        //Patrulheiro
//        daoServidor.salvar(new Servidor("MAYKON LUIZ ZORZAN DE LIMA", "ZORZAN", "Soldado", "84272", true));
//        daoServidor.salvar(new Servidor("LAZARO ESTEVAO DE PAULA", "ESTEVAO", "Soldado", "324", true));
//        daoServidor.salvar(new Servidor("LUCAS INOCENCIO DOS SANTOS", "INOCENCIO", "Soldado", "32324", true));
//        daoServidor.salvar(new Servidor("ICARO VILLAGE DELVALE", "VILLAGE", "Soldado", "8427422", true));
//        daoServidor.salvar(new Servidor("MARCOS VENICIO ROHR", "VENICIO", "Soldado", "653", true));
//        daoServidor.salvar(new Servidor("ROGERIO DEDINI GUSSAO", "GUSSAO", "Soldado", "2", true));
//        daoServidor.salvar(new Servidor("JOSE ISAC DE LARA", "ISAC", "Soldado", "42", true));
//        daoServidor.salvar(new Servidor("KELLY ISABEL RODRIGUES DE PAULA", "RODRIGUES", "Soldado", "424", true));
//        daoServidor.salvar(new Servidor("TALES BAZZO LUCCIN", "BAZZO", "Soldado", "5321", true));
//        daoServidor.salvar(new Servidor("ARILSON ZARELLI CUSTODIO DA SILVA", "ZARELLI", "Soldado", "567", true));
//        daoServidor.salvar(new Servidor("WILLIAM SERIBELI", "SERIBELI", "Soldado", "253467", true));
//        daoServidor.salvar(new Servidor("RAIMIR PEREIRA DA SILVA", "PEREIRA", "Soldado", "290019", true));
//        daoServidor.salvar(new Servidor("JUARI CARLOS PEREIRA", "CARLOS", "Soldado", "2121990", true));
//
//        //MOTORISTA
//        daoServidor.salvar(new Servidor("TIAGO FURLANETTO SOARES", "FURLANETTO", "Cabo", "299292", true));
//        daoServidor.salvar(new Servidor("PLINIO SEBASTIAO GALDINO", "GALDINO", "Cabo", "4343", true));
//        daoServidor.salvar(new Servidor("FERNANDA STASIAK CAPELLINE", "STASIAK", "Cabo", "4351", true));
//        daoServidor.salvar(new Servidor("TIMOTEO CALISTRO DE SOUZA", "CALISTRO", "Cabo", "8543", true));
//        daoServidor.salvar(new Servidor("DAYSE TIEMI SAKIAMA VIEIRA", "TIEMI", "Cabo", "783", true));
//        daoServidor.salvar(new Servidor("ANDREW TSUJIGUCHI", "TSUJIGUCHI", "Cabo", "7692", true));
//        daoServidor.salvar(new Servidor("DIOLANDO ESTRELA", "ESTRELA", "Cabo", "538", true));
//        daoServidor.salvar(new Servidor("IRWING CESAR BONDAR", "BONDAR", "Cabo", "6464", true));
//
//        System.out.println("SERVIDOR SALVOS \n\n");
//
//        //Unidade
//        System.out.println("SALVAR UNIDADE \n\n");
        Unidade und1 = new Unidade("POLICIA MILITAR","8º BPM");
        Unidade und2 = new Unidade("POLICIA MILITAR","7º CIPM");
        Unidade und3 = new Unidade("POLICIA MILITAR","CIPPA");
        Unidade und4 = new Unidade("POLICIA MILITAR","5º PEL/CIPRV - ITABUNA");
        Unidade und5 = new Unidade("POLICIA MILITAR","CIPT/SUL");
        Unidade und6 = new Unidade("CORPO DE BOMBEIROS MILITAR","6º GBM");
        Unidade und7 = new Unidade("DPT","24º CRPT");
        Unidade und8 = new Unidade("DELEGACIA","23º COORPIN");
//        Unidade und2 = new Unidade("10ª CIPM / CANDEIAS", "R.M.S");
//        Unidade und3 = new Unidade("22ª CIPM / SIMÕES FILHO", "R.M.S");
//        Unidade und4 = new Unidade("32ª CIPM / POJUCA", "R.M.S");
//        Unidade und5 = new Unidade("36ª CIPM / DIAS D'ÁVILA", "R.M.S");
//        Unidade und6 = new Unidade("52ª CIPM / LAURO DE FREITAS", "R.M.S");
//        Unidade und7 = new Unidade("53ª CIPM / PRAIA DO FORTE", "R.M.S");
//        Unidade und8 = new Unidade("59ª CIPM / VILA DE ABRANTES", "R.M.S");
//        Unidade und9 = new Unidade("81ª CIPM / ITINGA", "R.M.S");
//        Unidade und10 = new Unidade("12º BPM / CANDEIAS", "R.M.S");
//        Unidade und11 = new Unidade("CIPT/CPRMS", "R.M.S");
//        Unidade und12 = new Unidade("1ª CIPM / PERNANBUÉS", "CENTRAL");
//        Unidade und13 = new Unidade("3ª CIPM / CAJAZEIRAS", "CENTRAL");
//        Unidade und14 = new Unidade("23ª CIPM / TANCREDO NEVES", "CENTRAL");
//        Unidade und15 = new Unidade("47ª CIPM / PAU DA LIMA", "CENTRAL", listaDePOs);
//        Unidade und16 = new Unidade("48ª CIPM / SUSSUARANA", "CENTRAL");
//        Unidade und17 = new Unidade("50ª CIPM / SETE DE ABRIL", "CENTRAL");
//        Unidade und18 = new Unidade("82ª CIPM / CAB", "CENTRAL");
//        Unidade und19 = new Unidade("CIPT/CENTRAL", "CENTRAL");
//        Unidade und20 = new Unidade("11ª CIPM / BARRA", "ATLÂNTICO");
//        Unidade und21 = new Unidade("12ª CIPM / RIO VERMELHO", "ATLANTICO");
//        Unidade und22 = new Unidade("13ª CIPM / PITUBA", "ATLÂNTICO");
//        Unidade und23 = new Unidade("15ª CIPM / ITAPUÃ", "ATLÂNTICO");
//        Unidade und24 = new Unidade("BCS / BAIRRO DA PAZ", "ATLÂNTICO");
//        Unidade und25 = new Unidade("26ª CIPM / BROTAS", "ATLÂNTICO");
//        Unidade und26 = new Unidade("35ª CIPM / IGUATEMI", "ATLÂNTICO");
//        Unidade und27 = new Unidade("39ª CIPM / BOCA DO RIO", "ATLÂNTICO");
//        Unidade und28 = new Unidade("40ª CIPM / NORDESTE", "ATLÂNTICO");
//        Unidade und29 = new Unidade("BCS/NORDESTE", "ATLÂNTICO");
//        Unidade und30 = new Unidade("BCS/CHAPADA", "ATLÂNTICO");
//        Unidade und31 = new Unidade("BCS/SANTA CRUZ", "ATLÂNTICO");
//        Unidade und32 = new Unidade("41ª CIPM / FEDERAÇÃO", "ATLÂNTICO");
//        Unidade und33 = new Unidade("BCS/CALABAR", "ATLÂNTICO");
//        Unidade und34 = new Unidade("49ª CIPM / SÃO CRISTOVÃO", "ATLÂNTICO");
//        Unidade und35 = new Unidade("58ª CIPM / COSME DE FARIAS", "ATLÂNTICO");
//        Unidade und36 = new Unidade("CIPT/ATLÂNTICO", "ATLÂNTICO");
//        Unidade und37 = new Unidade("18º BPM / PEROURINHO", "B.T.S");
//        Unidade und38 = new Unidade("2ª CIPM / BARBALHO", "B.T.S");
//        Unidade und39 = new Unidade("9ª CIPM / PIRAJÁ", "B.T.S");
//        Unidade und40 = new Unidade("14ª CIPM / LOBATO", "B.T.S");
//        Unidade und41 = new Unidade("16ª CIPM / COMÉRCIO", "B.T.S");
//        Unidade und42 = new Unidade("17ª CIPM / URUGUAI", "B.T.S");
//        Unidade und43 = new Unidade("BSC/RIO SENA", "B.T.S");
//        Unidade und44 = new Unidade("18ª CIPM / PERIPERI", "B.T.S");
//        Unidade und45 = new Unidade("BCS/COUTOS", "B.T.S");
//        Unidade und46 = new Unidade("19ª CIPM / PARIPE", "B.T.S");
//        Unidade und47 = new Unidade("31ª CIPM / VALÉRIA", "B.T.S");
//        Unidade und48 = new Unidade("37ª CIPM / LIBERDADE", "B.T.S");
//        Unidade und49 = new Unidade("CIPT/BTS", "B.T.S");
//        Unidade und50 = new Unidade("BPRV", "C.P.E");
//        Unidade und51 = new Unidade("CHOQUE", "C.P.E");
//        Unidade und52 = new Unidade("GRAER", "C.P.E");
//        Unidade und53 = new Unidade("ÁGUIA", "C.P.E");
//        Unidade und54 = new Unidade("CIPE POLO", "C.P.E");
//        Unidade und55 = new Unidade("BPGD", "C.P.E");
//        Unidade und56 = new Unidade("GÊMEOS", "C.P.E");
//        Unidade und57 = new Unidade("MONTADA", "C.P.E");
//        Unidade und58 = new Unidade("COPPA", "C.P.E");
//        Unidade und59 = new Unidade("RONDA ESCOLAR", "C.P.E");
//        Unidade und60 = new Unidade("CTS COOPER", "C.P.E");
//        Unidade und61 = new Unidade("APOLO", "C.P.E");
//        Unidade und62 = new Unidade("BEPTUR", "C.P.E");
//        Unidade und63 = new Unidade("BEPE", "C.P.E");
//        Unidade und64 = new Unidade("BEPE/TUR", "C.P.E");
//        Unidade und65 = new Unidade("MARIA DA PENHA", "C.P.E");
//        Unidade und66 = new Unidade("OPERAÇÃO VERÃO", "C.P.E");

        UnidadeDAO daoUnidade = new UnidadeDAO();
        daoUnidade.salvar(und1);
        daoUnidade.salvar(und2);
        daoUnidade.salvar(und3);
        daoUnidade.salvar(und4);
        daoUnidade.salvar(und5);
        daoUnidade.salvar(und6);
        daoUnidade.salvar(und7);
        daoUnidade.salvar(und8);
//        daoUnidade.salvar(und9);
//        daoUnidade.salvar(und10);
//        daoUnidade.salvar(und11);
//        daoUnidade.salvar(und12);
//        daoUnidade.salvar(und13);
//        daoUnidade.salvar(und14);
//        daoUnidade.salvar(und15);
//        daoUnidade.salvar(und16);
//        daoUnidade.salvar(und17);
//        daoUnidade.salvar(und18);
//        daoUnidade.salvar(und19);
//        daoUnidade.salvar(und20);
//        daoUnidade.salvar(und21);
//        daoUnidade.salvar(und22);
//        daoUnidade.salvar(und23);
//        daoUnidade.salvar(und24);
//        daoUnidade.salvar(und25);
//        daoUnidade.salvar(und26);
//        daoUnidade.salvar(und27);
//        daoUnidade.salvar(und28);
//        daoUnidade.salvar(und29);
//        daoUnidade.salvar(und30);
//        daoUnidade.salvar(und31);
//        daoUnidade.salvar(und32);
//        daoUnidade.salvar(und33);
//        daoUnidade.salvar(und34);
//        daoUnidade.salvar(und35);
//        daoUnidade.salvar(und36);
//        daoUnidade.salvar(und37);
//        daoUnidade.salvar(und38);
//        daoUnidade.salvar(und39);
//        daoUnidade.salvar(und40);
//        daoUnidade.salvar(und41);
//        daoUnidade.salvar(und42);
//        daoUnidade.salvar(und43);
//        daoUnidade.salvar(und44);
//        daoUnidade.salvar(und45);
//        daoUnidade.salvar(und46);
//        daoUnidade.salvar(und47);
//        daoUnidade.salvar(und48);
//        daoUnidade.salvar(und49);
//        daoUnidade.salvar(und50);
//        daoUnidade.salvar(und51);
//        daoUnidade.salvar(und52);
//        daoUnidade.salvar(und53);
//        daoUnidade.salvar(und54);
//        daoUnidade.salvar(und55);
//        daoUnidade.salvar(und56);
//        daoUnidade.salvar(und57);
//        daoUnidade.salvar(und58);
//        daoUnidade.salvar(und59);
//        daoUnidade.salvar(und60);
//        daoUnidade.salvar(und61);
//        daoUnidade.salvar(und62);
//        daoUnidade.salvar(und63);
//        daoUnidade.salvar(und64);
//        daoUnidade.salvar(und65);
//        daoUnidade.salvar(und66);

        System.out.println("UNIDADES SALVAS \n\n");

        //Cadastra MESAS
        System.out.println("SALVAR MESA \n\n");

        Set<Unidade> listaUnid = new HashSet<>();
        listaUnid.add(und1);
        listaUnid.add(und3);
        listaUnid.add(und5);
//        listaUnid.add(und12);
//        listaUnid.add(und14);
//        listaUnid.add(und15);
//        listaUnid.add(und20);
//        listaUnid.add(und22);
//        listaUnid.add(und24);
//        listaUnid.add(und37);
//        listaUnid.add(und39);
//        listaUnid.add(und41);
//        listaUnid.add(und59);
//        listaUnid.add(und64);
//        listaUnid.add(und65);
        MesaDAO daoMesa = new MesaDAO();
        daoMesa.salvar(new Mesa("CICOM - PORTO SEGURO", listaUnid));
//        daoMesa.salvar(new Mesa("ATL 02"));
//        daoMesa.salvar(new Mesa("ATL 03"));
//        daoMesa.salvar(new Mesa("ATL 04"));
//        daoMesa.salvar(new Mesa("ATL 05"));
//        daoMesa.salvar(new Mesa("ATL 06"));
//        daoMesa.salvar(new Mesa("ATL 07"));
//        daoMesa.salvar(new Mesa("ATL 08"));
//        daoMesa.salvar(new Mesa("ATL 09"));
//        daoMesa.salvar(new Mesa("ATL 10"));
//        daoMesa.salvar(new Mesa("ATL 11"));
//        daoMesa.salvar(new Mesa("BTS 12"));
//        daoMesa.salvar(new Mesa("BTS 13"));
//        daoMesa.salvar(new Mesa("BTS 14"));
//        daoMesa.salvar(new Mesa("BTS 15"));
//        daoMesa.salvar(new Mesa("BTS 16"));
//        daoMesa.salvar(new Mesa("BTS 17"));
//        daoMesa.salvar(new Mesa("BTS 18"));
//        daoMesa.salvar(new Mesa("BTS 19"));
//        daoMesa.salvar(new Mesa("BTS 20"));
//        daoMesa.salvar(new Mesa("BTS 21"));
//        daoMesa.salvar(new Mesa("CEN 22"));
//        daoMesa.salvar(new Mesa("CEN 23"));
//        daoMesa.salvar(new Mesa("CEN 24"));
//        daoMesa.salvar(new Mesa("CEN 25"));
//        daoMesa.salvar(new Mesa("CEN 26"));
//        daoMesa.salvar(new Mesa("CEN 27"));
//        daoMesa.salvar(new Mesa("CEN 28"));
//        daoMesa.salvar(new Mesa("RMS 29"));
//        daoMesa.salvar(new Mesa("RMS 30"));
//        daoMesa.salvar(new Mesa("RMS 31"));
//        daoMesa.salvar(new Mesa("RMS 32"));
//        daoMesa.salvar(new Mesa("RMS 33"));
//        daoMesa.salvar(new Mesa("RMS 34"));
//        daoMesa.salvar(new Mesa("RMS 35"));
//        daoMesa.salvar(new Mesa("RMS 36"));
//        daoMesa.salvar(new Mesa("CPE 37"));
//        daoMesa.salvar(new Mesa("CPE 38"));
//        daoMesa.salvar(new Mesa("CIPT 39"));
//        daoMesa.salvar(new Mesa("CIPT 40"));
//        daoMesa.salvar(new Mesa("RMS 41"));
//        daoMesa.salvar(new Mesa("RMS 42"));

        System.out.println("MESAS SALVAS \n\n");
    }
}
