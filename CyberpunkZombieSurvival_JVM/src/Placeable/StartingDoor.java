package Placeable;

import Dialog.DialogManager;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class StartingDoor implements IScriptEventHandler {
    @Override
    public void runScript(NWObject door) {
        NWObject oPC = NWScript.getLastUsedBy();
        DialogManager.startConversation(oPC, oPC, "StartingDoor");
    }
}
