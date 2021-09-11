package Exception;

public class ErroSintaticoException extends Exception {
	/**
     * importante caso a exce��o seja serializada
     */

    public ErroSintaticoException(String msg){
        super(msg);
    }

    public ErroSintaticoException(String msg, Throwable cause){
        super(msg, cause);
    }

}