package compilador;

public class LiteralBadFormattedException extends Exception {
	/**
     * importante caso a exceção seja serializada
     */
    private static final long serialVersionUID = 1149241010009861914L;

    public LiteralBadFormattedException(String msg){
        super(msg);
    }

    public LiteralBadFormattedException(String msg, Throwable cause){
        super(msg, cause);
    }

}
