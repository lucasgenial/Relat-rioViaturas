package com.cicom.relatorioefetivos.DAO;

import com.cicom.relatorioefetivos.model.Caracteristica;
import java.util.List;

/**
 * MesaDAO class
 *
 * @author Lucas Matos
 */
public class CaracteristicaDAO extends AbstractDAO<Caracteristica> {

    public CaracteristicaDAO() {
        super(Caracteristica.class);
    }

    @SuppressWarnings("unchecked")
    public Caracteristica buscaPorNome(String nome) {
        List<Caracteristica> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            resultados = administracao.createQuery("SELECT u FROM Caracteristica u WHERE u.nome=:nome")
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
