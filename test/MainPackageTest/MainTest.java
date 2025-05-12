package MainPackageTest;

import SpamFiltersPackage.CompositeSpamFilter;
import UsersMessagePackage.*;
import MainPackage.Main;
import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private static CompositeSpamFilter compositeSpamFilter;

    @BeforeEach
    void setUp() {
        Main.userStorage = new UserStorage();
        compositeSpamFilter = new CompositeSpamFilter();
    }

    @Test
    void addUserTest(){
        String userName = "Lenya";
        String emptyUserName = "";
        assertEquals("Пользователь " + userName + " добавлен", Main.addUser(userName));
        assertEquals("Пользователь " + userName + " уже существует", Main.addUser(userName));
        assertEquals("Имя пользователя не может быть пустым", Main.addUser(emptyUserName));
    }

    @Test
    void usersListTest(){
        User user1 = new User("Lenya");
        User user2 = new User("Леня");
        List<String> emptyAllUsersList = Main.usersList();
        assertEquals("В системе нет пользователей", emptyAllUsersList.getFirst());

        Main.userStorage.addUser(user1);
        Main.userStorage.addUser(user2);
        List<String> newAllUsersList = Main.usersList();
        assertEquals("* Леня", newAllUsersList.getFirst());
        assertEquals("* Lenya", newAllUsersList.getLast());
    }

    @Test
    void emptyBoxesTest(){
        String userName = "Lenya";
        List<String> notExistingInboxList = Main.getInboxMessages(userName);
        List<String> notExistingOutboxList = Main.getOutboxMessages(userName);
        List<String> notExistingSpamList = Main.getSpamMessages(userName);

        assertEquals("Такого пользователя не существует. " +
                "\nЕсли хотите продолжить, введите команду заново", notExistingInboxList.getFirst());
        assertEquals("Такого пользователя не существует. " +
                "\nЕсли хотите продолжить, введите команду заново", notExistingOutboxList.getFirst());
        assertEquals("Такого пользователя не существует. " +
                "\nЕсли хотите продолжить, введите команду заново", notExistingSpamList.getFirst());

        Main.userStorage.addUser(new User(userName));
        List<String> emptyInboxList = Main.getInboxMessages(userName);
        List<String> emptyOutboxList = Main.getOutboxMessages(userName);
        List<String> emptySpamList = Main.getSpamMessages(userName);

        assertEquals("Сообщений этому пользователю пока нет",
                emptyInboxList.getFirst());
        assertEquals("Этот пользователь пока не писал никому сообщения",
                emptyOutboxList.getFirst());
        assertEquals("Спам сообщений этому пользователю пока нет",
                emptySpamList.getFirst());
    }

    @Test
    void notEmptyBoxesTest(){
        User sender = new User("Lenya");
        User receiver = new User("Леня");

        Main.userStorage.addUser(sender);
        Main.userStorage.addUser(receiver);

        sender.sendMessage(receiver, "caption", "text");
        receiver.setSpamFilter(message -> true);
        sender.sendMessage(receiver, "spam caption", "spam text");

        List<String> receiverInbox = Main.getInboxMessages("Леня");
        List<String> senderOutbox = Main.getOutboxMessages("Lenya");
        List<String> receiverSpam = Main.getSpamMessages("Леня");

        String receiverInboxExpectedString = String.join("\n",
                "--------------------",
                "Сообщение от пользователя " + "Lenya",
                "Заголовок: " + "caption",
                "Текст: " + "text",
                "--------------------");

        String receiverSpamExpectedString = String.join("\n",
                "--------------------",
                "Спам сообщение от пользователя " + "Lenya",
                "Заголовок: " + "spam caption",
                "Текст: " + "spam text",
                "--------------------");

        String senderOutboxExpectedString1 = String.join("\n",
                "--------------------",
                "Сообщение для пользователя " + "Леня",
                "Заголовок: " + "caption",
                "Текст: " + "text",
                "--------------------");

        String senderOutboxExpectedString2 = String.join("\n",
                "--------------------",
                "Сообщение для пользователя " + "Леня",
                "Заголовок: " + "spam caption",
                "Текст: " + "spam text",
                "--------------------");

        assertEquals(receiverInboxExpectedString, receiverInbox.getFirst());
        assertEquals(receiverSpamExpectedString, receiverSpam.getFirst());
        assertEquals(senderOutboxExpectedString1, senderOutbox.getFirst());
        assertEquals(senderOutboxExpectedString2, senderOutbox.getLast());

    }

    @Test
    void isNumericTest(){
        assertTrue(Main.isNumeric("52"));
        assertFalse(Main.isNumeric("q12w"));
    }
    @Test
    void isWordTest(){
        assertTrue(Main.isWord("52qwe"));
        assertFalse(Main.isNumeric("qwe13*"));
    }

    @Test
    void setSpamFilterTest(){
        
        String notExistingUser = Main.setSpamFilter("Lenya", compositeSpamFilter);
        assertEquals("Такого пользователя не существует. " +
                "\nЕсли хотите продолжить, введите команду заново", notExistingUser);

        User receiver = new User("Леня");
        Main.userStorage.addUser(receiver);

        String doneSpamFilterEmpty = Main.processSetSpamFilter(compositeSpamFilter, receiver, "done");
        assertEquals("Вы не добавили ни одного фильтра", doneSpamFilterEmpty);

        String setSimpleSpamFilter = Main.processSetSpamFilter(compositeSpamFilter, receiver,
                "simple");
        assertEquals("Спам фильтр записан и будет добавлен при вводе команды \"done\"",
                setSimpleSpamFilter);

        String incorrectFilter = Main.processSetSpamFilter(compositeSpamFilter, receiver, "1q");
        assertEquals("Некорректный фильтр, введите заново", incorrectFilter);

        String doneSpamFilter = Main.processSetSpamFilter(compositeSpamFilter, receiver, "done");
        assertEquals("Фильтры заданы", doneSpamFilter);
    }

    @Test
    void setKeywordsFilterTest() {
        
        String spamKeywordsNotWords = "qwe1___,+re4==f";
        String spamKeywordsIsWords = "qw1e qew 4we frd";
        String expectedStringIsWord = "Спам фильтр записан и будет добавлен при вводе команды \"done\"";
        String expectedStringStringNotWord = "Некорректный ввод";

        String setKeywordsSpamFiltersIsWords =
                Main.setKeywordsSpamFilter(compositeSpamFilter, spamKeywordsIsWords);
        String setKeywordsSpamFiltersNotWords =
                Main.setKeywordsSpamFilter(compositeSpamFilter, spamKeywordsNotWords);

        assertEquals(expectedStringIsWord, setKeywordsSpamFiltersIsWords);
        assertEquals(expectedStringStringNotWord, setKeywordsSpamFiltersNotWords);
    }

    @Test
    void setRepetitionSpamFilter(){
        

        String notNumericMaxRepetition = "42q";
        String numericMaxRepetition = "42";

        String setNotNumericRepetitionFilter = Main.setRepetitionSpamFilter(compositeSpamFilter,
                notNumericMaxRepetition);
        String setNumericRepetitionFilter = Main.setRepetitionSpamFilter(compositeSpamFilter,
                numericMaxRepetition);

        assertEquals("Необходимо ввести число, введите фильтр еще раз",
                setNotNumericRepetitionFilter);
        assertEquals("Спам фильтр записан и будет добавлен при вводе команды \"done\"",
                setNumericRepetitionFilter);
    }

    @Test
    void setSenderSpamFilterTest() {

        User sender = new User("Lenya");
        User receiver = new User("Леня");

        Main.userStorage.addUser(sender);
        Main.userStorage.addUser(receiver);

        String setNotExistingSenderFilter = Main.setSenderSpamFilter(compositeSpamFilter,
                receiver, "shmulya_praim");
        String setEqualsSenderFilter = Main.setSenderSpamFilter(compositeSpamFilter,
                receiver, receiver.getUserName());
        String setExistingSenderFilter = Main.setSenderSpamFilter(compositeSpamFilter,
                receiver, sender.getUserName());

        assertEquals("Такого пользователя не существует", setNotExistingSenderFilter);
        assertEquals("Вы не можете указать самого себя как спам-отправителя",
                setEqualsSenderFilter);
        assertEquals("Спам фильтр записан и будет добавлен при вводе команды \"done\"",
                setExistingSenderFilter);
    }

    @Test
    void processCommandTest(){
        List<String> emptyInput = Main.processCommand("");
        List<String> notExistingCommand = Main.processCommand("q4w2e");
        assertEquals("Введена пустая строка" , emptyInput.getFirst());
        assertEquals("Неизвестная команда: q4w2e", notExistingCommand.getFirst());
    }
}