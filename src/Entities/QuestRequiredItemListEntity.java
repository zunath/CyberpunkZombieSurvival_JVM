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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestID", updatable = false, insertable = false)
    private QuestEntity quest;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "Quantity")
    private int quantity;

    public int getQuestRequiredItemListID() {
        return questRequiredItemListID;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }
    public void setQuestRequiredItemListID(int questRequiredItemListID) {
        this.questRequiredItemListID = questRequiredItemListID;
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
