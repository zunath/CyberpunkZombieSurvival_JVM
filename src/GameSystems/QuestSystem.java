package GameSystems;

import Data.Repository.FameRepository;
import Data.Repository.QuestRepository;
import Dialog.DialogPage;
import Entities.*;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.List;

public class QuestSystem {

    public static void OnClientEnter()
    {
        NWObject oPC = NWScript.getEnteringObject();
        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();
        List<PCQuestStatusEntity> pcQuests = questRepo.GetAllPCQuestStatusesByID(pcGO.getUUID());

        for(PCQuestStatusEntity quest : pcQuests)
        {
            NWScript.addJournalQuestEntry(quest.getQuest().getJournalTag(), quest.getCurrentQuestState().getJournalStateID(), oPC, false, false, false);
        }
    }

    public static void AcceptQuest(NWObject oPC, int questID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();
        QuestRepository questRepo = new QuestRepository();
        FameRepository fameRepo = new FameRepository();

        PCQuestStatusEntity status = questRepo.GetPCQuestStatusByID(uuid, questID);

        if(status != null)
        {
            if(status.getCompletionDate() != null)
            {
                NWScript.sendMessageToPC(oPC, "You have already completed this quest.");
                return;
            }
            else
            {
                NWScript.sendMessageToPC(oPC, "You have already accepted this quest.");
                return;
            }
        }


        QuestEntity quest = questRepo.GetQuestByID(questID);
        if(!DoesPlayerMeetPrerequisites(quest.getPrerequisiteQuests()))
        {
            NWScript.sendMessageToPC(oPC, "You do not meet the prerequisites necessary to accept this quest.");
            return;
        }

        PCRegionalFameEntity fame = fameRepo.GetPCFameByID(uuid, quest.getFameRegion().getFameRegionID());

        if(fame.getAmount() < quest.getRequiredFameAmount())
        {
            NWScript.sendMessageToPC(oPC, "You do not have enough fame to accept this quest.");
            return;
        }

        status = new PCQuestStatusEntity();
        for(QuestStateEntity state : quest.getQuestStates())
        {
            if(state.getSequence() == 1)
            {
                status.setCurrentQuestState(state);
                break;
            }
        }

        if(status.getCurrentQuestState() == null)
        {
            NWScript.sendMessageToPC(oPC, "There was an error accepting the quest '" + quest.getName() + "'. Please inform an admin this quest is bugged. (QuestID: " + questID + ")");
            return;
        }

        status.setQuest(quest);
        status.setPlayerID(pcGO.getUUID());
        status.setQuest(quest);

        questRepo.Save(status);
        NWScript.addJournalQuestEntry(quest.getJournalTag(), 1, oPC, false, false, false);
        NWScript.sendMessageToPC(oPC, "Quest '" + quest.getName() + "' accepted. Refer to your journal for more information on this quest.");
    }

    public static void AdvanceQuestState(NWObject oPC, int questID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository repo = new QuestRepository();
        PCQuestStatusEntity questStatus = repo.GetPCQuestStatusByID(pcGO.getUUID(), questID);

        if(questStatus == null)
        {
            NWScript.sendMessageToPC(oPC, "You have not accepted this quest yet.");
            return;
        }

        QuestEntity quest = questStatus.getQuest();
        List<QuestStateEntity> states = quest.getQuestStates();

        // Find the next quest state.
        for(QuestStateEntity nextState : states)
        {
            if(nextState.getSequence() == questStatus.getCurrentQuestState().getSequence()+1)
            {
                // Either complete the quest or move to the new state.
                if(nextState.isFinalState())
                {
                    CompleteQuest(oPC, questID);
                    return;
                }
                else
                {
                    NWScript.addJournalQuestEntry(quest.getJournalTag(), nextState.getJournalStateID(), oPC, false, false, false);
                    questStatus.setCurrentQuestState(nextState);
                    NWScript.sendMessageToPC(oPC, "Objective for quest '" + quest.getName() + "' complete! Check your journal for information on the next objective.");
                    repo.Save(questStatus);
                    return;
                }
            }
        }

        // Shouldn't reach this point unless configuration for the quest is broken.
        NWScript.sendMessageToPC(oPC, "There was an error advancing you to the next objective for quest '" + quest.getName() + "'. Please inform an admin this quest is bugged. (QuestID = " + questID + ")");
    }

    public static void CompleteQuest(NWObject oPC, int questID)
    {
        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();
        QuestRepository questRepo = new QuestRepository();
        QuestEntity quest = questRepo.GetQuestByID(questID);


    }

    public static DialogPage BuildRewardItemPage(int questID)
    {

        return null;
    }

    public static boolean DoesPlayerHaveRequiredItems(int questID)
    {

        return false;
    }

    private static boolean DoesPlayerMeetPrerequisites(List<QuestPrerequisiteEntity> prereqs)
    {


        return true;
    }

    public static void SpawnQuestItems(NWObject oChest, NWObject oPC)
    {

    }
}
