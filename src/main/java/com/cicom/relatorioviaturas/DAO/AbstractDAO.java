package com.cicom.relatorioviaturas.DAO;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public abstract class AbstractDAO<T> implements FabricaDAO<T> {

    static EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("persistenciaBancoDeDados");
//    static EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("h2PU");
    static EntityManager administracao = fabrica.createEntityManager();
    static EntityTransaction transacao = administracao.getTransaction();
    private final Class<T> classeEntidade;

    public AbstractDAO(Class<T> classeEntidade) {
        this.classeEntidade = classeEntidade;
        fabrica.getCache().evictAll();
    }

    @Override
    public void salvar(T obj) {
        try {
            if (obj != null) {
                transacao.begin();
                administracao.persist(obj);
                transacao.commit();
            }
        } catch (RuntimeException e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally {
//            administracao.close();
        }
    }

    @Override
    public T buscaPorCodigo(int codigoBuscado) {
        T retorno = null;

        try {
            transacao.begin();
            retorno = administracao.find(classeEntidade, codigoBuscado);
            transacao.commit();

            if (retorno == null) {
                System.out.println("ESTOU AQUI");
            }
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally {
            //Fecha a Tranção e a fabrica
//            administracao.close();
        }
        System.out.println("BUSCA POR CODIGO: " + retorno);
        return retorno;
    }

    @Override
    public T buscaPorObjeto(T obj) {
        T retorno = null;

        try {
            transacao.begin();
            retorno = administracao.find(classeEntidade, obj);
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally {
            //Fecha a Tranção e a fabrica
//            administracao.close();
        }
        return retorno;
    }

    @Override
    public T alterar(T obj) {
        try {
            transacao.begin();
            obj = administracao.merge(obj);
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally {
            //Fecha a Tranção e a fabrica
//            administracao.close();
        }
        return obj;
    }

    @Override
    public void deletar(int id) {
        try {
            transacao.begin();
            administracao.remove(administracao.find(classeEntidade, id));
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<T> getList(String sql) {

        List<T> t = new ArrayList<>();

        try {
            transacao.begin();
            t = administracao.createQuery(sql, classeEntidade).getResultList();
            transacao.commit();
        } catch (Exception e) {
            if (transacao != null) {
                transacao.rollback();
            }
            e.printStackTrace();
        } finally {
            //Fecha a Tranção e a fabrica
//            administracao.close();
        }
        return t;
    }
}
