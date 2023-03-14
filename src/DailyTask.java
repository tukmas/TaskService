import java.time.LocalDateTime;

public class DailyTask extends Task implements RepeatabilityTask {
    public DailyTask(String title, String description, TaskType taskType, LocalDateTime date) throws ParameterException {
        super(title, description, taskType, date);
    }

    @Override
    public boolean checkOccurance(LocalDateTime requestedDate) {
        return getFirstDate().toLocalDate().isBefore(requestedDate.toLocalDate()) || getFirstDate().toLocalDate().equals(requestedDate.toLocalDate());
    }
}
