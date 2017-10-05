package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestRewardList")
public class QuestRewardListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestRewardListID")
    private int questRewardListID;

    @Column(name = "QuestID")
    private int questID;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "Quantity")
    private int quantity;

    public int getQuestRewardListID() {
        return questRewardListID;
    }

    public void setQuestRewardListID(int questRewardListID) {
        this.questRewardListID = questRewardListID;
    }

    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
