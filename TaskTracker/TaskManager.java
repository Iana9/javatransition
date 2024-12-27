package TaskTracker;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


abstract class TaskManager {
    Map<String,ArrayList<DefaultTask>> allTasks;
    Integer identificator;

    TaskManager() {
        this.identificator = 1;
        this.allTasks = new HashMap<>();
        this.allTasks.put("tasks", new ArrayList<DefaultTask>());
        this.allTasks.put("epics", new ArrayList<DefaultTask>());
        this.allTasks.put("subtasks", new ArrayList<DefaultTask>());
    }

    public ArrayList<DefaultTask> getAllTasks(String type) {
        if (type == "tasks" || type == "epics" || type == "subtasks") {
            return allTasks.get(type);
        } else {
            throw new VerifyError("Wrong type, there are: tasks, epics, subtasks");
        }
    }

    public DefaultTask getTask(Integer id) {
        if (id > 0 && id <= this.identificator) {
            for (String ar : this.allTasks.keySet()) {
                for (DefaultTask tsk : this.allTasks.get(ar)) {
                    if (tsk.identification == id) {
                        return tsk;
                    }
                }
            }
        }
        return null;
    }

    public void putTask(DefaultTask tsk) {
        tsk.identification = this.identificator++;
        if (tsk.getClass() == Task.class) {
            allTasks.get("tasks").add(tsk);
        } else if (tsk.getClass() == SubTask.class) {
            allTasks.get("subtasks").add(tsk);
        } else if (tsk.getClass() == Epic.class) {
            allTasks.get("epics").add(tsk);
        }
    }

    public void relaceTask(DefaultTask tsk, Integer id) {
        if (id > 0 && id <= this.identificator) {
            for (String ar : this.allTasks.keySet()) {
                for (Integer idx=0; idx<this.allTasks.get(ar).size(); ++idx) {
                    if (this.allTasks.get(ar).get(idx).identification == id) {
                        this.allTasks.get(ar).set(idx, tsk);
                    }
                }
            }
        }
    }

    public void deleteTask(Integer id) {
        if (id > 0 && id <= this.identificator) {
            for (String ar : this.allTasks.keySet()) {
                for (int idx=0; idx<this.allTasks.get(ar).size(); ++idx) {
                    if (this.allTasks.get(ar).get(idx).identification == id) {
                        this.allTasks.get(ar).remove(idx);
                    }
                }
            }
        }
    }

    public void deleteAllTasks() {
        allTasks.clear();
    }

    public ArrayList<SubTask> getSubtasks(Epic epic) {
        return epic.SubTasks;
    }
    
}
