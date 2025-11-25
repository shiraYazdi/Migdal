package Service;

import Models.Status;
import Models.Task;
import Repositories.TaskRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    //Finds a task by id using repo.getById, Changes its status to DONE and updates the information in the repository
    public void markAsDone(int id){
        Task t = repo.getById(id);
        if (t != null){
            t.setStatus(Status.DONE);
            repo.update(t.getId(), t);
        }
    }

    //Performs a free search in the task list by title or description
    public List<Task> search(String text){
        String query = text.toLowerCase();
        //Retrieves all tasks from the repository. Filters tasks by the condition:
        //The text appears in //the //title or The text appears in the description.
        //Returns a list of all tasks that match.
        return repo.listAll().stream().filter(
                t -> t.getTitle().toLowerCase().contains(query) ||
                        t.getDescription().toLowerCase().contains(query)
        ).collect(Collectors.toList());
    }

    //Returns all tasks, sorted by their status
    public List<Task> listSortedByStatus(){
        //Retrieves all tasks from the repository (repo.listAll)
        //Sorts them by the numerical value of the status ordinal
        return repo.listAll()
                .stream()
                .sorted(Comparator.comparingInt(t -> t.getStatus().ordinal()))
                .collect(Collectors.toList());

    }
}
