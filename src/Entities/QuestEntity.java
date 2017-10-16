package Entities;

import javax.persistence.*;
import java.util.List;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FameRegionID", updatable = false, insertable = false)
    private FameRegionEntity fameRegion;

    @Column(name = "RequiredFameAmount")
    private int requiredFameAmount;

    @Column(name = "AllowRewardSelection")
    private boolean allowRewardSelection;

    @Column(name = "RewardGold")
    private int rewardGold;

    @Column(name = "RewardXP")
    private int rewardXP;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RewardKeyItemID", updatable = false, insertable = false)
    private KeyItemEntity rewardKeyItem;

    @Column(name = "RewardFame")
    private int rewardFame;

    @Column(name = "IsRepeatable")
    private boolean isRepeatable;

    @Column(name = "MapNoteTag")
    private String mapNoteTag;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "StartKeyItemID", updatable = false, insertable = false)
    private KeyItemEntity startKeyItemID;

    @Column(name = "RemoveStartKeyItemAfterCompletion")
    private boolean removeStartKeyItemAfterCompletion;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestKillTargetListEntity> killTargets;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestRewardItemEntity> rewardItems;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestRequiredItemListEntity> requiredItems;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestRequiredKeyItemListEntity> requiredKeyItems;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<QuestPrerequisiteEntity> prerequisiteQuests;



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

    public int getRewardFame() {
        return rewardFame;
    }

    public void setRewardFame(int rewardFame) {
        this.rewardFame = rewardFame;
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

    public boolean isRemoveStartKeyItemAfterCompletion() {
        return removeStartKeyItemAfterCompletion;
    }

    public void setRemoveStartKeyItemAfterCompletion(boolean removeStartKeyItemAfterCompletion) {
        this.removeStartKeyItemAfterCompletion = removeStartKeyItemAfterCompletion;
    }

    public FameRegionEntity getFameRegion() {
        return fameRegion;
    }

    public void setFameRegion(FameRegionEntity fameRegion) {
        this.fameRegion = fameRegion;
    }

    public KeyItemEntity getRewardKeyItem() {
        return rewardKeyItem;
    }

    public void setRewardKeyItem(KeyItemEntity rewardKeyItem) {
        this.rewardKeyItem = rewardKeyItem;
    }

    public KeyItemEntity getStartKeyItemID() {
        return startKeyItemID;
    }

    public void setStartKeyItemID(KeyItemEntity startKeyItemID) {
        this.startKeyItemID = startKeyItemID;
    }

    public List<QuestKillTargetListEntity> getKillTargets() {
        return killTargets;
    }

    public void setKillTargets(List<QuestKillTargetListEntity> killTargets) {
        this.killTargets = killTargets;
    }

    public List<QuestRewardItemEntity> getRewardItems() {
        return rewardItems;
    }

    public void setRewardItems(List<QuestRewardItemEntity> rewardItems) {
        this.rewardItems = rewardItems;
    }

    public List<QuestRequiredItemListEntity> getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(List<QuestRequiredItemListEntity> requiredItems) {
        this.requiredItems = requiredItems;
    }

    public List<QuestRequiredKeyItemListEntity> getRequiredKeyItems() {
        return requiredKeyItems;
    }

    public void setRequiredKeyItems(List<QuestRequiredKeyItemListEntity> requiredKeyItems) {
        this.requiredKeyItems = requiredKeyItems;
    }

    public List<QuestPrerequisiteEntity> getPrerequisiteQuests() {
        return prerequisiteQuests;
    }

    public void setPrerequisiteQuests(List<QuestPrerequisiteEntity> prerequisiteQuests) {
        this.prerequisiteQuests = prerequisiteQuests;
    }
}
