USE [Matha]
GO

INSERT INTO [dbo].[State]
           ([name])
SELECT [name]
  FROM [Matha_Old].[dbo].[State]

GO
