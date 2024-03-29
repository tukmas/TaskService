import java.time.LocalDateTime;

public class OncelyTask extends Task implements RepeatabilityTask {

    public OncelyTask(String title, String description, TaskType taskType, LocalDateTime date) throws ParameterException {
        super(title, description, taskType, date);
    }

    @Override
    public boolean checkOccurance(LocalDateTime requestedDate) {
        return getFirstDate().toLocalDate().equals(requestedDate.toLocalDate());
    }
}

