package Dialog;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class DialogStart implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        String className = NWScript.getLocalString(oPC, "REO_CONVERSATION");
        NWObject oTalkToTarget = NWScript.getLocalObject(oPC, "REO_CONVERSATION_TARGET");
        NWScript.deleteLocalString(oPC, "REO_CONVERSATION");
        NWScript.deleteLocalObject(oPC, "REO_CONVERSATION_TARGET");

        DialogManager.startConversation(oPC, oTalkToTarget, className);
    }
}
