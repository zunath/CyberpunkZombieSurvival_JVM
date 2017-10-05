package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestKillTargetList")
public class QuestKillTargetListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestKillTargetListID")
    private int questKillTargetListID;

    @Column(name = "QuestID")
    private int questID;

    @Column(name = "QuestNPCGroupID")
    private int questNPCGroupID;

    @Column(name = "Quantity")
    private int quantity;

    public int getQuestKillTargetListID() {
        return questKillTargetListID;
    }

    public void setQuestKillTargetListID(int questKillTargetListID) {
        this.questKillTargetListID = questKillTargetListID;
    }

    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public int getQuestNPCGroupID() {
        return questNPCGroupID;
    }

    public void setQuestNPCGroupID(int questNPCGroupID) {
        this.questNPCGroupID = questNPCGroupID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
