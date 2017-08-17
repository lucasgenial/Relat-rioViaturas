package com.cicom.relatorioviaturas.DAO;

import static com.cicom.relatorioviaturas.DAO.AbstractDAO.administracao;
import com.cicom.relatorioviaturas.model.Funcao;
import java.util.List;

/**
 * @author Lucas Matos
 */
public class FuncaoDAO extends AbstractDAO<Funcao> {

    public FuncaoDAO() {
        super(Funcao.class);
    }

    @SuppressWarnings("unchecked")
    public Funcao buscaPorNome(String nome) {
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        List<Funcao> resultados = null;
        try {
            resultados = administracao.createQuery("SELECT u FROM Funcao u WHERE u.nome=:nome")
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
