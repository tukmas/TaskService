import java.time.LocalDateTime;

public class YearlyTask extends Task implements RepeatabilityTask{
    public YearlyTask(String title, String description, TaskType taskType, LocalDateTime date) throws ParameterException {
        super(title, description, taskType, date);
    }
    @Override
    public boolean checkOccurance(LocalDateTime requestedDate) {
        return getFirstDate().getDayOfYear() == requestedDate.getDayOfYear();
    }
}