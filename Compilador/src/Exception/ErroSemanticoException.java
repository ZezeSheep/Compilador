package Exception;

public class ErroSemanticoException extends Exception {
	
	public ErroSemanticoException(String msg){
        super(msg);
    }

    public ErroSemanticoException(String msg, Throwable cause){
        super(msg, cause);
    }

}
