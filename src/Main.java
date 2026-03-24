import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void showMenu() {
        System.out.println();
        System.out.println("1. Добавить задачу");
        System.out.println("2. Показать все задачи");
        System.out.println("3. Удалить задачу");
        System.out.println("4. Отметить задачу как выполненную");
        System.out.println("0. Выход");
    }

    public static void showTasks(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }

        System.out.println("Список задач:");
        int index = 1;
        for (Task task : tasks) {
            String status = task.isDone() ? "✅" : "❌";
            System.out.println(index + ". " + status + " " + task.getTitle());
            index++;
        }
    }

    public static void addTask(ArrayList<Task> tasks, Scanner scanner) throws IOException {
        scanner.nextLine();
        System.out.print("Введите задачу: ");
        String title = scanner.nextLine();
        tasks.add(new Task(title));
        saveTasksToFile(tasks);
        System.out.println("Задача добавлена");
    }

    public static void removeTask(ArrayList<Task> tasks, Scanner scanner) throws IOException {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }

        showTasks(tasks);
        System.out.print("Введите номер задачи для удаления: ");
        int taskNumber = scanner.nextInt();

        if (taskNumber > 0 && taskNumber <= tasks.size()) {
            tasks.remove(taskNumber - 1);
            saveTasksToFile(tasks);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Некорректный номер");
        }
    }

    public static void markTaskDone(ArrayList<Task> tasks, Scanner scanner) throws IOException {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }

        showTasks(tasks);
        System.out.print("Введите номер выполненной задачи: ");
        int taskNumber = scanner.nextInt();

        if (taskNumber > 0 && taskNumber <= tasks.size()) {
            tasks.get(taskNumber - 1).markAsDone();
            saveTasksToFile(tasks);
            System.out.println("Задача отмечена как выполненная");
        } else {
            System.out.println("Некорректный номер");
        }
    }

    public static void saveTasksToFile(ArrayList<Task> tasks) throws IOException {
        FileWriter writer = new FileWriter("tasks.txt");

        for (Task task : tasks) {
            writer.write(task.toFileString());
            writer.write("\n");
        }

        writer.close();
    }

    public static void loadTasksFromFile(ArrayList<Task> tasks) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(";");
            if (parts.length != 2) {
                continue;
            }

            String title = parts[0];
            boolean isDone = Boolean.parseBoolean(parts[1]);

            Task task = new Task(title, isDone);
            tasks.add(task);
        }

        reader.close();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            loadTasksFromFile(tasks);
        } catch (IOException e) {
            System.out.println("Файл задач пока не найден. Будет создан новый.");
        }

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            showMenu();
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addTask(tasks, scanner);
                    break;
                case 2:
                    showTasks(tasks);
                    break;
                case 3:
                    removeTask(tasks, scanner);
                    break;
                case 4:
                    markTaskDone(tasks, scanner);
                    break;
                case 0:
                    System.out.println("Выход из программы");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Неверный пункт меню");
            }
        }

        scanner.close();
    }
}