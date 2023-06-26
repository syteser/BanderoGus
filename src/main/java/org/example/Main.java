package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main extends TelegramLongPollingBot {
    int i= 6;
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

        SendMessage msg = createMessage("*Hello* "+ MY_NAME+ "!!!");
        msg.setChatId(chatID);
        sendApiMethodAsync(msg);
    }

    public Long getChatID(Update update){
        if(update.hasMessage()){
            return update.getMessage().getFrom().getId();
        }
        if(update.hasCallbackQuery()){
            return update.getCallbackQuery().getFrom().getId();
        }
        return null;
    }

    public SendMessage createMessage(String text){
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setParseMode("markdown");
        return message;
    }
}