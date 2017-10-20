package Entities;


import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestRequiredKeyItemList")
public class QuestRequiredKeyItemListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestRequiredKeyItemID")
    private int questRequiredKeyItemID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QuestID", updatable = false, insertable = false)
    private QuestEntity quest;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KeyItemID", updatable = false, insertable = false)
    private KeyItemEntity keyItem;

    public int getQuestRequiredKeyItemID() {
        return questRequiredKeyItemID;
    }

    public void setQuestRequiredKeyItemID(int questRequiredKeyItemID) {
        this.questRequiredKeyItemID = questRequiredKeyItemID;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }

    public KeyItemEntity getKeyItem() {
        return keyItem;
    }

    public void setKeyItem(KeyItemEntity keyItem) {
        this.keyItem = keyItem;
    }

}
