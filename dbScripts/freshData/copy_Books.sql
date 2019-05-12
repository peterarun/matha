USE [MathaDist]
GO

INSERT INTO [dbo].[Book]
           ([BookNo]
           ,[BName]
           ,[PubId]
           ,[Price]
           ,[Inventory])
	SELECT [BookNo]
           ,[BName]
           ,[PubId]
           ,[Price]
           ,[Inventory] FROM [Matha].[dbo].[Book]

GO
