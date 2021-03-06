
SELECT pc.PlayerID ,
       pc.CharacterName ,
       pc.HitPoints ,
       pc.LocationAreaTag ,
       pc.LocationX ,
       pc.LocationY ,
       pc.LocationZ ,
       pc.LocationOrientation ,
       pc.CreateTimestamp ,
       pc.InfectionCap ,
       pc.CurrentInfection ,
       pc.InfectionRemovalTick ,
       pc.MaxHunger ,
       pc.CurrentHunger ,
       pc.CurrentHungerTick ,
       pc.UnallocatedSP ,
       pc.Level ,
       pc.Experience ,
       pc.NextSPResetDate ,
       pc.NumberOfSPResets ,
       pc.ResetTokens ,
       pc.NextResetTokenReceiveDate ,
       pc.HPRegenerationAmount ,
       pc.InventorySpaceBonus ,
       pc.RegenerationTick ,
       pc.RegenerationRate ,
       pc.ZombieKillCount ,
       pc.VersionNumber ,
       pc.MaxMana ,
       pc.CurrentMana ,
       pc.CurrentManaTick ,
       pc.ProfessionID ,
       pc.RevivalStoneCount ,
       pc.RespawnAreaTag ,
       pc.RespawnLocationX ,
       pc.RespawnLocationY ,
       pc.RespawnLocationZ ,
       pc.RespawnLocationOrientation ,
       pc.DateLastForcedSPReset ,
       pc.DateSanctuaryEnds ,
       pc.IsSanctuaryOverrideEnabled,
       pc.DisplayHelmet
FROM dbo.PlayerCharacters pc
WHERE pc.PlayerID = :playerID