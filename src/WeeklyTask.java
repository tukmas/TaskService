import java.time.LocalDateTime;

public class WeeklyTask extends Task implements RepeatabilityTask{
    public WeeklyTask(String title, String description, TaskType taskType, LocalDateTime date) throws ParameterException {
        super(title, description, taskType, date);
    }

    @Override
    public boolean checkOccurance(LocalDateTime requestedDate) {
        return getFirstDate().getDayOfWeek().equals(requestedDate.getDayOfWeek());
    }
    @Override
    public void setArchived(boolean archived) {

    }
}
