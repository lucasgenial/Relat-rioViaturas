package com.cicom.relatorioefetivos.DAO;

import com.cicom.relatorioefetivos.model.TipoMesa;
import java.util.List;

/**
 * MesaDAO class
 *
 * @author Lucas Matos
 */
public class TipoMesaDAO extends AbstractDAO<TipoMesa> {

    public TipoMesaDAO() {
        super(TipoMesa.class);
    }

    @SuppressWarnings("unchecked")
    public TipoMesa buscaPorNome(String nome) {
        List<TipoMesa> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            resultados = administracao.createQuery("SELECT u FROM TipoMesa u WHERE u.nome=:nome")
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
