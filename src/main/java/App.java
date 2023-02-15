import org.infrastructure.json.JsonRepository;
import org.model.Server;
import org.relay.RelayMulticast;
import org.repository.DataInterface;
import org.server.Executor;
import org.server.TaskFactory;
import org.server.TaskList;
import org.server.ServerManager;

public class App {

    public static void main(String args[]) {
        try(DataInterface dataInterface = new JsonRepository()) {
            Server server =  dataInterface.getServerSettings();

            TaskList taskList = new TaskList();
            TaskFactory taskFactory = new TaskFactory(taskList);

            ServerManager serverManager = new ServerManager(taskFactory, server.getUnicastPort());
            (new Thread(serverManager)).start();

            Executor executor = new Executor(taskList, server, dataInterface);
            (new Thread(executor)).start();

            new RelayMulticast(server.getMulticastAddress(), server.getMulticastPort(), server.getRelayPort(), server.getCurrentDomain());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
