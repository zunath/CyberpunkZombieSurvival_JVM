

CREATE TABLE ClientLogEventTypesDomain(
  ClientLogEventTypeID INT,
  Name NVARCHAR(30) NOT NULL,

  CONSTRAINT PK_ClientLogEventTypesDomain_ClientLogEventTypeID PRIMARY KEY(ClientLogEventTypeID)

)


INSERT INTO ClientLogEventTypesDomain(ClientLogEventTypeID, Name)
VALUES (1, 'Log In')
INSERT INTO ClientLogEventTypesDomain(ClientLogEventTypeID, Name)
VALUES (2, 'Log Out')

CREATE TABLE ClientLogEvents (

  ClientLogEventID INT NOT NULL IDENTITY,
  ClientLogEventTypeID INT NOT NULL,
  PlayerID NVARCHAR(60) NOT NULL,
  CDKey NVARCHAR(20) NOT NULL,
  AccountName NVARCHAR(1024) NOT NULL,
  DateOfEvent DATETIME2 NOT NULL DEFAULT GETUTCDATE(),

  CONSTRAINT PK_ClientLogEvents_ClientLogEventID PRIMARY KEY (ClientLogEventID),
  CONSTRAINT FK_ClientLogEvents_ClientLogEventTypeID FOREIGN KEY(ClientLogEventTypeID) REFERENCES ClientLogEventTypesDomain(ClientLogEventTypeID),
  CONSTRAINT FK_ClientLogEvents_PlayerID FOREIGN KEY (PlayerID) REFERENCES PlayerCharacters(PlayerID)

)

