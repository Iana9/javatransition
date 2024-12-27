package TaskTracker;

import java.util.ArrayList;

public class Epic extends DefaultTask {
    ArrayList<SubTask> SubTasks;
    String name;
    String description;

    Epic(String name, String description) {
        super(name, description);
        this.SubTasks = new ArrayList<>();
    }

    public void createSubTask(String name, String description) {
        SubTasks.add(new SubTask(this, name, description));
    }

    protected void addSubTask(SubTask subTask) {
        SubTasks.add(subTask);
    }
}
