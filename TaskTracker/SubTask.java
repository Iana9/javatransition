package TaskTracker;

public class SubTask extends DefaultTask {
    Epic task;

    SubTask(Epic task, String name, String description) {
        super(name, description);
        this.task = task;
        this.task.addSubTask(this);
    }

    @Override
    public void setStatus(String status) {
        super.setStatus(status);
        if (this.status == STATUS.DONE) {
            boolean MainTask = true;
            for (SubTask i : this.task.SubTasks) {
                MainTask = MainTask && (i.status == STATUS.DONE);
            }
            if (MainTask) {
                this.task.status = STATUS.DONE;
            }
        }
    }

}
