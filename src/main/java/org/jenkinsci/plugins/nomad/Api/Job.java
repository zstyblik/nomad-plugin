package org.jenkinsci.plugins.nomad.Api;

public final class Job {

    private String ID;
    private String Name;
    private String Region;
    private String Type;
    private Integer Priority;
    private String[] Datacenters;
    private TaskGroup[] TaskGroups;
    private Constraints[] Constraints;

    public Job(
        String ID,
        String name,
        String region,
        String type,
        Integer priority,
        String[] datacenters,
        TaskGroup[] taskGroups,
        Constraints[] constraints)
    {
        this.ID = ID;
        Name = name;
        Region = region;
        Type = type;
        Priority = priority;
        Datacenters = datacenters;
        TaskGroups = taskGroups;
        Constraints = constraints;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Integer getPriority() {
        return Priority;
    }

    public void setPriority(Integer priority) {
        Priority = priority;
    }

    public String[] getDatacenters() {
        return Datacenters;
    }

    public void setDatacenters(String[] datacenters) {
        Datacenters = datacenters;
    }

    public TaskGroup[] getTaskGroups() {
        return TaskGroups;
    }

    public void setTaskGroups(TaskGroup[] taskGroups) {
        TaskGroups = taskGroups;
    }

    public Constraints[] getConstraints() {
        return Constraints;
    }

    public void setConstraints(Constraints[] constraints) {
        Constraints = constraints;
    }

}
