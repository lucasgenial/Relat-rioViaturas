package com.cicom.relatorioviaturas.DAO;

import static com.cicom.relatorioviaturas.DAO.AbstractDAO.transacao;
import com.cicom.relatorioviaturas.model.Servidor;
import java.util.List;

/**
 *
 * @author icaro.bastos
 */
public class ServidorDAO extends AbstractDAO<Servidor> {

    public ServidorDAO() {
        super(Servidor.class);
    }

    @SuppressWarnings("unchecked")
    public Servidor buscaPorNome(String nome) {
        List<Servidor> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            resultados = administracao.createQuery("SELECT u FROM Servidor u WHERE u.nome=:nome")
                    .setParameter("nome", nome)
                    .getResultList();
        } catch (Exception e) {
            throw e;
        } finally{
            administracao.close();
        }

        if (resultados.size() > 0) {
            return resultados.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Servidor buscaPorMatricula(String matricula) {
        List<Servidor> resultados = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            resultados = administracao.createQuery("SELECT u FROM Servidor u WHERE u.matricula=:matricula")
                    .setParameter("matricula", matricula)
                    .getResultList();
        } catch (Exception e) {
            throw e;
        } finally{
            administracao.close();
        }

        if (resultados.size() > 0) {
            return resultados.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void deletar(Servidor servidor) {
        servidor.setAtivo(false);
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            transacao.begin();
            administracao.merge(servidor);
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally{
            administracao.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Servidor> getListAtivos() {
        List<Servidor> t = null;
        administracao = fabrica.createEntityManager();
        transacao = administracao.getTransaction();
        
        try {
            transacao.begin();
            t = administracao.createQuery("SELECT u FROM Servidor u WHERE u.ativo=1").getResultList();
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally{
            administracao.close();
        }
        return t;
    }
}
