public class ValidateUtils {
    public static String checkString(String str) throws ParameterException{
        if (str == null || str.isEmpty() || str.isBlank()) {
            throw new ParameterException("Некорректный ввод");
        } else {
            return str;
        }
    }
}
