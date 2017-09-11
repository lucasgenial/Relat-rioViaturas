package com.cicom.relatorioefetivos.DAO;

import static com.cicom.relatorioefetivos.DAO.AbstractDAO.administracao;
import com.cicom.relatorioefetivos.model.Sexo;
import java.util.List;

/**
 * @author Lucas Matos
 */
public class SexoDAO extends AbstractDAO<Sexo> {

    public SexoDAO() {
        super(Sexo.class);
    }

    @SuppressWarnings("unchecked")
    public Sexo buscaPorNome(String nome) {
        List<Sexo> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();

        try {
            resultados = administracao.createQuery("SELECT u FROM Sexo u WHERE u.nome=:nome")
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
