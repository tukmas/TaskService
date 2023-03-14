import java.time.LocalDateTime;

public class MonthlyTask extends Task implements RepeatabilityTask {
    public MonthlyTask(String title, String description, TaskType taskType, LocalDateTime date) throws ParameterException {
        super(title, description, taskType, date);
    }

    @Override
    public boolean checkOccurance(LocalDateTime requestedDate) {
      return getFirstDate().getDayOfMonth() == requestedDate.getDayOfMonth();
    }
}
