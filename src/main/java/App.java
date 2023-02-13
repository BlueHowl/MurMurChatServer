import org.infrastructure.json.JsonRepository;
import org.model.Server;
import org.repository.DataInterface;
import org.task.TaskHandler;
import org.task.TaskList;
import org.thread.ClientListener;

public class App {

    public static void main(String args[]) {
        try(DataInterface dataInterface = new JsonRepository()) {
            Server server =  dataInterface.getServerSettings();

            TaskList taskList = new TaskList();
            TaskHandler taskHandler = new TaskHandler(taskList, server, dataInterface);
            (new Thread(taskHandler)).start();

            ClientListener clientListener = new ClientListener(taskHandler, server.getUnicastPort());
            (new Thread(clientListener)).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
