package NWNX;

import Helper.ScriptHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;

import java.util.Objects;

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

    public static void StartGuiTimingBar(NWObject player, float seconds, String javaScript)
    {
        String sFunc = "StartGuiTimingBar";
        NWNX_PushArgumentFloat(NWNX_Player, sFunc, seconds);
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);

        Scheduler.delay(player, (int)(seconds * 1000), () -> StopGuiTimingBar(player, javaScript));
    }

    public static void StopGuiTimingBar(NWObject player, String javaScript)
    {
        String sFunc = "StopGuiTimingBar";
        NWNX_PushArgumentObject(NWNX_Player, sFunc, player);

        NWNX_CallFunction(NWNX_Player, sFunc);

        if(!Objects.equals(javaScript, "")) {
            ScriptHelper.RunJavaScript(player, javaScript);
        }
    }
}
