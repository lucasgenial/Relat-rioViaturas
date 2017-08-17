package com.cicom.relatorioviaturas.DAO;

import static com.cicom.relatorioviaturas.DAO.AbstractDAO.transacao;
import com.cicom.relatorioviaturas.model.Mesa;
import java.util.List;

/**
 * MesaDAO class
 *
 * @author Lucas Matos
 */
public class MesaDAO extends AbstractDAO<Mesa> {

    public MesaDAO() {
        super(Mesa.class);
    }

    @SuppressWarnings("unchecked")
    public Mesa buscaPorNome(String nome) {
        List<Mesa> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            resultados = administracao.createQuery("SELECT u FROM Mesa u WHERE u.nome=:nome")
                    .setParameter("nome", nome)
                    .getResultList();
        } catch (Exception e) {
            throw e;
        } finally{
            administracao.close();
        }

        if (resultados.size() > 0) {
            return resultados.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Mesa> getListAtivos() {
        List<Mesa> t = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            transacao.begin();
            t = administracao.createQuery("SELECT u FROM Mesa u WHERE u.ativo=1").getResultList();
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally{
            administracao.close();
        }
        return t;
    }
}
