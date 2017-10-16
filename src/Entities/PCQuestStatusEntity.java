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

    @Column(name = "PlayerID")
    private String playerID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestID")
    private QuestEntity quest;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CurrentQuestStateID")
    private QuestStateEntity currentQuestState;

    @Column(name = "CompletionDate")
    private Timestamp completionDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SelectedItemRewardID")
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

    public QuestStateEntity getCurrentQuestState() {
        return currentQuestState;
    }

    public void setCurrentQuestState(QuestStateEntity currentQuestState) {
        this.currentQuestState = currentQuestState;
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

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }
}
