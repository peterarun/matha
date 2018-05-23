USE [Matha]
GO

select GETDATE();
GO

SELECT MAX([SerialId]) from [dbo].[PReturn];
GO

SELECT MAX([SerialId]) from [dbo].[Purchase];
GO

SELECT MAX([SerialId]) from [dbo].[Sales];
GO

SELECT MAX([SerialId]) from [dbo].[SOrder];
GO
