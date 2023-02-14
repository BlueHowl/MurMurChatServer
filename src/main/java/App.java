import org.infrastructure.json.JsonRepository;
import org.model.Server;
import org.repository.DataInterface;
import org.server.Executor;
import org.server.TaskFactory;
import org.server.TaskList;
import org.client.ClientListener;

public class App {

    public static void main(String args[]) {
        try(DataInterface dataInterface = new JsonRepository()) {
            Server server =  dataInterface.getServerSettings();

            TaskList taskList = new TaskList();
            TaskFactory taskFactory = new TaskFactory(taskList);

            ClientListener clientListener = new ClientListener(taskFactory, server.getUnicastPort());
            (new Thread(clientListener)).start();
            Executor executor = new Executor(taskList, server, dataInterface);
            (new Thread(executor)).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
