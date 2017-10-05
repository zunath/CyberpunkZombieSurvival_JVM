package GameSystems;

import Dialog.DialogPage;
import org.nwnx.nwnx2.jvm.NWObject;

public class QuestSystem {

    public static void AcceptQuest(int questID)
    {

    }

    protected void CompleteQuest(int questID)
    {

    }

    protected DialogPage BuildRewardItemPage(int questID)
    {

        return null;
    }

    protected boolean DoesPlayerHaveRequiredItems(int questID)
    {

        return false;
    }

    protected boolean HasPlayerStartedQuest(int questID)
    {

        return false;
    }

    protected boolean HasPlayerFinishedQuest(int questID)
    {

        return false;
    }

    protected boolean DoesPlayerMeetPrerequisites(int questID)
    {

        return false;
    }

    protected boolean IsQuestRepeatable(int questID)
    {

        return false;
    }

    public static void SpawnQuestItems(NWObject oChest, NWObject oPC)
    {

    }
}
