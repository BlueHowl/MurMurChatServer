import org.infrastructure.json.JsonRepository;
import org.model.Server;
import org.repository.DataInterface;
import org.task.TaskFactory;
import org.task.TaskList;
import org.thread.ClientListener;
import org.thread.MurMurExecutor;

public class App {

    public static void main(String args[]) {
        try(DataInterface dataInterface = new JsonRepository()) {
            Server serverSettings =  dataInterface.getServerSettings();

            TaskList taskList = new TaskList();
            TaskFactory taskFactory = new TaskFactory(taskList);

            ClientListener clientListener = new ClientListener(taskFactory, serverSettings.getMulticastPort()); //TODO changer port ?
            (new Thread(clientListener)).start();
            MurMurExecutor murMurExecutor = new MurMurExecutor(taskList, serverSettings);
            (new Thread(murMurExecutor)).start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
