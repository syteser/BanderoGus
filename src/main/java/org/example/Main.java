package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main extends TelegramLongPollingBot {
    private final Map<Long, Integer> levels = new HashMap<>();

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }

    @Override
    public String getBotToken() {
        return "5820632523:AAEF0-n52yixQuFeyJwhAo1q4oxHTwsgGBA";
    }

    @Override
    public String getBotUsername() {
        return "SV-Service_Bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatID = getChatID(update);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            firstScreen(update, chatID);
        }

        if (update.hasCallbackQuery()) {
            setLevels(chatID, 2);
            sendImage("SV_reklama", chatID);
            SendMessage message = createMessage("""
                    *В процесі розробки.....*""");
            message.setChatId(chatID);

            List<String> buttons = Arrays.asList(
                    "До головного меню"
            );

            attachButtons(message, Map.of(
                    buttons.get(0), "level_2_task"));
            sendApiMethodAsync(message);
        }

        if (update.getCallbackQuery().getData().equals("level_1_task") && getLevel(chatID) == 2) {
            setLevels(chatID, 1);
            firstScreen(update, chatID);
        }


//        if (update.hasCallbackQuery()) {
//            if (update.getCallbackQuery().getData().equals("level_1_task") && getLevel(chatID) == 1) {
//                setLevels(chatID, 2);
//                sendImage("SV_reklama", chatID);
//
//                SendMessage message = createMessage("""
//                        *Вітаємо на другому рівні! Твій гусак - біогусак.*
//                        Баланс: 20 монет.\s
//                        Обери завдання, щоб перейти на наступний рівень""");
//                message.setChatId(chatID);
//
//                List<String> buttons = Arrays.asList(
//                        "Зібрати комарів для нової біологічної зброї (+15 монет)",
//                        "Пройти курс молодого бійця (+15 монет)",
//                        "Задонатити на ЗСУ (+15 монет)",
//                        "Збити дрона банкою огірків (+15 монет)",
//                        "Зробити запаси коктейлів Молотова (+15 монет)"
//                );
//
//                buttons = getRandom3(buttons);
//
//                attachButtons(message, Map.of(
//                        buttons.get(0), "level_2_task",
//                        buttons.get(1), "level_2_task",
//                        buttons.get(2), "level_2_task"));
//                sendApiMethodAsync(message);
//            }
//

//
//            if (update.getCallbackQuery().getData().equals("level_3_task") && getLevel(chatID) == 3) {
//                setLevels(chatID, 4);
//                sendImage("level-4", chatID);
//
//                SendMessage message = createMessage("""
//                        *Вітаємо на останньому рівні! Твій гусак - готова біологічна зброя - бандерогусак.*
//                        Баланс: 50 монет.\s
//                        Тепер ти можеш придбати Джавелін і глушити чмонь""");
//                message.setChatId(chatID);
//
//                attachButtons(message, Map.of(
//                        "Купити Джавелін (50 монет)", "level_4_task"));
//                sendApiMethodAsync(message);
//            }
//
//            if (update.getCallbackQuery().getData().equals("level_4_task") && getLevel(chatID) == 4) {
//                setLevels(chatID, 5);
//                sendImage("SV_reklama", chatID);
//
//                SendMessage message = createMessage("*Джавелін твій. Повний вперед!*");
//                message.setChatId(chatID);
//                sendApiMethodAsync(message);
//            }
//        }
    }

    private void firstScreen(Update update, Long chatID) {
        sendImage("SV_reklama", chatID);
        SendMessage message = createMessage("""
                Доброго дня!
                Вас вітає Бот SV-Service

                Я вам можу допомогти:
                ✔️дізнатися про часи роботи
                ✔️записатись на визов
                ✔️узнати актуальні ціни.

                Оберіть що вас цікавить""");
        message.setChatId(chatID);

        List<String> buttons = Arrays.asList(
                "Подивитись адресу магазину та часи роботи",
                "Замовити дзвінок майстра та записатись на діагностику",
                "Подивитись повний перелік робіт які виконує сервіс."
        );

        buttons = getRandom3(buttons);

        attachButtons(message, Map.of(
                buttons.get(0), "level_1_task",
                buttons.get(1), "level_1_task",
                buttons.get(2), "level_1_task"));
        sendApiMethodAsync(message);

    }

    public Long getChatID(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        return null;
    }

    public SendMessage createMessage(String text) {
        SendMessage message = new SendMessage();

        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        return message;
    }

    public void attachButtons(SendMessage message, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonName : buttons.keySet()) {
            String buttonValue = buttons.get(buttonName);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(buttonValue);
            keyboard.add(Arrays.asList(button));
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }

    public void sendImage(String name, Long chatID) {
        SendAnimation animation = new SendAnimation();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(new File("images/" + name + ".gif"));

        animation.setAnimation(inputFile);
        animation.setChatId(chatID);

        executeAsync(animation);
    }

    public int getLevel(Long chatID) {
        return levels.getOrDefault(chatID, 1);
    }

    public void setLevels(Long chatID, int level) {
        levels.put(chatID, level);
    }

    public List<String> getRandom3(List<String> variant) {
        ArrayList<String> copy = new ArrayList<>(variant);
        Collections.shuffle(copy);
        return copy.subList(0, 3);
    }
}