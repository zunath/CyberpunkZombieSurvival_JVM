package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestRequiredItemList")
public class QuestRequiredItemListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestRequiredItemListID")
    private int questRequiredItemListID;

    @Column(name = "QuestID")
    private int questID;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "Quantity")
    private int quantity;

    public int getQuestRequiredItemListID() {
        return questRequiredItemListID;
    }

    public void setQuestRequiredItemListID(int questRequiredItemListID) {
        this.questRequiredItemListID = questRequiredItemListID;
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
