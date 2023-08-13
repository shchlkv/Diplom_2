import org.apache.commons.lang3.RandomStringUtils;

public class TestData {
    static String email = ("email" + RandomStringUtils.randomAlphanumeric(10) + "@ya.ru").toLowerCase();
    static String password = RandomStringUtils.randomAlphanumeric(12);
    static String name = "Василий Пупкин_" + RandomStringUtils.randomNumeric(10);

}
