USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table Sales add TxnId int null;
GO

alter table Sales add discType bit null;
GO
