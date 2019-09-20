USE [Matha]
GO

update [dbo].[Districts]
set [name] = UPPER([name]);

update [dbo].[Districts]
set [state] = UPPER([state]);

GO
