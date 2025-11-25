import Models.Status;
import Models.Task;
import Repositories.TaskRepository;
import Service.TaskService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        TaskRepository repo = new TaskRepository("src/data/tasks.json");
        TaskService service = new TaskService(repo);

        while (true) {
            System.out.println("\n=== Task Manager ===");
            System.out.println("1. Add Task");
            System.out.println("2. Update Task");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task as DONE");
            System.out.println("5. Search Tasks");
            System.out.println("6. List All Tasks");
            System.out.println("7. List Tasks Sorted By Status");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {

                case 1:
                    System.out.print("Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Description: ");
                    String desc = scanner.nextLine();

                    System.out.println("Status (NEW, IN_PROGRESS, DONE): ");
                    String status = scanner.nextLine();

                    Task newTask = new Task(title, desc);
                    repo.add(newTask);
                    System.out.println("Task added.");
                    break;

                case 2:
                    System.out.print("Enter Task ID to update: ");
                    int upId = Integer.parseInt(scanner.nextLine());

                    Task t = repo.getById(upId);
                    if (t == null) {
                        System.out.println("Task not found.");
                        break;
                    }

                    System.out.print("New Title: ");
                    t.setTitle(scanner.nextLine());

                    System.out.print("New Description: ");
                    t.setDescription(scanner.nextLine());

                    System.out.print("New Status (NEW, IN_PROGRESS, DONE): ");
                    t.setStatus(Status.valueOf(scanner.nextLine()));

                    repo.update(t.getId(), t);
                    System.out.println("Task updated.");
                    break;

                case 3:
                    System.out.print("Enter Task ID to delete: ");
                    int delId = Integer.parseInt(scanner.nextLine());

                    repo.delete(delId);
                    System.out.println("Task deleted.");
                    break;

                case 4:
                    System.out.print("Enter Task ID to mark DONE: ");
                    int doneId = Integer.parseInt(scanner.nextLine());

                    service.markAsDone(doneId);
                    System.out.println("Task marked as DONE.");
                    break;

                case 5:
                    System.out.print("Enter text to search: ");
                    String text = scanner.nextLine();

                    List<Task> found = service.search(text);
                    System.out.println("Found Tasks:");
                    found.forEach(System.out::println);
                    break;

                case 6:
                    System.out.println("All Tasks:");
                    repo.listAll().forEach(System.out::println);
                    break;

                case 7:
                    System.out.println("Tasks sorted by status:");
                    service.listSortedByStatus().forEach(System.out::println);
                    break;

                case 0:
                    System.out.println("Bye!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }

    }
}