package com.cicom.relatorioefetivos.DAO;

import com.cicom.relatorioefetivos.model.Funcionalidade;
import java.util.List;

/**
 * MesaDAO class
 *
 * @author Lucas Matos
 */
public class FuncionalidadeDAO extends AbstractDAO<Funcionalidade> {

    public FuncionalidadeDAO() {
        super(Funcionalidade.class);
    }

    @SuppressWarnings("unchecked")
    public Funcionalidade buscaPorNome(String nome) {
        List<Funcionalidade> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            resultados = administracao.createQuery("SELECT u FROM Funcionalidade u WHERE u.nome=:nome")
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
