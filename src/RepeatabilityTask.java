import java.time.LocalDateTime;

public interface RepeatabilityTask {

    boolean checkOccurance(LocalDateTime localDateTime);

    void setTitle(String title);

    void setDescription(String description);

    LocalDateTime getFirstDate();

    void setArchived(boolean archived);

}