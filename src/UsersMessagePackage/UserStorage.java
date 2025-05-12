package UsersMessagePackage;

import java.util.*;

public class UserStorage {
    private final Map<String, User> userStorage = new HashMap<>();

    public void addUser(User user) {
        userStorage.put(user.getUserName(), user);
    }

    public User getUserByName(String userName) {
        return userStorage.get(userName);
    }

    public boolean hasUser(String userName) {
        return userStorage.containsKey(userName);
    }
    
    public boolean isEmpty(){
        return userStorage.isEmpty();
    }

    public List<String> getAllUsersNames() {
        List<String> allUsersNames = new ArrayList<>();
        for (User user : userStorage.values()) {
            allUsersNames.add("* " + user.getUserName());
        }
        return allUsersNames;
    }

}