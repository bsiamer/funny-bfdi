package xyz.imcodist.funnybfdi.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import xyz.imcodist.funnybfdi.FunnyBFDI;
import xyz.imcodist.funnybfdi.other.MouthManager;

public class FunnyBFDIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FunnyBFDI.LOGGER.info("You swear you heard \"New Friendly\" begin to play in your head.");

        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (sender == null) return;

            String messageText = "";
            if (signedMessage != null && signedMessage.signedContent() != null) {
                messageText = signedMessage.signedContent();
            } else if (message != null) {
                messageText = message.getString();
            }

            MouthManager.onPlayerChatted(messageText + " ", sender.id());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> MouthManager.tick());
    }
}
