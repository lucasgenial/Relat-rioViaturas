package com.cicom.relatorioviaturas.DAO;

import static com.cicom.relatorioviaturas.DAO.AbstractDAO.administracao;
import com.cicom.relatorioviaturas.model.Sexo;
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
        try {
            resultados = administracao.createQuery("SELECT u FROM Sexo u WHERE u.nome=:nome")
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
