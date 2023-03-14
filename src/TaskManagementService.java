import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TaskManagementService {
    private static final Map<Integer, RepeatabilityTask> actualTasks = new HashMap<>();
    private static final Map<Integer, RepeatabilityTask> archivedTasks = new HashMap<>();
    public static void addTask(Scanner scanner) {
        try {
            scanner.nextLine();
            System.out.println("Введите название задачи:");
            String title = ValidateUtils.checkString(scanner.nextLine());
            System.out.println("Введите описание задачи:");
            String description = ValidateUtils.checkString(scanner.nextLine());
            System.out.println("Введите тип задачи: 0 - Рабочая, 1 - Личная.");
            TaskType taskType = TaskType.values()[scanner.nextInt()];
            System.out.println("Введите повторяемость задачи: 0 - Однократная, 1 - Ежедневная, 2 - Еженедельная, 3 - Ежемесячная, 4 - Ежегодная.");
            int occurrence = scanner.nextInt();
            System.out.println("Введите дату в формате: dd.MM.yyyy HH:mm");
            scanner.nextLine();
            createEvent(scanner, title, description, taskType, occurrence);
            System.out.println("Для выхода нажмите Enter\n");
            scanner.nextLine();
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            ;
        }
    }
    private static void createEvent(Scanner scanner, String title, String description, TaskType taskType, int occurance) {
        try {
            LocalDateTime eventDate = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            RepeatabilityTask task = null;
            try {
                task = createTask(occurance, title, description, taskType, eventDate);
                System.out.println("Создана задача " + task);
            } catch (ParameterException e) {
                System.out.println(e.getMessage());
            }
        } catch (DateTimeParseException e) {
            System.out.println("Проверьте формат dd.MM.yyyy HH:mm и попробуйте ещё раз");
            createEvent(scanner, title, description, taskType, occurance);
        }
    }
    public static void editTask(Scanner scanner) {
        try {
            System.out.println("Редактирование задачи: введите id");
            printActualTasks();
            int id = scanner.nextInt();
            if (!actualTasks.containsKey(id)) {
                throw new ParameterException("Задачи не найдены");
            }
            System.out.println("Редактирование: 0 - заголовка, 1 - описания, 2 - типа, 3 - даты.");
            int menuCase = scanner.nextInt();
            switch (menuCase) {
                case 0 -> {
                    scanner.nextLine();
                    System.out.println("Введите название задачи: ");
                    String title = scanner.nextLine();
                    RepeatabilityTask task = actualTasks.get(id);
                    task.setTitle(title);
                }
                case 1 -> {
                    scanner.nextLine();
                    System.out.println("Введите описание задачи: ");
                    String description = scanner.nextLine();
                    RepeatabilityTask task = actualTasks.get(id);
                    task.setDescription(description);
                }
                case 2 -> {
                    scanner.nextLine();
                    System.out.println("Введите тип задачи: 0 - Рабочая, 1 - Личная.");
                    String  taskType = scanner.nextLine();
                    RepeatabilityTask task = actualTasks.get(id);
                    task.setTitle(taskType);
                }
                case 3 -> {
                    scanner.nextLine();
                    System.out.println("Введите дату задачи: ");
                    String date = scanner.nextLine();
                    RepeatabilityTask task = actualTasks.get(id);
                    task.setTitle(date);
                }
            }
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteTask(Scanner scanner) {
        System.out.println("Текущие задачи\n");
        printActualTasks();
        System.out.println("Для удаления введите id задачи\n");
        int id = scanner.nextInt();
        if (actualTasks.containsKey(id)) {
            RepeatabilityTask removedTask = actualTasks.get(id);
            removedTask.setArchived(true);
            archivedTasks.put(id, removedTask);
            System.out.println("Задача " + id + " удалена\n");
        } else {
            System.out.println("Такой задачи не существует\n");
        }
    }

    public static void getTaskByDay(Scanner scanner) {
        System.out.println("Ведите дату как: dd.MM.yyyy");
        try {
            String date = scanner.next();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate requestedDate = LocalDate.parse(date, dateFormatter);
            List<RepeatabilityTask> foundEvents = findTaskByDate(requestedDate);
            System.out.println("События на " + requestedDate + ":");
            for (RepeatabilityTask task : foundEvents) {
                System.out.println(task);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Проверьте формат dd.MM.yyyy  и попробуйте ещё раз");
        }
        scanner.nextLine();
        System.out.println("Для выхода нажмите Enter\n");
    }

    public static void printArchivedTasks() {
        for (RepeatabilityTask task : archivedTasks.values()) {
            System.out.println(task);
        }
    }
    public static void getGroupedByDate(){
        Map<LocalDate, ArrayList<RepeatabilityTask>> taskMap = new HashMap<>();
        for (Map.Entry<Integer, RepeatabilityTask> entry : actualTasks.entrySet()) {
            RepeatabilityTask task = entry.getValue();
            LocalDate localDate = task.getFirstDate().toLocalDate();
            if (taskMap.containsKey(localDate)){
                taskMap.get(localDate).add(task);
            } else {
                taskMap.put(localDate, new ArrayList<>(Collections.singletonList(task)));
            }
        }
        for (Map.Entry<LocalDate, ArrayList<RepeatabilityTask>> taskEntry : taskMap.entrySet()){
            System.out.println(taskEntry.getKey() + " : " + taskEntry.getValue());
        }
    }
    private static List<RepeatabilityTask> findTaskByDate (LocalDate date){
        List<RepeatabilityTask> tasks = new ArrayList<>();
        for (RepeatabilityTask task : actualTasks.values()){
            if (task.checkOccurance(date.atStartOfDay())){
                tasks.add(task);
            }
        }
        return tasks;
    }
    private static RepeatabilityTask createTask(int occurance, String title, String description, TaskType taskType, LocalDateTime localDateTime) throws ParameterException{
        return switch (occurance){
            case 0 -> {
                OncelyTask oncelyTask = new OncelyTask(title, description, taskType, localDateTime);
                actualTasks.put(oncelyTask.getId(), oncelyTask);
                yield oncelyTask;
            }
            case 1 -> {
                DailyTask task = new DailyTask(title, description, taskType, localDateTime);
                actualTasks.put(task.getId(), task);
                yield task;
            }
            case 2 -> {
                WeeklyTask task = new WeeklyTask(title, description, taskType, localDateTime);
                actualTasks.put(task.getId(), task);
                yield task;
            }
            case 3 -> {
                MonthlyTask task = new MonthlyTask(title, description, taskType, localDateTime);
                actualTasks.put(task.getId(), task);
                yield task;
            }
            case 4 -> {
                YearlyTask task = new YearlyTask(title, description, taskType, localDateTime);
                actualTasks.put(task.getId(), task);
                yield task;
            }
            default -> null;
        };
    }
    public static void printActualTasks() {
        for (RepeatabilityTask task : actualTasks.values()) {
            System.out.println(task);
        }
    }
}
