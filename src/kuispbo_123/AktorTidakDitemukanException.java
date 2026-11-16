// Custom Exception
public class AktorTidakDitemukanException extends Exception {
    private static final long serialVersionUID = 1L;

    public AktorTidakDitemukanException(String pesan) {
        super(pesan);
    }
}
