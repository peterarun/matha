USE [Matha]
GO

select GETDATE();
GO

SELECT MAX(cast( substring([SerialId], 4, 10) as int)) from [dbo].[PReturn];
GO

SELECT MAX(cast( substring([SerialId], 3, 10) as int)) from [dbo].[Purchase];
GO

SELECT MAX(cast( substring([SerialId], 3, 10) as int)) from [dbo].[Sales];
GO

SELECT MAX(cast( substring([SerialId], 4, 10) as int)) from [dbo].[SOrder];
GO
