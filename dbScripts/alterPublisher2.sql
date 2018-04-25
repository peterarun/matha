USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table [dbo].[Publisher]
add [Outstanding] [decimal](12,2) NULL;

GO
