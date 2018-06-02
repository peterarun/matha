USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table [dbo].[Sales] add TxnId int null;
GO

alter table [dbo].[Sales] add discType bit null;
GO
--
--delete from [dbo].[Sales]
--where TDate < '2018-02-01';
--GO
