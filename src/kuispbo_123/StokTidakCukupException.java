// Custom Exception
public class StokTidakCukupException extends Exception {
    private static final long serialVersionUID = 1L;

    public StokTidakCukupException(String pesan) {
        super(pesan);
    }
}
