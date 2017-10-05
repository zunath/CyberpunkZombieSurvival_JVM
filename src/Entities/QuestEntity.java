package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="Quests")
public class QuestEntity {

    @Id
    @Column(name = "QuestID")
    private int questID;

    @Column(name = "Name")
    private String name;

    @Column(name = "JournalTag")
    private String journalTag;

    @Column(name = "QuestTypeID")
    private int questTypeID;

    @Column(name = "FameRegionID")
    private int fameRegionID;

    @Column(name = "RequiredFameAmount")
    private int requiredFameAmount;

    @Column(name = "AllowRewardSelection")
    private boolean allowRewardSelection;

    @Column(name = "RewardGold")
    private int rewardGold;

    @Column(name = "RewardXP")
    private int rewardXP;

    @Column(name = "RewardKeyItemID")
    private int rewardKeyItemID;

    @Column(name = "RewardFame")
    private int rewardFame;

    @Column(name = "FinishStateID")
    private int finishStateID;

    @Column(name = "IsRepeatable")
    private boolean isRepeatable;

    @Column(name = "MapNoteTag")
    private String mapNoteTag;

    @Column(name = "StartKeyItemID")
    private int startKeyItemID;

    @Column(name = "RemoveStartKeyItemAfterCompletion")
    private boolean removeStartKeyItemAfterCompletion;


    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJournalTag() {
        return journalTag;
    }

    public void setJournalTag(String journalTag) {
        this.journalTag = journalTag;
    }

    public int getQuestTypeID() {
        return questTypeID;
    }

    public void setQuestTypeID(int questTypeID) {
        this.questTypeID = questTypeID;
    }

    public int getFameRegionID() {
        return fameRegionID;
    }

    public void setFameRegionID(int fameRegionID) {
        this.fameRegionID = fameRegionID;
    }

    public int getRequiredFameAmount() {
        return requiredFameAmount;
    }

    public void setRequiredFameAmount(int requiredFameAmount) {
        this.requiredFameAmount = requiredFameAmount;
    }

    public boolean isAllowRewardSelection() {
        return allowRewardSelection;
    }

    public void setAllowRewardSelection(boolean allowRewardSelection) {
        this.allowRewardSelection = allowRewardSelection;
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public void setRewardGold(int rewardGold) {
        this.rewardGold = rewardGold;
    }

    public int getRewardXP() {
        return rewardXP;
    }

    public void setRewardXP(int rewardXP) {
        this.rewardXP = rewardXP;
    }

    public int getRewardKeyItemID() {
        return rewardKeyItemID;
    }

    public void setRewardKeyItemID(int rewardKeyItemID) {
        this.rewardKeyItemID = rewardKeyItemID;
    }

    public int getRewardFame() {
        return rewardFame;
    }

    public void setRewardFame(int rewardFame) {
        this.rewardFame = rewardFame;
    }

    public int getFinishStateID() {
        return finishStateID;
    }

    public void setFinishStateID(int finishStateID) {
        this.finishStateID = finishStateID;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public String getMapNoteTag() {
        return mapNoteTag;
    }

    public void setMapNoteTag(String mapNoteTag) {
        this.mapNoteTag = mapNoteTag;
    }

    public int getStartKeyItemID() {
        return startKeyItemID;
    }

    public void setStartKeyItemID(int startKeyItemID) {
        this.startKeyItemID = startKeyItemID;
    }

    public boolean isRemoveStartKeyItemAfterCompletion() {
        return removeStartKeyItemAfterCompletion;
    }

    public void setRemoveStartKeyItemAfterCompletion(boolean removeStartKeyItemAfterCompletion) {
        this.removeStartKeyItemAfterCompletion = removeStartKeyItemAfterCompletion;
    }
}
