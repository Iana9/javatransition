package TaskTracker;
import java.util.List;

public interface HistoryManager {
    public List<DefaultTask> getHistory();
    public void add(DefaultTask task);
    public void remove(int id);
}
