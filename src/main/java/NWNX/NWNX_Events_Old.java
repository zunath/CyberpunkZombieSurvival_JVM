package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class NWNX_Events_Old {
    public static int GetEventSubType()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_EVENT_SUBID", "      ");
        return NWScript.stringToInt(NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_EVENT_SUBID"));
    }

    public static NWObject GetEventTarget()
    {
        return NWScript.getLocalObject(NWObject.MODULE, "NWNX!EVENTS!TARGET");
    }

    public static void BypassEvent()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!BYPASS", "1");
    }
}
