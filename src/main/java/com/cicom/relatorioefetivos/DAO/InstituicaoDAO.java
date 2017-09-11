package com.cicom.relatorioefetivos.DAO;

import static com.cicom.relatorioefetivos.DAO.AbstractDAO.administracao;
import com.cicom.relatorioefetivos.model.Instituicao;
import java.util.List;

/**
 * @author Lucas Matos
 */
public class InstituicaoDAO extends AbstractDAO<Instituicao> {

    public InstituicaoDAO() {
        super(Instituicao.class);
    }

    @SuppressWarnings("unchecked")
    public Instituicao buscaPorNome(String nome) {
        List<Instituicao> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            resultados = administracao.createQuery("SELECT u FROM Instituicao u WHERE u.nome=:nome")
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

}
