package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PCQuestStatus")
public class PCQuestStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PCQuestStatusID")
    private int pcQuestStatusID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestID", updatable = false, insertable = false)
    private QuestEntity quest;

    @Column(name = "CurrentStateID")
    private int currentStateID;

    @Column(name = "CompletionDate")
    private Timestamp completionDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SelectedItemRewardID", updatable = false, insertable = false)
    private QuestRewardItemEntity selectedItemReward;

    public int getPcQuestStatusID() {
        return pcQuestStatusID;
    }

    public void setPcQuestStatusID(int pcQuestStatusID) {
        this.pcQuestStatusID = pcQuestStatusID;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }

    public int getCurrentStateID() {
        return currentStateID;
    }

    public void setCurrentStateID(int currentStateID) {
        this.currentStateID = currentStateID;
    }

    public Timestamp getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Timestamp completionDate) {
        this.completionDate = completionDate;
    }

    public QuestRewardItemEntity getSelectedItemReward() {
        return selectedItemReward;
    }

    public void setSelectedItemReward(QuestRewardItemEntity selectedItemReward) {
        this.selectedItemReward = selectedItemReward;
    }
}
