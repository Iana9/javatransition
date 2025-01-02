package TaskTracker;

abstract class DefaultTask {

    String name;
    String description;
    Integer identification = null;
    STATUS status;

    protected enum STATUS {
        NEW,
        IN_PROGRESS,
        DONE
    }

    DefaultTask(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = STATUS.NEW;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public int getIdentification() {
        return identification;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status.toUpperCase();
        try {
            this.status = STATUS.valueOf(status);
        } catch (Exception e)
        { throw new VerifyError("There is no such status as " + status); }
    }

    @Override
    public String toString() {
        return "DefaultTask [name=" + name + ", description=" + description + ", identification=" + identification
                + ", status=" + status + "]";
    }
    
}