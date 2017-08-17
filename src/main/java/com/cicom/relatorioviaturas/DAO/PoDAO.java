package com.cicom.relatorioviaturas.DAO;

import static com.cicom.relatorioviaturas.DAO.AbstractDAO.administracao;
import com.cicom.relatorioviaturas.model.PO;
import java.util.List;

/**
 * @author Lucas Matos
 */
public class PoDAO extends AbstractDAO<PO> {

    public PoDAO() {
        super(PO.class);
    }

    @SuppressWarnings("unchecked")
    public PO buscaPorNome(String nome) {
        List<PO> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            resultados = administracao.createQuery("SELECT u FROM PO u WHERE u.nome=:nome")
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
    public void deletar(PO po) {
        po.setAtivo(false);
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            transacao.begin();
            administracao.merge(po);
            transacao.commit();
        } catch (Exception e) {
            throw e;
        } finally{
            administracao.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<PO> getListAtivos() {
        List<PO> t = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            transacao.begin();
            t = administracao.createQuery("SELECT u FROM PO u WHERE u.ativo=1").getResultList();
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
