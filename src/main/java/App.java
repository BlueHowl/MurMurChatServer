import org.infrastructure.json.JsonRepository;
import org.model.ServerSettings;
import org.relay.RelayManager;
import org.relay.RelayMulticast;
import org.repository.DataInterface;
import org.server.ClientManager;
import org.server.Executor;
import org.server.TaskFactory;
import org.server.TaskList;
import org.utils.AESGCM;

public class App {

    public static void main(String args[]) {
        try(DataInterface dataInterface = new JsonRepository()) {
            setSystemProperty(args);
            ServerSettings serverSettings =  dataInterface.getServerSettings();

            TaskList taskList = new TaskList();
            TaskFactory taskFactory = new TaskFactory(taskList);

            ClientManager clientManager = new ClientManager(taskFactory, serverSettings.getUnicastPort());
            (new Thread(clientManager)).start();

            Executor executor = new Executor(taskList, clientManager, serverSettings, dataInterface);
            (new Thread(executor)).start();

            RelayMulticast relayMulticast = new RelayMulticast(serverSettings.getMulticastAddress(), serverSettings.getMulticastPort(), serverSettings.getRelayPort(), serverSettings.getCurrentDomain());
            RelayManager relayManager = new RelayManager(taskFactory, relayMulticast, serverSettings.getRelayPort(), new AESGCM(serverSettings.getAESKey()));
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
