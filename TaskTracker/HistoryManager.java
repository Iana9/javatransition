package TaskTracker;

public interface HistoryManager {
    public DefaultTask[] getHistory();
    public void add(DefaultTask task);
}
