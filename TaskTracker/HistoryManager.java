package TaskTracker;

public interface HistoryManager {
    public void add(DefaultTask task);
    public DefaultTask[] getHistory();
}
