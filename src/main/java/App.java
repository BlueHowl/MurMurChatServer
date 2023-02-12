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
            Server server =  dataInterface.getServerSettings();

            TaskList taskList = new TaskList();
            TaskFactory taskFactory = new TaskFactory(taskList);

            ClientListener clientListener = new ClientListener(taskFactory, server.getUnicastPort());
            (new Thread(clientListener)).start();
            MurMurExecutor murMurExecutor = new MurMurExecutor(taskList, server);
            (new Thread(murMurExecutor)).start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
