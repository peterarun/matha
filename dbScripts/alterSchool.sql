USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table [dbo].[Customer]
add 
email nvarchar(120),
city nvarchar(120),
principal nvarchar(120),
pin nvarchar(12),
district varchar(50),
state varchar(50);
