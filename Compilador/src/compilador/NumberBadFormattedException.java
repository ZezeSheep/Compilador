package compilador;

public class NumberBadFormattedException extends Exception {
	/**
     * importante caso a exce��o seja serializada
     */
    private static final long serialVersionUID = 1149241039409861914L;

    public NumberBadFormattedException(String msg){
        super(msg);
    }

    public NumberBadFormattedException(String msg, Throwable cause){
        super(msg, cause);
    }

}
