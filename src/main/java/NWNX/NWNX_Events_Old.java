package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.NWVector;
import org.nwnx.nwnx2.jvm.constants.Gender;

import java.util.Objects;

public class NWNX_Events_Old {
    public static final int EVENT_TYPE_ATTACK             = 3;

    public static final int LANGUAGE_ENGLISH              = 0;

    public static int GetEventSubType()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_EVENT_SUBID", "      ");
        return NWScript.stringToInt(NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_EVENT_SUBID"));
    }

    public static NWObject GetEventTarget()
    {
        return NWScript.getLocalObject(NWObject.MODULE, "NWNX!EVENTS!TARGET");
    }

    public static NWVector GetEventPosition()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_EVENT_POSITION", "                                              ");
        String sVector = NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_EVENT_POSITION");
        float x, y, z;

        //Get X
        int nPos = NWScript.findSubString(sVector, "¬", 0);
        if(nPos == -1) return new NWVector(0.0f, 0.0f, 0.0f);
        x = NWScript.stringToFloat(NWScript.getStringLeft(sVector, nPos));
        sVector = NWScript.getStringRight(sVector, NWScript.getStringLength(sVector) - nPos - 1);

        //Get Y
        nPos = NWScript.findSubString(sVector, "¬", 0);
        if(nPos == -1) return new NWVector(0.0f, 0.0f, 0.0f);
        y = NWScript.stringToFloat(NWScript.getStringLeft(sVector, nPos));
        sVector = NWScript.getStringRight(sVector, NWScript.getStringLength(sVector) - nPos - 1);

        //Get Z
        nPos = NWScript.findSubString(sVector, "¬", 0);
        if(nPos == -1)
        {
            z = NWScript.stringToFloat(sVector);
        }
        else return new NWVector(0.0f, 0.0f, 0.0f);
        return new NWVector(x, y, z);
    }

    public static void BypassEvent()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!BYPASS", "1");
    }

    public static void SetGlobalEventHandler(int nEventID, String sHandler)
    {
        if (Objects.equals(sHandler, ""))
            sHandler = "-";

        String sKey = "NWNX!EVENTS!SET_EVENT_HANDLER_" + NWScript.intToString(nEventID);
        NWScript.setLocalString(NWObject.MODULE, sKey, sHandler);
        NWScript.deleteLocalString(NWObject.MODULE, sKey);
    }

    public static int GetCurrentNodeType()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_NODE_TYPE", "      ");
        return NWScript.stringToInt(NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_NODE_TYPE"));
    }

    public static int GetCurrentNodeID()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_NODE_ID", "      ");
        return NWScript.stringToInt(NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_NODE_ID"));
    }

    public static int GetSelectedNodeID()
    {
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_SELECTED_NODE_ID", "      ");
        return NWScript.stringToInt(NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_SELECTED_NODE_ID"));
    }

    public static String GetSelectedNodeText(int nLangID, int nGender)
    {
        if (nGender != Gender.FEMALE) nGender = Gender.MALE;
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_SELECTED_NODE_TEXT", NWScript.intToString(nLangID * 2 + nGender));
        return NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_SELECTED_NODE_TEXT");
    }

    public static String GetCurrentNodeText(int nLangID, int nGender)
    {
        if (nGender != Gender.FEMALE) nGender = Gender.MALE;
        NWScript.setLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_NODE_TEXT", NWScript.intToString(nLangID * 2 + nGender));
        return NWScript.getLocalString(NWObject.MODULE, "NWNX!EVENTS!GET_NODE_TEXT");
    }
}
