

CREATE TABLE StructureQuickBuildAudit(
    StructureQuickBuildID int IDENTITY not null,
    PCTerritoryFlagID int null,
    PCTerritoryFlagStructureID bigint null,
    DMName NVARCHAR(200) not null default '',
    DateBuilt DATETIME2 not null default GetUTCDate(),

    CONSTRAINT PK_StructureQuickBuildAudit_StructureQuickBuildID PRIMARY KEY(StructureQuickBuildID),
    CONSTRAINT FK_StructureQuickBuildAudit_PCTerritoryFlagID FOREIGN KEY(PCTerritoryFlagID)
        REFERENCES PCTerritoryFlags(PCTerritoryFlagID),
    CONSTRAINT FK_StructureQuickBuildAudit_PCTerritoryFlagStructureID FOREIGN KEY(PCTerritoryFlagStructureID)
        REFERENCES PCTerritoryFlagsStructures(PCTerritoryFlagStructureID)
)