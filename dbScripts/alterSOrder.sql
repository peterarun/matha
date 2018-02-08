USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table SOrder add desLocation nvarchar(50) null;
GO

alter table SOrder add SaleId nvarchar(15) null;
GO
