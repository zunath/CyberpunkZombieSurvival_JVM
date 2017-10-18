
SELECT qktl.QuestKillTargetListID ,
       qktl.QuestID ,
       qktl.NPCGroupID ,
       qktl.Quantity
FROM dbo.QuestKillTargetList qktl
WHERE qktl.QuestID = :questID
