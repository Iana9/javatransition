package TaskTracker;

abstract class DefaultTask {

    String name;
    String description;
    Integer identification = null;
    String status;

    DefaultTask(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals("NEW") ||
        status.equals("IN_PROGRESS") ||
        status.equals("DONE")) {
            this.status = status;
        } else { throw new VerifyError("There is no such status as " + status); }
    }

    @Override
    public String toString() {
        return "DefaultTask [name=" + name + ", description=" + description + ", identification=" + identification
                + ", status=" + status + "]";
    }
    
}