package Item;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import NWNX.NWNX_Events_Old;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class GenericStartConversation implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = NWNX_Events_Old.GetEventItem();
        String conversation = NWScript.getLocalString(oItem, "CONVERSATION");

        DialogManager.startConversation(oPC, oPC, conversation);
    }
}
