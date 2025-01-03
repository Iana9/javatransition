package TaskTracker;

public class InMemoryHistoryManager implements HistoryManager {
    private static DefaultTask[] taskHistory = new DefaultTask[10];
    protected static int taskInHistory = 0;
    
    public DefaultTask[] getHistory() {
        return taskHistory;
    }

    public void add(DefaultTask task) {
        taskHistory[taskInHistory % 10] = task;
        ++taskInHistory;
    }
}
