package NWNX;

import org.nwnx.nwnx2.jvm.NWObject;

import static NWNX.NWNX_Core.*;

public class NWNX_Events {

    // The following events are exposed by this plugin:
    //
    //     NWNX_ON_ADD_ASSOCIATE_BEFORE
    //     NWNX_ON_ADD_ASSOCIATE_AFTER
    //     NWNX_ON_REMOVE_ASSOCIATE_BEFORE
    //     NWNX_ON_REMOVE_ASSOCIATE_AFTER
    //     NWNX_ON_ENTER_STEALTH_BEFORE
    //     NWNX_ON_ENTER_STEALTH_AFTER
    //     NWNX_ON_EXIT_STEALTH_BEFORE
    //     NWNX_ON_EXIT_STEALTH_AFTER
    //     NWNX_ON_EXAMINE_OBJECT_BEFORE
    //     NWNX_ON_EXAMINE_OBJECT_AFTER
    //     NWNX_ON_USE_ITEM_BEFORE
    //     NWNX_ON_USE_ITEM_AFTER
    //     NWNX_ON_DM_GIVE_GOLD (TODO: Upgrade - Currently disabled)
    //     NWNX_ON_DM_SET_EXP (TODO: Upgrade - Currently disabled)
    //     NWNX_ON_CLIENT_DISCONNECT_BEFORE
    //     NWNX_ON_CLIENT_DISCONNECT_AFTER
    //

    // Scripts can subscribe to events.
    // Some events are dispatched via the NWNX plugin (see NWNX_EVENTS_EVENT_* constants).
    // Others can be signalled via script code (see NWNX_Events_SignalEvent).
    public static void SubscribeEvent(String evt, String script)
    {
        NWNX_PushArgumentString("NWNX_Events", "SUBSCRIBE_EVENT", script);
        NWNX_PushArgumentString("NWNX_Events", "SUBSCRIBE_EVENT", evt);
        NWNX_CallFunction("NWNX_Events", "SUBSCRIBE_EVENT");
    }

    // Pushes event data at the provided tag, which subscribers can access with GetEventData.
    // This should be called BEFORE SignalEvent.
    public static void PushEventData(String tag, String data)
    {
        NWNX_PushArgumentString("NWNX_Events", "PUSH_EVENT_DATA", data);
        NWNX_PushArgumentString("NWNX_Events", "PUSH_EVENT_DATA", tag);
        NWNX_CallFunction("NWNX_Events", "PUSH_EVENT_DATA");
    }

    // Signals an event. This will dispatch a notification to all subscribed handlers.
    // Returns TRUE if anyone was subscribed to the event, FALSE otherwise.
    public static int SignalEvent(String evt, NWObject target)
    {
        NWNX_PushArgumentObject("NWNX_Events", "SIGNAL_EVENT", target);
        NWNX_PushArgumentString("NWNX_Events", "SIGNAL_EVENT", evt);
        NWNX_CallFunction("NWNX_Events", "SIGNAL_EVENT");
        return NWNX_GetReturnValueInt("NWNX_Events", "SIGNAL_EVENT");
    }

    // Retrieves the event data for the currently executing script.
    // THIS SHOULD ONLY BE CALLED FROM WITHIN AN EVENT HANDLER.
    public static String GetEventData(String tag)
    {
        NWNX_PushArgumentString("NWNX_Events", "GET_EVENT_DATA", tag);
        NWNX_CallFunction("NWNX_Events", "GET_EVENT_DATA");
        return NWNX_GetReturnValueString("NWNX_Events", "GET_EVENT_DATA");
    }

}
