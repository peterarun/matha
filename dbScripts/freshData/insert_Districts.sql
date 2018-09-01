USE [Matha]
GO

INSERT INTO [dbo].[Districts]
           ([name]
           ,[state])
	select [name]
           ,[state]
		   from [Matha_Old].[dbo].[Districts]
GO
