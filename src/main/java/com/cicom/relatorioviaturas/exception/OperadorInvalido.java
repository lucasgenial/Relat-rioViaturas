package com.cicom.relatorioviaturas.exception;

/**
 *
 * @author Lucas Matos
 */
public class OperadorInvalido extends Exception {
    // constrói um objeto OperadorInvalido com a mensagem passada por parâmetro
    public OperadorInvalido(String msg) {
        super(msg);
    }

    // contrói um objeto OperadorInvalido com mensagem e a causa dessa exceção, utilizado para encadear exceptions
    public OperadorInvalido(String msg, Throwable cause) {
        super(msg, cause);
    }
}
