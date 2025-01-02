package TaskTracker;

import java.util.ArrayList;


public interface TaskManager {
    public ArrayList<DefaultTask> getAllTasks(String type);
    public DefaultTask getTask(Integer id);
    public void putTask(DefaultTask tsk);
    public void replaceTask(DefaultTask tsk, Integer id);
    public void deleteTask(Integer id);
    public void deleteAllTasks();
    public ArrayList<SubTask> getSubtasks(Epic epic);
}
