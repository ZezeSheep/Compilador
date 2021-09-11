package Exception;

public class LiteralBadFormattedException extends Exception {
	/**
     * importante caso a exce��o seja serializada
     */

    public LiteralBadFormattedException(String msg){
        super(msg);
    }

    public LiteralBadFormattedException(String msg, Throwable cause){
        super(msg, cause);
    }

}
