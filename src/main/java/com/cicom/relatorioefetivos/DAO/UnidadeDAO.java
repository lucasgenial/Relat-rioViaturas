package com.cicom.relatorioefetivos.DAO;

import static com.cicom.relatorioefetivos.DAO.AbstractDAO.administracao;
import com.cicom.relatorioefetivos.model.Unidade;
import java.util.List;

/**
 *
 * @author Lucas Matos
 */
public class UnidadeDAO extends AbstractDAO<Unidade> {

    public UnidadeDAO() {
        super(Unidade.class);
    }

    @SuppressWarnings("unchecked")
    public Unidade buscaPorNome(String nome) {
        List<Unidade> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            resultados = administracao.createQuery("SELECT u FROM Unidade u WHERE u.nome=:nome")
                    .setParameter("nome", nome)
                    .getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            administracao.close();
        }

        if (resultados.size() > 0) {
            return resultados.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Unidade> getListAtivos() {
        List<Unidade> t = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            transacao.begin();
            t = administracao.createQuery("SELECT u FROM Unidade u WHERE u.ativo=1").getResultList();
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
