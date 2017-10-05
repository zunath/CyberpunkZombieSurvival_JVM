package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestPrerequisites")
public class QuestPrerequisiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QuestPrerequisiteID")
    private int questPrerequisiteID;

    @Column(name = "QuestID")
    private int questID;

    @Column(name = "RequiredQuestID")
    private int requiredQuestID;

    public int getQuestPrerequisiteID() {
        return questPrerequisiteID;
    }

    public void setQuestPrerequisiteID(int questPrerequisiteID) {
        this.questPrerequisiteID = questPrerequisiteID;
    }

    public int getQuestID() {
        return questID;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public int getRequiredQuestID() {
        return requiredQuestID;
    }

    public void setRequiredQuestID(int requiredQuestID) {
        this.requiredQuestID = requiredQuestID;
    }
}
