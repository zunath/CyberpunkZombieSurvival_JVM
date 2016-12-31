package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

@SuppressWarnings("unused")
public class StartingDoor extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog();
        DialogPage mainPage = new DialogPage(
                ColorToken.Red() + "WARNING: " + ColorToken.End() +
                        "You are about to enter the game world. You cannot come back here once you've entered.\n\n" +
                        "If you're ready to start playing please select a location from the list below.",
                "Ruby Outpost"
        );

        DialogPage cityHallPage = new DialogPage(
                "Ruby Outpost\n\n" + ColorToken.Green() + "Description: " + ColorToken.End() +
                "The home of an old lady named Ruby. It has since been reinforced to keep the undead out.",
                "Select this location (ENTER THE GAME WORLD)",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("RubyOutpostPage", cityHallPage);
        return dialog;
    }

    @Override
    public void Initialize() {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                switch (responseID)
                {
                    case 1: // Ruby Outpost
                        ChangePage("RubyOutpostPage");
                        break;
                }
                break;
            case "RubyOutpostPage":
                switch (responseID)
                {
                    case 1: // Select this location
                        Scheduler.assign(oPC, new Runnable() {
                            @Override
                            public void run() {
                                NWLocation location = NWScript.getLocation(NWScript.getWaypointByTag("RUBY_OUTPOST_STARTING_LOCATION"));
                                NWScript.actionJumpToLocation(location);
                                EndConversation();
                            }
                        });
                        break;
                    case 2: // Back
                        ChangePage("MainPage");
                        break;
                }
                break;
        }
    }

    @Override
    public void EndDialog() {

    }
}
