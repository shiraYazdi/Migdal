package Models;

public class Task {
    static int nextId = 1;
    private final int id;
    private String title;
    private String description;
    private Status status;

    //Constructor for //create
    public Task(String title, String description) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    //Constructor for update
    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public static void resetNextId(int value) {
        nextId = value;
    }

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "id: " + id +
                ", title: " + title +
                ", description: " + description +
                ", status: " + status;
    }
}
