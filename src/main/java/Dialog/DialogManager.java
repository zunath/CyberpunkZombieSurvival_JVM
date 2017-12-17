package Dialog;

import GameObject.PlayerGO;
import Helper.ErrorHelper;
import NWNX.NWNX_Creature;
import NWNX.NWNX_Funcs_Old;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

import java.util.HashMap;
import java.util.Objects;

public class DialogManager {

    public static final int NumberOfResponsesPerPage = 12;
    private static HashMap<String, PlayerDialog> playerDialogs;

    public static void storePlayerDialog(String uuid, PlayerDialog dialog)
    {
        if(playerDialogs == null)
        {
            playerDialogs = new HashMap<>();
        }


        playerDialogs.put(uuid, dialog);
    }

    public static PlayerDialog loadPlayerDialog(String uuid)
    {
        if(playerDialogs == null)
        {
            playerDialogs = new HashMap<>();
        }

        if(playerDialogs.containsKey(uuid))
        {
            return playerDialogs.get(uuid);
        }
        else
        {
            return null;
        }
    }

    public static void removePlayerDialog(String uuid)
    {
        if(playerDialogs == null)
        {
            playerDialogs = new HashMap<>();
        }

        playerDialogs.remove(uuid);
    }

    public static void loadConversation(NWObject oPC, NWObject oTalkTo, String conversationName)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        Class scriptClass = Class.forName("Conversation." + conversationName);
        IDialogHandler script = (IDialogHandler)scriptClass.newInstance();
        PlayerDialog dialog = script.SetUp(oPC);
        dialog.setActiveDialogName(conversationName);
        dialog.setDialogTarget(oTalkTo);
        DialogManager.storePlayerDialog(pcGO.getUUID(), dialog);

    }

    public static void startConversation(NWObject oPC, final NWObject oTalkTo, final String conversationName)
    {
        try {
            loadConversation(oPC, oTalkTo, conversationName);

            String convo = NWNX_Funcs_Old.GetConversation(oTalkTo);

            if(Objects.equals(convo, "") || Objects.equals(convo, "0"))
            {
                Scheduler.assign(oPC, () -> NWScript.actionStartConversation(oTalkTo, "dialog", true, false));
            }
        }
        catch(Exception ex) {
            ErrorHelper.HandleException(ex, "DialogStart was unable to execute class method: Conversation." + conversationName + ".Initialize()");
        }
    }

    public static void endConversation(NWObject oPC)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerDialog playerDialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        playerDialog.setIsEnding(true);
        DialogManager.storePlayerDialog(pcGO.getUUID(), playerDialog);
    }

}
