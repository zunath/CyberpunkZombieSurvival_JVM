package GameSystems;

import Data.Repository.ActivityLoggingRepository;
import Entities.ChatChannelEntity;
import Entities.ChatLogEntity;
import GameObject.PlayerGO;
import NWNX.ChatMessage;
import NWNX.NWNX_Chat;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class ActivityLoggingSystem {

    public static void OnModuleNWNXChat(NWObject sender)
    {
        if(!NWScript.getIsPC(sender)) return;

        ActivityLoggingRepository repo = new ActivityLoggingRepository();
        ChatLogEntity entity = new ChatLogEntity();
        ChatMessage chatMessage = NWNX_Chat.GetMessage();
        String text = chatMessage.getText();
        int channel = ConvertNWNXChatChannelIDToDatabaseID(chatMessage.getMode());
        ChatChannelEntity channelEntity = repo.GetChatChannelByID(channel);

        // Sender - should always have this data.
        PlayerGO senderGO = new PlayerGO(sender);
        String senderCDKey = NWScript.getPCPublicCDKey(sender, false);
        String senderAccountName = NWScript.getPCPlayerName(sender);
        String senderPlayerID = senderGO.getUUID();

        // Receiver - may or may not have the data.

        String receiverCDKey = null;
        String receiverAccountName = null;
        String receiverPlayerID = null;

        if(NWScript.getIsObjectValid(chatMessage.getRecipient()))
        {
            PlayerGO receiverGO = new PlayerGO(chatMessage.getRecipient());
            receiverCDKey = NWScript.getPCPublicCDKey(chatMessage.getRecipient(), false);
            receiverAccountName = NWScript.getPCPlayerName(chatMessage.getRecipient());
            receiverPlayerID = receiverGO.getUUID();
        }

        entity.setMessage(text);
        entity.setSenderCDKey(senderCDKey);
        entity.setSenderAccountName(senderAccountName);
        entity.setSenderPlayerID(senderPlayerID);

        entity.setReceiverCDKey(receiverCDKey);
        entity.setReceiverAccountName(receiverAccountName);
        entity.setReceiverPlayerID(receiverPlayerID);

        entity.setChatChannel(channelEntity);
        repo.Save(entity);

    }

    private static int ConvertNWNXChatChannelIDToDatabaseID(int nwnxChatChannelID)
    {
        switch (nwnxChatChannelID)
        {
            case 1: // Talk
                return 3;
            case 2: // Shout
                return 1;
            case 3: // Whisper
                return 2;
            case 4: // Private (Tell)
                return 6;
            case 5: // Server
                return 7;
            case 6: // Party
                return 4;
            default: // DM
                return 5;
        }
    }

}
