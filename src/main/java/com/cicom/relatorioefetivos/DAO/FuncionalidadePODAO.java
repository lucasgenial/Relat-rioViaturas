package com.cicom.relatorioefetivos.DAO;

import com.cicom.relatorioefetivos.model.FuncionalidadePO;
import java.util.List;

/**
 * MesaDAO class
 *
 * @author Lucas Matos
 */
public class FuncionalidadePODAO extends AbstractDAO<FuncionalidadePO> {

    public FuncionalidadePODAO() {
        super(FuncionalidadePO.class);
    }

    @SuppressWarnings("unchecked")
    public FuncionalidadePO buscaPorNome(String nome) {
        List<FuncionalidadePO> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            resultados = administracao.createQuery("SELECT u FROM FuncionalidadePO u WHERE u.nome=:nome")
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
