package com.cicom.relatorioviaturas.DAO;

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
        try {
            resultados = administracao.createQuery("SELECT u FROM Mesa u WHERE u.nome=:nome")
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
