package GameSystems;

import Data.Repository.FameRepository;
import Data.Repository.KeyItemRepository;
import Data.Repository.QuestRepository;
import Dialog.DialogPage;
import Entities.*;
import GameObject.PlayerGO;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.ArrayList;
import java.util.List;

public class QuestSystem {

    public static void OnClientEnter()
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

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
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

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
        if(!DoesPlayerMeetPrerequisites(oPC, quest.getPrerequisiteQuests()))
        {
            NWScript.sendMessageToPC(oPC, "You do not meet the prerequisites necessary to accept this quest.");
            return;
        }

        if(!DoesPlayerHaveRequiredKeyItems(oPC, quest.getRequiredKeyItems()))
        {
            NWScript.sendMessageToPC(oPC, "You do not have the required key items to accept this quest.");
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
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

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
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();
        QuestRepository questRepo = new QuestRepository();
        QuestEntity quest = questRepo.GetQuestByID(questID);
        PCQuestStatusEntity pcState = questRepo.GetPCQuestStatusByID(uuid, questID);

        if(quest.allowRewardSelection())
        {
            // TODO: Launch conversation for selecting reward.

            return;
        }

        QuestStateEntity finalState = null;
        for(QuestStateEntity questState : quest.getQuestStates())
        {
            if(questState.isFinalState())
            {
                finalState = questState;
                break;
            }
        }

        if(finalState == null)
        {
            NWScript.sendMessageToPC(oPC, "Could not find final state of quest. Please notify an admin this quest is bugged. (QuestID: " + questID + ")");
            return;
        }

        pcState.setCurrentQuestState(finalState);
        pcState.setCompletionDate(DateTime.now(DateTimeZone.UTC).toDate());

        for(QuestRewardItemEntity reward : quest.getRewardItems())
        {
            NWScript.createItemOnObject(reward.getResref(), oPC, reward.getQuantity(), "");
        }

        if(quest.getRewardGold() > 0)
        {
            NWScript.giveGoldToCreature(oPC, quest.getRewardGold());
        }

        if(quest.getRewardXP() > 0)
        {
            ProgressionSystem.GiveExperienceToPC(oPC, quest.getRewardXP());
        }

        if(quest.getRewardKeyItem() != null)
        {
            KeyItemSystem.GivePlayerKeyItem(oPC, quest.getRewardKeyItem().getKeyItemID());
        }

        if(quest.removeStartKeyItemAfterCompletion())
        {
            KeyItemSystem.RemovePlayerKeyItem(oPC, quest.getStartKeyItemID().getKeyItemID());
        }

        if(quest.getRewardFame() > 0)
        {
            FameRepository fameRepo = new FameRepository();
            PCRegionalFameEntity fame = fameRepo.GetPCFameByID(uuid, quest.getFameRegion().getFameRegionID());
            fame.setAmount(fame.getAmount() + quest.getRewardFame());
            fameRepo.Save(fame);
        }

        NWScript.sendMessageToPC(oPC, "Quest '" + quest.getName() + "' complete!");
        questRepo.Save(pcState);
    }

    public static DialogPage BuildRewardItemPage(int questID)
    {

        return null;
    }

    public static boolean DoesPlayerHaveRequiredItems(int questID)
    {

        return false;
    }

    private static boolean DoesPlayerMeetPrerequisites(NWObject oPC, List<QuestPrerequisiteEntity> prereqs)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return false;
        if(prereqs.isEmpty()) return true;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();
        List<Integer> completedQuestIDs = questRepo.GetAllCompletedQuestIDs(pcGO.getUUID());

        ArrayList<Integer> prereqIDs = new ArrayList<>();
        for(QuestPrerequisiteEntity prereq : prereqs)
        {
            prereqIDs.add(prereq.getRequiredQuest().getQuestID());
        }

        return completedQuestIDs.containsAll(prereqIDs);
    }

    private static boolean DoesPlayerHaveRequiredKeyItems(NWObject oPC, List<QuestRequiredKeyItemListEntity> requiredKeyItems)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return false;
        if(requiredKeyItems.isEmpty()) return true;

        PlayerGO pcGO = new PlayerGO(oPC);
        KeyItemRepository keyItemRepo = new KeyItemRepository();
        List<Integer> keyItemIDs = keyItemRepo.GetListOfPCKeyItemIDs(pcGO.getUUID());

        ArrayList<Integer> requiredKeyItemIDs = new ArrayList<>();
        for(QuestRequiredKeyItemListEntity ki : requiredKeyItems)
        {
            requiredKeyItemIDs.add(ki.getKeyItem().getKeyItemID());
        }

        return keyItemIDs.containsAll(requiredKeyItemIDs);
    }

    public static void SpawnQuestItems(NWObject oChest, NWObject oPC)
    {

    }
}
