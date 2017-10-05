package Entities;


import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestRequiredKeyItemList")
public class QuestRequiredKeyItemListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestRequiredKeyItemID")
    private int questRequiredKeyItemID;

    @Column(name = "QuestID")
    private int questID;

    @Column(name = "KeyItemID")
    private int keyItemID;

    public int getQuestRequiredKeyItemID() {
        return questRequiredKeyItemID;
    }

    public void setQuestRequiredKeyItemID(int questRequiredKeyItemID) {
        this.questRequiredKeyItemID = questRequiredKeyItemID;
    }

    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public int getKeyItemID() {
        return keyItemID;
    }

    public void setKeyItemID(int keyItemID) {
        this.keyItemID = keyItemID;
    }

}
