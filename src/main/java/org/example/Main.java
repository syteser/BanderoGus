package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main extends TelegramLongPollingBot {
    public final String MY_NAME = "Sergey";

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
        return "BanderogusSyteserBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatID = getChatID(update);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            SendMessage message = createMessage("Привiт!");
            message.setChatId(chatID);
            attachButtons(message, Map.of(
                    "Слава Україні!", "glory_from_ukraine"
            ));
            sendApiMethodAsync(message);
        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("glory_from_ukraine")) {
                SendMessage message = createMessage("Героям слава!");
                message.setChatId(chatID);
                attachButtons(message, Map.of(
                        "Слава нації!", "glory_from_nacii"
                ));
                sendApiMethodAsync(message);
            }

            if (update.getCallbackQuery().getData().equals("glory_from_nacii")) {
                SendMessage message = createMessage("Смерть ворогам!");
                message.setChatId(chatID);
                sendApiMethodAsync(message);
            }
        }
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
}