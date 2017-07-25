package com.cicom.relatorioviaturas.DAO;

import java.util.List;

public interface FabricaDAO<T> {

    /**
     * Salva o objeto passado no banco de dados
     *
     * @param obj
     */
    public void salvar(T obj);

    /**
     * Busca um objeto pelo código
     *
     * @param codigoBuscado
     * @return
     */
    public T buscaPorCodigo(int codigoBuscado);
    
    /**
     * Altera no banco de Dados.
     *
     * @param obj
     * @return 
     */
    public T alterar(T obj);
    
    /**
     * Deleta definitivo do banco de Dados.
     *
     * @param id
     */
    public void deletar(int id);
    
    public T buscaPorObjeto(T obj);
    
    public List<T> getList(String query);
   
}