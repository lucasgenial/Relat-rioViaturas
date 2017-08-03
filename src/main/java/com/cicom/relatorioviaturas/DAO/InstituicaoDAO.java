package com.cicom.relatorioviaturas.DAO;

import static com.cicom.relatorioviaturas.DAO.AbstractDAO.administracao;
import com.cicom.relatorioviaturas.model.Instituicao;
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
        try {
            resultados = administracao.createQuery("SELECT u FROM Instituicao u WHERE u.nome=:nome")
                    .setParameter("nome", nome)
                    .getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
//            administracao.close();
//            fabrica.close();
        }

        if (resultados.size() > 0) {
            return resultados.get(0);
        } else {
            return null;
        }
    }

}
