package Conversation.Quests;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class Tutorial extends DialogBase implements IDialogHandler {

    private final int QUEST_LEVELING_UP_ID = 2;
    private final int QUEST_EQUIPPING_ABILITIES_ID = 3;
    private final int QUEST_KEY_ITEMS_ID = 4;
    private final int QUEST_SEARCHING_ID = 5;
    private final int QUEST_BUILDING_STRUCTURES_ID = 6;

    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "Well met, rookie. If you plan to survive this chaos you best listen to me.",
                "I need training."
        );

        DialogPage trainingPage = new DialogPage(
                "What kind of training do you need, scrub?",
                "[QUEST] Leveling Up",
                "[QUEST] Equipping Abilities",
                "[QUEST] Key Items",
                "[QUEST] Searching",
                "[QUEST] Building Structures"
        );

        DialogPage levelingUpPage = new DialogPage(
                "Ready to upgrade your skills, scrub? Open up your skill allocation menu in your Omni Tool and start making upgrades. You can do this by pressing 'R' or actively using the Omni Tool in your inventory. When you've figured this much out, come back and let me know.",
                "Understood. I will allocate my skill points and return back to you.",
                "Back"
        );
        DialogPage levelingUpInProgressPage = new DialogPage(
                "Have you accessed your skill allocation menu yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage equippingAbilitiesPage = new DialogPage(
                "Ready to equip an ability, scrub? Learn an ability by using an Ability Disc. Then, equip it by using an AMP terminal.",
                "Understood. I will learn an ability by using an Ability Disc and then equip it by using the AMP terminal nearby.",
                "Back"
        );
        DialogPage equippingAbilitiesInProgressPage = new DialogPage(
                "Have you equipped an ability yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage keyItemsPage = new DialogPage(
                "Key items are important things, scrub. Your Omni Tool will hold them for you so you don't accidentally lose them. Access your key items by pressing 'R' or actively using the Omni Tool in your inventory. When you've figured this much out, come back and let me know.",
                "Understood. I will access my key items and return back to you.",
                "Back"
        );
        DialogPage keyItemsInProgressPage = new DialogPage(
                "Have you accessed your key items yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage searchingPage = new DialogPage(
                "Searching is vital to your survival, scrub. For this mission I want you to leave the outpost and look for a place worth searching. When you've completed this task return to me for your reward.",
                "Understood. I will look for items at a search site and return back to you.",
                "Back"
        );
        DialogPage searchingInProgressPage = new DialogPage(
                "Have you searched for items yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage buildingStructuresPage = new DialogPage(
                "You will need to be able to live off the land yourself, scrub. You can't count on us to protect you forever. For this task, I want you to build a construction site by using the Omni Tool in your inventory. When this is done, return to me.",
                "Understood. I will create a construction site by using my Omni Tool and return back to you.",
                "Back"
        );
        DialogPage buildingStructuresInProgressPage = new DialogPage(
                "Have you built a construction site yet, scrub?",
                "Not yet...",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("TrainingPage", trainingPage);
        dialog.addPage("LevelingUpPage", levelingUpPage);
        dialog.addPage("EquippingAbilitiesPage", equippingAbilitiesPage);
        dialog.addPage("KeyItemsPage", keyItemsPage);
        dialog.addPage("SearchingPage", searchingPage);
        dialog.addPage("BuildingStructuresPage", buildingStructuresPage);

        dialog.addPage("LevelingUpInProgressPage", levelingUpInProgressPage);
        dialog.addPage("EquippingAbilitiesInProgressPage", equippingAbilitiesInProgressPage);
        dialog.addPage("KeyItemsInProgressPage", keyItemsInProgressPage);
        dialog.addPage("SearchingInProgressPage", searchingInProgressPage);
        dialog.addPage("BuildingStructuresInProgressPage", buildingStructuresInProgressPage);

        return dialog;
    }

    @Override
    public void Initialize() {
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QUEST_LEVELING_UP_ID)) // Leveling Up
            GetResponseByID("MainPage", 1).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QUEST_EQUIPPING_ABILITIES_ID)) // Equipping Abilities
            GetResponseByID("MainPage", 2).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QUEST_KEY_ITEMS_ID)) // Key Items
            GetResponseByID("MainPage", 3).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QUEST_SEARCHING_ID)) // Searching
            GetResponseByID("MainPage", 4).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QUEST_BUILDING_STRUCTURES_ID)) // Building Structures
            GetResponseByID("MainPage", 5).setActive(false);

        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_LEVELING_UP_ID) == 2)
            GetResponseByID("LevelingUpInProgressPage", 1).setText("I have accessed my skill allocation menu.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_EQUIPPING_ABILITIES_ID) == 2)
            GetResponseByID("EquippingAbilitiesInProgressPage", 1).setText("I have equipped an ability.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_KEY_ITEMS_ID) == 2)
            GetResponseByID("KeyItemsInProgressPage", 1).setText("I have accessed my key items.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_SEARCHING_ID) == 2)
            GetResponseByID("SearchingInProgressPage", 1).setText("I have finished searching a location.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_BUILDING_STRUCTURES_ID) == 2)
            GetResponseByID("BuildingStructuresInProgressPage", 1).setText("I have built a construction site.");

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleMainPageSelection(responseID);
                break;
            case "TrainingPage":
                HandleTrainingPageSelection(responseID);
                break;
            case "LevelingUpPage":
                HandleLevelingUpPageSelection(responseID);
                break;
            case "LevelingUpInProgressPage":
                HandleLevelingUpInProgressPageSelection(responseID);
                break;
            case "EquippingAbilitiesPage":
                HandleEquippingAbilitiesPageSelection(responseID);
                break;
            case "EquippingAbilitiesInProgressPage":
                HandleEquippingAbilitiesInProgressPageSelection(responseID);
                break;
            case "KeyItemsPage":
                HandleKeyItemsPageSelection(responseID);
                break;
            case "KeyItemsInProgressPage":
                HandleKeyItemsPageInProgressSelection(responseID);
                break;
            case "SearchingPage":
                HandleSearchingPageSelection(responseID);
                break;
            case "SearchingInProgressPage":
                HandleSearchingPageInProgressSelection(responseID);
                break;
            case "BuildingStructuresPage":
                HandleBuildingStructuresPageSelection(responseID);
                break;
            case "BuildingStructuresInProgressPage":
                HandleBuildingStructuresInProgressPageSelection(responseID);
                break;
        }
    }

    private void HandleMainPageSelection(int responseID)
    {
        switch(responseID)
        {
            case 1: // I need training
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleTrainingPageSelection(int responseID)
    {
        switch(responseID)
        {
            case 1: // Leveling Up
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_LEVELING_UP_ID) == -1)
                    ChangePage("LevelingUpPage");
                else ChangePage("LevelingUpInProgressPage");
                break;
            case 2: // Equipping Abilities
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_EQUIPPING_ABILITIES_ID) == -1)
                    ChangePage("EquippingAbilitiesPage");
                else ChangePage("EquippingAbilitiesInProgressPage");
                break;
            case 3: // Key Items
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_KEY_ITEMS_ID) == -1)
                    ChangePage("KeyItemsPage");
                else ChangePage("KeyItemsInProgressPage");
                break;
            case 4: // Searching
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_SEARCHING_ID) == -1)
                    ChangePage("SearchingPage");
                else ChangePage("SearchingInProgressPage");
                break;
            case 5: // Building Structures
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_BUILDING_STRUCTURES_ID) == -1)
                    ChangePage("BuildingStructuresPage");
                else ChangePage("BuildingStructuresInProgressPage");
                break;
        }
    }

    private void HandleLevelingUpPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QUEST_LEVELING_UP_ID);
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleLevelingUpInProgressPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_LEVELING_UP_ID) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QUEST_LEVELING_UP_ID);
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleEquippingAbilitiesPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QUEST_EQUIPPING_ABILITIES_ID);
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleEquippingAbilitiesInProgressPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_EQUIPPING_ABILITIES_ID) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QUEST_EQUIPPING_ABILITIES_ID);
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleKeyItemsPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QUEST_KEY_ITEMS_ID);
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleKeyItemsPageInProgressSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_KEY_ITEMS_ID) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QUEST_KEY_ITEMS_ID);
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleSearchingPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QUEST_SEARCHING_ID);
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleSearchingPageInProgressSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_SEARCHING_ID) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QUEST_SEARCHING_ID);
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleBuildingStructuresPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QUEST_BUILDING_STRUCTURES_ID);
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleBuildingStructuresInProgressPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QUEST_BUILDING_STRUCTURES_ID) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QUEST_BUILDING_STRUCTURES_ID);
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("MainPage");
                break;
        }
    }

    @Override
    public void EndDialog() {

    }
}
