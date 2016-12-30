package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="PCAbilities")
public class PCAbilityEntity {

    @Id
    @Column(name = "PCAbilityID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pcAbilityID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "AcquiredDate")
    private Timestamp acquiredDate;

    @Column(name = "FeatID")
    private int featID;

    public int getPcBlueprintID() {
        return pcAbilityID;
    }

    public void setPcBlueprintID(int pcAbilityID) {
        this.pcAbilityID = pcAbilityID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public Timestamp getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(Timestamp acquiredDate) {
        this.acquiredDate = acquiredDate;
    }

    public int getFeatID() {
        return featID;
    }

    public void setFeatID(int featID){
        this.featID = featID;
    }

}
