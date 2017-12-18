package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;

public class NWNX_Player {

    private static final String NWNX_Player = "NWNX_Player";

    public static void ForcePlaceableExamineWindow(NWObject player, NWObject placeable)
    {
        String sFunc = "ForcePlaceableExamineWindow";
        NWNX_PushArgumentObject(NWNX_Player, sFunc, placeable);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);
    }

    public static void StartGuiTimingBar(NWObject player, int seconds, String script)
    {
        String sFunc = "StartGuiTimingBar";
        NWNX_PushArgumentString(NWNX_Player, sFunc, script);
        NWNX_PushArgumentInt(NWNX_Player, sFunc, seconds);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);
    }

    public static void StopGuiTimingBar(NWObject player)
    {
        String sFunc = "StopGuiTimingBar";
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);
    }
}
