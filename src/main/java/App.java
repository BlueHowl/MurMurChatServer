import org.infrastructure.json.JsonRepository;
import org.model.ServerSettings;
import org.relay.AESGCM;
import org.relay.RelayManager;
import org.relay.RelayMulticast;
import org.repository.DataInterface;
import org.server.ClientManager;
import org.server.Executor;
import org.server.TaskFactory;
import org.server.TaskList;

public class App {

    public static void main(String args[]) {
        try(DataInterface dataInterface = new JsonRepository()) {
            setSystemProperty(args);
            ServerSettings server =  dataInterface.getServerSettings();

            TaskList taskList = new TaskList();
            TaskFactory taskFactory = new TaskFactory(taskList);

            ClientManager clientManager = new ClientManager(taskFactory, server.getUnicastPort());
            (new Thread(clientManager)).start();

            Executor executor = new Executor(taskList, clientManager, server, dataInterface);
            (new Thread(executor)).start();

            RelayMulticast relayMulticast = new RelayMulticast(server.getMulticastAddress(), server.getMulticastPort(), server.getRelayPort(), server.getCurrentDomain());
            RelayManager relayManager = new RelayManager(taskFactory, relayMulticast, server.getRelayPort(), new AESGCM(server.getAESKey()));
            (new Thread(relayManager)).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setSystemProperty(final String[] args) {
        System.setProperty("javax.net.ssl.keyStore", args[0]);
        System.setProperty("javax.net.ssl.keyStorePassword", args[1]);
    }

}
