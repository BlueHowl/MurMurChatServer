import org.infrastructure.json.JsonRepository;
import org.model.ServerSettings;
import org.relay.RelayManager;
import org.relay.RelayMulticast;
import org.repository.DataInterface;
import org.resources.NetChooser;
import org.server.ClientManager;
import org.server.Executor;
import org.server.TaskFactory;
import org.server.TaskList;
import org.utils.AESGCM;

import java.net.NetworkInterface;

public class App {

    public static void main(String args[]) {
        try(DataInterface dataInterface = new JsonRepository()) {
            final NetChooser netChooser = new NetChooser();
            final NetworkInterface net = netChooser.selectInterface();

            setSystemProperty(args);
            ServerSettings serverSettings =  dataInterface.getServerSettings();

            AESGCM aesgcm = new AESGCM(serverSettings.getAESKey());

            TaskList taskList = new TaskList();
            TaskFactory taskFactory = new TaskFactory(taskList, aesgcm);

            new RelayMulticast(serverSettings.getMulticastPort(), serverSettings.getRelayPort(), serverSettings.getCurrentDomain(), net, serverSettings.getMulticastAddress());
            RelayManager relayManager = new RelayManager(taskFactory, serverSettings.getRelayPort(), aesgcm);
            (new Thread(relayManager)).start();

            ClientManager clientManager = new ClientManager(taskFactory, serverSettings.getUnicastPort());
            (new Thread(clientManager)).start();

            Executor executor = new Executor(taskList, clientManager, relayManager, serverSettings, dataInterface);
            (new Thread(executor)).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setSystemProperty(final String[] args) {
        System.setProperty("javax.net.ssl.keyStore", args[0]);
        System.setProperty("javax.net.ssl.keyStorePassword", args[1]);
    }

}
