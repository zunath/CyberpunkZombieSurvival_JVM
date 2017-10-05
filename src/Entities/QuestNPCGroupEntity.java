package Entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="QuestNPCGroups")
public class QuestNPCGroupEntity {
    @Id
    @Column(name = "QuestNPCGroupID")
    private int questNPCGroupID;

    @Column(name = "Name")
    private String name;

    public int getQuestNPCGroupID() {
        return questNPCGroupID;
    }

    public void setQuestNPCGroupID(int questNPCGroupID) {
        this.questNPCGroupID = questNPCGroupID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
