USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table [dbo].[Publisher]
add 
address1 nvarchar(120),
address2 nvarchar(120),
address3 nvarchar(120),
phone1 nvarchar(50),
phone2 nvarchar(50),
GSTIN varchar(30),
Email nvarchar(120),
PIN nvarchar(12);
