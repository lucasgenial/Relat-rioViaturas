package com.cicom.relatorioefetivos.DAO;

import com.cicom.relatorioefetivos.model.ServidorFuncao;
import java.util.List;

/**
 *
 * @author Lucas Matos
 */
public class ServidorFuncaoDAO extends AbstractDAO<ServidorFuncao> {

    public ServidorFuncaoDAO() {
        super(ServidorFuncao.class);
    }

    @SuppressWarnings("unchecked")
    public List<ServidorFuncao> getListAtivos() {
        List<ServidorFuncao> t = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            transacao.begin();
            t = administracao.createQuery("SELECT u FROM ServidorFuncao u WHERE u.ativo=1").getResultList();
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally {
            administracao.close();
        }
        return t;
    }

}
