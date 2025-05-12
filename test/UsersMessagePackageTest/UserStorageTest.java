package UsersMessagePackageTest;
import UsersMessagePackage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {

    private static User user1;
    private static User user2;

    private static UserStorage userStorage;

    @BeforeEach
    void setup(){
        user1 = new User("Lenya");
        user2 = new User("Леня");
        userStorage = new UserStorage();

    }

    @Test
    void userStorageMethodsTest() {
        assertTrue(userStorage.isEmpty());

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        assertFalse(userStorage.isEmpty());
        assertTrue(userStorage.hasUser(user1.getUserName()));
        assertTrue(userStorage.hasUser(user2.getUserName()));

        assertEquals(userStorage.getUserByName("Lenya"), user1);
        assertEquals(userStorage.getUserByName("Леня"), user2);

        List<String> allUsersNames = userStorage.getAllUsersNames();

        assertTrue(allUsersNames.contains("* Lenya"));
        assertTrue(allUsersNames.contains("* Леня"));
    }

}