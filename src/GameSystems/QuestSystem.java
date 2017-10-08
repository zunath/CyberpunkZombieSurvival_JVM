package GameSystems;

import Data.Repository.FameRepository;
import Data.Repository.QuestRepository;
import Dialog.DialogPage;
import Entities.PCQuestStatusEntity;
import Entities.PCRegionalFameEntity;
import Entities.QuestEntity;
import Entities.QuestPrerequisiteEntity;
import GameObject.PlayerGO;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.List;

public class QuestSystem {

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
        if(!DoesPlayerMeetPrerequisites(quest.getPrerequisiteQuests())) return;

        PCRegionalFameEntity fame = fameRepo.GetPCFameByID(uuid, quest.getFameRegion().getFameRegionID());

        if(fame.getAmount() < quest.getRequiredFameAmount())
        {
            NWScript.sendMessageToPC(oPC, "You do not have enough fame to accept that quest.");
            return;
        }

        status = new PCQuestStatusEntity();
        status.setCurrentStateID(1);
        status.setQuest(quest);

        questRepo.Save(status);
        NWScript.sendMessageToPC(oPC, "Quest '" + quest.getName() + "' accepted. Refer to your journal for more information on this quest.");
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


        return false;
    }

    public static void SpawnQuestItems(NWObject oChest, NWObject oPC)
    {

    }
}
