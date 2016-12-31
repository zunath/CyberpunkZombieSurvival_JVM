package Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="PCLearnedAbilities")
public class PCLearnedAbilityEntity {

    @Id
    @Column(name = "PCLearnedAbilityID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pcAbilityID;

    @Column(name = "PlayerID")
    private String playerID;

    @Column(name = "AcquiredDate")
    private Timestamp acquiredDate;

    @Column(name = "AbilityID")
    private int abilityID;

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

    public int getAbilityID() {
        return abilityID;
    }

    public void setAbilityID(int abilityID) {
        this.abilityID = abilityID;
    }
}
