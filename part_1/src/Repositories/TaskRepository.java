package Repositories;

import Models.Status;
import Models.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private final Path filePath;
    private List<Task> tasks = new ArrayList<>();

    public TaskRepository(String filePath) {
        this.filePath = Path.of(filePath);
        System.out.println(filePath);
        load();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    //load data from db
    private void load(){
        try {
            //Checks if the data file exists.
            if (!Files.exists(filePath)) {
                tasks = new ArrayList<>();
                return;
            }
            String json = Files.readString(filePath).trim();
            //Cheks if the content is empty
            if (json.isEmpty() || json.equals("[]")){
                tasks = new ArrayList<>();
                return;
            }
            //Remove the outer parentheses of the array: []
            json = json.substring(1, json.length()-1).trim();
            //Split each object according to a pattern that recognizes a closing parenthesis
            String[] items = json.split("(?<=\\}),");

            //for each task
            for (String item : items){
                String clean = item.trim();
                //Remove the parentheses {}
                clean = clean.substring(1, clean.length() - 1);
                //Split into //key:value pairs using splitJsonPairs
                List<String> pairs = splitJsonPairs(clean);


                int id = 0;
                String title = "";
                String description = "";
                Status status = Status.NEW;

                for (String p: pairs){
                    //Divide each line to key-value by splitting
                    String[] element = p.split(":", 2);
                    String key = element[0].replace("\"", "" ).trim();
                    String value = element[1].replace("\"", "").trim();

                    //Convert each value to the appropriate type:
                    switch (key){
                        case  "id" -> id = Integer.parseInt(value);
                        case "title" -> title = value;
                        case "description" -> description = value;
                        case "status" -> status = Status.valueOf(value);
                    }
                }
                //Add //task to //tasks list
                tasks.add(new Task(id, title, description, status));
            }

            //Calc the max id and reset nextId
            int maxId = tasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0);
            Task.resetNextId(maxId + 1);
            //In case of error: Print the error, Load an empty list
        }catch (Exception e){
            e.printStackTrace();
            tasks = new ArrayList<>();
        }
    }

    //Save Data
    private void save(){
        //Creating a JSON string using StringBuilder
        StringBuilder sb = new StringBuilder();
        //Opening the array using "["
        sb.append("[\n");

        //Going through all the tasks in the list:
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);

            //Building a JSON object
            sb.append("{");
            sb.append("\"id\":").append(t.getId()).append(",");
            sb.append("\"title\":\"").append(t.getTitle()).append("\",");
            sb.append("\"description\":\"").append(t.getDescription()).append("\",");
            sb.append("\"status\":\"").append(t.getStatus()).append("\"");
            sb.append("}");

            //Adding a comma at the end of each object, except the last one
            if (i < tasks.size() - 1) sb.append(",");
            sb.append("\n");
        }
        //Closing the array using //[]
        sb.append("]");

        try {
            //Physically writing the string to a file using Files.writeString.
            Files.writeString(filePath, sb.toString());
        }
        //In case of an I/O error: Printing the error to the terminal.
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Task task){
        tasks.add(task);
        save();
    }

    public Task getById(int id){
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    public void update(int id, Task newTask){
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                tasks.set(i, newTask);
                save();
                return;
            }
        }
    }

    public void delete(int id){
        tasks.removeIf(t -> t.getId() == id);
        save();
    }

    public List<Task> listAll(){
        return new ArrayList<>(tasks);
    }
    //This function splits a JSON string of //key:value pairs into a list of strings,
    // but in a way that safely does not break values that contain a comma inside quotes.
    private static List<String> splitJsonPairs(String json){
        //Creates a result list and a sequence (StringBuilder) to build the current pair.
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        //Iterates over each character in the string.
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            //Every time quotes (") appear The value of insideQuotes is flipped
            if (c == '"'){
                insideQuotes = !insideQuotes;
            }
            //This is a boundary between JSON pairs: The current pair is added to the list.
            // The StringBuilder is reset to build the next pair.
            if (c == ',' && !insideQuotes){
                result.add(current.toString().trim());
                current.setLength(0);
            }
            //Any other characters are simply added to the current string
            else {
                current.append(c);
            }
        }
        //At the end of the loop, if there is content left in the StringBuilder, It is added as the last result
        if (current.length() > 0){
            result.add(current.toString().trim());
        }
        //Returns the list of all pairs
        return result;
    }

}

