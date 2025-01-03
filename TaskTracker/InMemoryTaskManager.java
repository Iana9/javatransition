package TaskTracker;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    Map<String,ArrayList<DefaultTask>> allTasks;
    static Integer identificator = 1;
    static protected InMemoryHistoryManager histManager = new InMemoryHistoryManager();

    InMemoryTaskManager() {
        this.allTasks = new HashMap<>();
        this.allTasks.put("tasks", new ArrayList<DefaultTask>());
        this.allTasks.put("epics", new ArrayList<DefaultTask>());
        this.allTasks.put("subtasks", new ArrayList<DefaultTask>());
    }

    @Override
    public ArrayList<DefaultTask> getAllTasks(String type)
    {
        if (type == "tasks" || type == "epics" || type == "subtasks") {
            return allTasks.get(type);
        } else {
            throw new VerifyError("Wrong type, there are: tasks, epics, subtasks");
        }
    }

    @Override
    public DefaultTask getTask(Integer id)
    {
        if (id > 0 && id <= identificator) {
            for (String ar : this.allTasks.keySet()) {
                for (DefaultTask tsk : this.allTasks.get(ar)) {
                    if (tsk.identification == id) {
                        histManager.add(tsk);
                        return tsk;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void putTask(DefaultTask tsk)
    {
        tsk.identification = identificator++;
        if (tsk.getClass() == Task.class) {
            allTasks.get("tasks").add(tsk);
        } else if (tsk.getClass() == SubTask.class) {
            allTasks.get("subtasks").add(tsk);
        } else if (tsk.getClass() == Epic.class) {
            allTasks.get("epics").add(tsk);
        }
    }

    @Override
    public void replaceTask(DefaultTask tsk, Integer id)
    {
        if (id > 0 && id <= identificator) {
            for (String ar : this.allTasks.keySet()) {
                for (Integer idx=0; idx<this.allTasks.get(ar).size(); ++idx) {
                    if (this.allTasks.get(ar).get(idx).identification == id) {
                        this.allTasks.get(ar).set(idx, tsk);
                    }
                }
            }
        }
    }

    @Override
    public void deleteTask(Integer id)
    {
        if (id > 0 && id <= identificator) {
            for (String ar : this.allTasks.keySet()) {
                for (int idx=0; idx<this.allTasks.get(ar).size(); ++idx) {
                    if (this.allTasks.get(ar).get(idx).identification == id) {
                        this.allTasks.get(ar).remove(idx);
                    }
                }
            }
        }
    }

    @Override
    public void deleteAllTasks()
    {
        allTasks.clear();
    }

    @Override
    public ArrayList<SubTask> getSubtasks(Epic epic)
    {
        return epic.SubTasks;
    }
}
