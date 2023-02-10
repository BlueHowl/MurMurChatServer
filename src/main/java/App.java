import org.server.Server;

public class App {
    private static int DEFAUL_PORT = 23502;
    private static String DEFAULT_ADDRESS = "server1.godswila.guru";
    private Server server;
    public static void main(String args[]) {
        if(args.length == 0)
            new Server(DEFAUL_PORT);
        else new Server(Integer.parseInt(args[0]));
    }
}
