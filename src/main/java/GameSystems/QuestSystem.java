package GameSystems;

import Data.Repository.FameRepository;
import Data.Repository.KeyItemRepository;
import Data.Repository.QuestRepository;
import Entities.*;
import Enumerations.QuestType;
import GameObject.PlayerGO;
import Helper.ColorToken;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static void OnItemAcquired()
    {
        NWObject oPC = NWScript.getModuleItemAcquiredBy();
        NWObject oItem = NWScript.getModuleItemAcquired();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        int questID = NWScript.getLocalInt(oItem, "QUEST_ID");

        if(questID <= 0) return;

        NWScript.setItemCursedFlag(oItem, true);
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
        QuestStateEntity questState = status.getCurrentQuestState();

        if(!DoesPlayerMeetPrerequisites(oPC, quest.getPrerequisiteQuests()))
        {
            NWScript.sendMessageToPC(oPC, "You do not meet the prerequisites necessary to accept this quest.");
            return;
        }

        if(!DoesPlayerHaveRequiredKeyItems(oPC, questState.getRequiredKeyItems()))
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

        CreateExtendedQuestDataEntries(status, questID, 1);

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

                    CreateExtendedQuestDataEntries(questStatus, questID, nextState.getSequence());
                    return;
                }
            }
        }

        // Shouldn't reach this point unless configuration for the quest is broken.
        NWScript.sendMessageToPC(oPC, "There was an error advancing you to the next objective for quest '" + quest.getName() + "'. Please inform an admin this quest is bugged. (QuestID = " + questID + ")");
    }

    private static void CreateExtendedQuestDataEntries(PCQuestStatusEntity status, int questID, int sequenceID)
    {
        QuestRepository questRepo = new QuestRepository();

        // Create entries for the PC kill targets.
        List<QuestKillTargetListEntity> killTargets = questRepo.GetQuestKillTargetsByQuestSequenceID(questID, sequenceID);
        for(QuestKillTargetListEntity kt : killTargets)
        {
            PCQuestKillTargetProgressEntity pcKT = new PCQuestKillTargetProgressEntity();
            pcKT.setRemainingToKill(kt.getQuantity());
            pcKT.setNpcGroup(kt.getNpcGroup());
            pcKT.setPcQuestStatus(status);
            pcKT.setPlayerID(status.getPlayerID());

            questRepo.Save(pcKT);
        }
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
        int questID = NWScript.getLocalInt(oChest, "QUEST_ID");
        int questStateSequence = NWScript.getLocalInt(oChest, "SEQUENCE_ID");
        String questItemResref = NWScript.getLocalString(oChest, "QUEST_ITEM_RESREF");

        if(questID <= 0 || questStateSequence <= 0 || questItemResref.equals("")) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();

        PCQuestStatusEntity status = questRepo.GetPCQuestStatusByID(pcGO.getUUID(), questID);
        QuestStateEntity questState = status.getCurrentQuestState();

        if(questStateSequence != questState.getSequence()) return;
        if(!NWScript.getIsObjectValid(NWScript.getItemPossessedBy(oPC, questItemResref))) return;

        // PC is on the correct quest, correct state, the chest creates quest items, and the PC does not already have the quest item.
        // Spawn it.

        NWScript.createItemOnObject(questItemResref, oChest, 1, "");

    }

    public static void OnCreatureDeath(NWObject creature)
    {
        int npcGroupID = NWScript.getLocalInt(creature, "NPC_GROUP");
        if (npcGroupID <= 0) return;

        NWObject oKiller = NWScript.getLastKiller();

        if(!NWScript.getIsPC(oKiller) || NWScript.getIsDM(oKiller)) return;

        String areaResref = NWScript.getResRef(NWScript.getArea(creature));

        for(NWObject oPC : NWScript.getFactionMembers(oKiller, true))
        {
            if(!areaResref.equals(NWScript.getResRef(NWScript.getArea(oPC)))) continue;
            if(NWScript.getDistanceBetween(creature, oPC) == 0.0f || NWScript.getDistanceBetween(creature, oPC) > 20.0f) continue;

            PlayerGO pcGO = new PlayerGO(oPC);
            QuestRepository questRepo = new QuestRepository();
            List<PCQuestKillTargetProgressEntity> killTargets = questRepo.GetPlayerKillTargetsByID(pcGO.getUUID(), npcGroupID);

            for(PCQuestKillTargetProgressEntity kt : killTargets)
            {
                kt.setRemainingToKill(kt.getRemainingToKill() - 1);
                String targetGroupName = kt.getNpcGroup().getName();
                String questName = kt.getPcQuestStatus().getQuest().getName();
                String updateMessage = "[" + questName + "] " + targetGroupName + " remaining: " + kt.getRemainingToKill();

                if(kt.getRemainingToKill() <= 0)
                {
                    questRepo.Delete(kt);
                    updateMessage += " " + ColorToken.Green() + " {COMPLETE}";

                    if(kt.getPcQuestStatus().getPcKillTargets().size()-1 <= 0)
                    {
                        AdvanceQuestState(oPC, kt.getPcQuestStatus().getQuest().getQuestID());
                    }
                }
                else
                {
                    questRepo.Save(kt);
                }

                final String finalMessage = updateMessage;
                Scheduler.delay(oPC, 1000, () -> NWScript.sendMessageToPC(oPC, finalMessage));
            }
        }
    }

    private static void HandleTriggerAndPlaceableQuestLogic(NWObject oPC, NWObject oObject)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;
        final String questMessage = NWScript.getLocalString(oObject, "QUEST_MESSAGE");
        int questID = NWScript.getLocalInt(oObject, "QUEST_ID");
        int questSequence = NWScript.getLocalInt(oObject, "QUEST_SEQUENCE");

        if(questID <= 0)
        {
            NWScript.sendMessageToPC(oPC, "QUEST_ID variable not set on object. Please inform admin this quest is bugged.");
            return;
        }

        if(questSequence <= 0)
        {
            NWScript.sendMessageToPC(oPC, "QUEST_SEQUENCE variable not set on object. Please inform admin this quest is bugged. (QuestID: " + questID + ")");
            return;
        }

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();

        PCQuestStatusEntity pcQuestStatus = questRepo.GetPCQuestStatusByID(pcGO.getUUID(), questID);
        if(pcQuestStatus == null) return;

        QuestStateEntity questState = pcQuestStatus.getCurrentQuestState();

        if(questState.getSequence() == questSequence ||
          (questState.getQuestType().getQuestTypeID() != QuestType.UseObject &&
                  questState.getQuestType().getQuestTypeID() != QuestType.ExploreArea))
        {
            return;
        }


        if(!questMessage.equals(""))
        {
            Scheduler.delay(oPC, 1000, () -> NWScript.sendMessageToPC(oPC, questMessage));
        }

        AdvanceQuestState(oPC, questID);
    }

    public static void OnQuestPlaceableUsed(NWObject oObject)
    {
        NWObject oPC = NWScript.getLastUsedBy();
        HandleTriggerAndPlaceableQuestLogic(oPC, oObject);
    }

    public static void OnQuestTriggerEntered(NWObject oObject)
    {
        NWObject oPC = NWScript.getEnteringObject();
        HandleTriggerAndPlaceableQuestLogic(oPC, oObject);
    }

    public static void TurnInQuestItems(NWObject oPC, NWObject oQuestGiver, int questID, int sequenceID)
    {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        QuestRepository questRepo = new QuestRepository();
        PCQuestStatusEntity pcStatus = questRepo.GetPCQuestStatusByID(pcGO.getUUID(), questID);

        if(pcStatus == null)
        {
            NWScript.sendMessageToPC(oPC, "You have not accepted this quest yet.");
            return;
        }

        QuestStateEntity questState = pcStatus.getCurrentQuestState();

        if(questState.getSequence() != sequenceID)
        {
            NWScript.sendMessageToPC(oPC, "SequenceID mismatch. Please inform an admin there is a bug with this quest. (QuestID = " + questID + ")");
            return;
        }


        for(QuestRequiredKeyItemListEntity ki : questState.getRequiredKeyItems())
        {
            if(!KeyItemSystem.PlayerHasKeyItem(oPC, ki.getKeyItem().getKeyItemID()))
            {
                NWScript.sendMessageToPC(oPC, "You are missing a required key item.");
                return;
            }
        }

        List<QuestRequiredItemListEntity> requiredItems = questState.getRequiredItems();
        HashMap<String, Integer> itemsFound = new HashMap<>();

        for(QuestRequiredItemListEntity ri : requiredItems)
        {
            if(itemsFound.containsKey(ri.getResref()))
            {
                int count = itemsFound.get(ri.getResref()) + ri.getQuantity();
                itemsFound.replace(ri.getResref(), count);
            }
            else
            {
                itemsFound.put(ri.getResref(), ri.getQuantity());
            }
        }

        for(NWObject item : NWScript.getItemsInInventory(oPC))
        {
            String resref = NWScript.getResRef(item);
            if(itemsFound.containsKey(resref))
            {
                if(itemsFound.get(resref) > 0)
                {
                    if(NWScript.getItemStackSize(item) == 1)
                    {
                        itemsFound.replace(resref, itemsFound.get(resref) - 1);
                        NWScript.copyItem(item, oQuestGiver, true);
                        NWScript.destroyObject(item, 0.0f);
                    }
                    else
                    {
                        // TODO: Handle stack sizes.
                    }
                }
            }
        }

        for(String foundResref : itemsFound.keySet())
        {
            if(itemsFound.get(foundResref) > 0)
            {
                for(NWObject item : NWScript.getItemsInInventory(oQuestGiver))
                {
                    NWScript.copyItem(item, oPC, true);
                    NWScript.destroyObject(item, 0.0f);
                }

                NWScript.sendMessageToPC(oPC, "You are missing a required item.");
                return;
            }
        }

        for(NWObject item : NWScript.getItemsInInventory(oQuestGiver))
        {
            NWScript.destroyObject(item, 0.0f);
        }

        AdvanceQuestState(oPC, questID);
    }
}
