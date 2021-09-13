package Exception;

public class NumberBadFormattedException extends Exception {
	/**
     * importante caso a exce��o seja serializada
     */

    public NumberBadFormattedException(String msg){
        super(msg);
    }

    public NumberBadFormattedException(String msg, Throwable cause){
        super(msg, cause);
    }

}
