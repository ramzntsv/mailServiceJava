package MainPackage;

import SpamFiltersPackage.*;
import UsersMessagePackage.*;

import java.util.*;
import java.math.BigInteger;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static UserStorage userStorage = new UserStorage();

    public static List<String> processCommand(String command) {
        switch (command) {
            case "add" -> {
                return List.of(addUser(getInput("Введите имя пользователя: ")));
            }
            case "list" -> {
                return (usersList());
            }
            case "send" -> {
                return List.of(sendMessage());
            }
            case "inbox" -> {
                return (getInboxMessages(getInput("Введите имя пользователя: ")));
            }
            case "outbox" -> {
                return (getOutboxMessages(getInput("Введите имя пользователя: ")));
            }
            case "spam" -> {
                return (getSpamMessages(getInput("Введите имя пользователя: ")));
            }
            case "setfilter" -> {
                CompositeSpamFilter compositeSpamFilter = new CompositeSpamFilter();
                return List.of(setSpamFilter(getInput("Введите имя пользователя: "),
                        compositeSpamFilter));
            }
            case "" -> {
                return List.of("Введена пустая строка");
            }
            default -> {
                return List.of("Неизвестная команда: " + command);
            }
        }
    }

    public static String getInput(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static void printList(List<String> listToPrint){
        for (String string : listToPrint) {
            System.out.println(string);
        }
    }

    public static String addUser(String userName){
        if (userName.isEmpty()){
            return "Имя пользователя не может быть пустым";
        }

        if (userStorage.hasUser(userName)){
            return "Пользователь " + userName + " уже существует";
        }

        User userToAdd = new User(userName);
        userStorage.addUser(userToAdd);
        return "Пользователь " + userName + " добавлен";
    }

    public static List<String> usersList(){
        if (userStorage.isEmpty()){
            return List.of("В системе нет пользователей");
        }

        return userStorage.getAllUsersNames();
    }


    private static String sendMessage(){
        String senderName = getInput("Введите имя отправителя: ");

        if (!userStorage.hasUser(senderName)) {
            return "Такого пользователя не существует. " +
                    "\nЕсли хотите продолжить, введите команду заново";
        }

        User sender = userStorage.getUserByName(senderName);

        String receiverName = getInput("Введите имя отправителя: ");

        if (!userStorage.hasUser(receiverName)) {
            return "Такого пользователя не существует. " +
                    "\nЕсли хотите продолжить, введите команду заново";
        }
        User receiver = userStorage.getUserByName(receiverName);

        if (receiver.getUserName().equals(sender.getUserName())){
            return "Имена отправителя и получателя не могут совпадать" +
                    "\nЕсли хотите продолжить, введите команду заново";
        }

        String caption = getInput("Введите заголовок в одной строке: ");
        if (caption.isEmpty()){
            return "Пустой заголовок, введите команду заново";
        }

        String text = getInput("Введите текст сообщения в одной строке: ");
        if (text.isEmpty()){
            return "Пустой текст сообщения, введите команду заново";
        }

        sender.sendMessage(receiver, caption, text);
        return "Сообщение отправлено";
    }



    public static List<String> getInboxMessages(String userName){
        List<String> listToPrint = new ArrayList<>();

        if (!userStorage.hasUser(userName)) {
            return List.of("Такого пользователя не существует. " +
                    "\nЕсли хотите продолжить, введите команду заново");
        }

        User user = userStorage.getUserByName(userName);

        List<Message> inboxList = user.getInbox();
        if (inboxList.isEmpty()){
            return List.of("Сообщений этому пользователю пока нет");
        }
        for (Message message : inboxList){
            String stringToPrint = String.join("\n",
                    "--------------------",
                    "Сообщение от пользователя " + message.getSender().getUserName(),
                    "Заголовок: " + message.getCaption(),
                    "Текст: " + message.getText(),
                    "--------------------");
            listToPrint.add(stringToPrint);

        }
        return listToPrint;
    }

    public static List<String> getOutboxMessages(String userName){
        List<String> listToPrint = new ArrayList<>();

        if (!userStorage.hasUser(userName)) {
            return List.of("Такого пользователя не существует. " +
                    "\nЕсли хотите продолжить, введите команду заново");
        }

        User user = userStorage.getUserByName(userName);

        List<Message> outboxList = user.getOutbox();
        if (outboxList.isEmpty()){
            return List.of("Этот пользователь пока не писал никому сообщения");
        }
        for (Message message : outboxList){
            String stringToPrint = String.join("\n",
                    "--------------------",
                    "Сообщение для пользователя " + message.getReceiver().getUserName(),
                    "Заголовок: " + message.getCaption(),
                    "Текст: " + message.getText(),
                    "--------------------");
            listToPrint.add(stringToPrint);
        }
        return listToPrint;
    }

    public static List<String> getSpamMessages(String userName){
        List<String> listToPrint = new ArrayList<>();

        if (!userStorage.hasUser(userName)) {
            return List.of("Такого пользователя не существует. " +
                    "\nЕсли хотите продолжить, введите команду заново");
        }

        User user = userStorage.getUserByName(userName);

        List<Message> spamList = user.getSpam();
        if (spamList.isEmpty()){
            return List.of("Спам сообщений этому пользователю пока нет");
        }
        for (Message message : spamList){
            String stringToPrint = String.join("\n",
                    "--------------------",
                    "Спам сообщение от пользователя " + message.getSender().getUserName(),
                    "Заголовок: " + message.getCaption(),
                    "Текст: " + message.getText(),
                    "--------------------");
            listToPrint.add(stringToPrint);
        }
        return listToPrint;
    }

    public static String setSpamFilter(String userName, CompositeSpamFilter compositeSpamFilter){
        if (!userStorage.hasUser(userName)) {
            return "Такого пользователя не существует. " +
                    "\nЕсли хотите продолжить, введите команду заново";
        }

        User user = userStorage.getUserByName(userName);

        System.out.println("Возможные фильтры : \"simple\", \"keywords\", \"repetition\", \"sender\"");
        System.out.println("Для завершения ввода фильтров напишите \"done\" (без кавычек)");
        while (true){
            String filter = getInput("Введите фильтр: ");
            String stringToPrint = processSetSpamFilter(compositeSpamFilter, user, filter);
            System.out.println(stringToPrint);
            if (stringToPrint.equals("Вы не добавили ни одного фильтра") ||
            stringToPrint.equals("Фильтры заданы")){
                return "";
            }

            }
        }

    public static String processSetSpamFilter(CompositeSpamFilter compositeSpamFilter,
                                               User userToSetFilter, String spamFilter) {
        switch (spamFilter) {
            case "simple" -> {
                return (setSimpleSpamFilter(compositeSpamFilter));

            }

            case "keywords" -> {
                String spamKeywords = getInput("Введите все спам-слова через пробел: ");
                return ((setKeywordsSpamFilter(compositeSpamFilter, spamKeywords)));
            }

            case "repetition" -> {
                String maxRepetitionString = getInput("Введите максимальное количество повторений слова: ");
                return (setRepetitionSpamFilter(compositeSpamFilter, maxRepetitionString));
            }

            case "sender" -> {
                String spamSenderName = getInput("Введите имя пользователя: ");
                return (setSenderSpamFilter(compositeSpamFilter, userToSetFilter, spamSenderName));
            }

            case "done" -> {
                if (compositeSpamFilter.isEmpty()) {
                    return "Вы не добавили ни одного фильтра";
                } else {
                    userToSetFilter.setSpamFilter(compositeSpamFilter);
                    return "Фильтры заданы";
                }
            }

            default -> {
                return "Некорректный фильтр, введите заново";
            }
        }
    }

    private static String setSimpleSpamFilter(CompositeSpamFilter compositeSpamFilter){
        SimpleSpamFilter simpleSpamFilter = new SimpleSpamFilter();
        compositeSpamFilter.addFilter(simpleSpamFilter);
        return "Спам фильтр записан и будет добавлен при вводе команды \"done\"";
    }

    public static String setKeywordsSpamFilter(CompositeSpamFilter compositeSpamFilter,
                                              String spamKeywords){
        List<String> spamKeywordsList = List.of(spamKeywords.split(" "));
        for (String keyword : spamKeywordsList) {
            if (!isWord(keyword)) {
                return "Некорректный ввод";
            }
        }
        KeywordsSpamFilter keywordsSpamFilter = new KeywordsSpamFilter(spamKeywordsList);
        compositeSpamFilter.addFilter(keywordsSpamFilter);
        return "Спам фильтр записан и будет добавлен при вводе команды \"done\"";
    }

    public static String setRepetitionSpamFilter(CompositeSpamFilter compositeSpamFilter,
                                                  String maxRepetitionString){
        if (isNumeric(maxRepetitionString)){
            BigInteger maxRepetition = new BigInteger(maxRepetitionString);
            RepetitionSpamFilter repetitionSpamFilter = new RepetitionSpamFilter(maxRepetition);
            compositeSpamFilter.addFilter(repetitionSpamFilter);
            return "Спам фильтр записан и будет добавлен при вводе команды \"done\"";
        } else{
            return "Необходимо ввести число, введите фильтр еще раз";
        }
    }


    public static String setSenderSpamFilter(CompositeSpamFilter compositeSpamFilter,
                                              User userToSetFilter, String spamSenderName){
        if (userStorage.hasUser(spamSenderName)){
            User spamSender = userStorage.getUserByName(spamSenderName);
            if (spamSenderName.equals(userToSetFilter.getUserName())){
                return "Вы не можете указать самого себя как спам-отправителя";
            } else {
                SenderSpamFilter senderSpamFilter = new SenderSpamFilter(spamSender);
                compositeSpamFilter.addFilter(senderSpamFilter);
            }
        } else{
           return "Такого пользователя не существует";
        }
        return "Спам фильтр записан и будет добавлен при вводе команды \"done\"";
    }

    public static boolean isNumeric(String str) {
        return str.matches("[0-9]+");
    }
    public static boolean isWord(String str) {
        return str.matches("[a-zA-Zа-яА-Я0-9]+");
    }

    public static void main(String[] args) {
        System.out.println("Для выхода из программы напишите \"quit\" (без кавычек)");


        while (true) {
            String command = getInput("Введите команду: ");
            if (command.equals("quit")){
                break;
            }
            printList((processCommand(command)));
            
        }
    }
}