USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table [dbo].[Customer]
add [email] nvarchar(120),
    [principal] nvarchar(120),
    [district] varchar(50),
    [state] varchar(50),
    [Outstanding] [decimal](12,2) NULL;
GO
