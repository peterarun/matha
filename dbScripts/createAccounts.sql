USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[BankAccounts](
	[Name] [nvarchar](120) PRIMARY KEY,
	[AccNum] [nvarchar](120) NULL,
	[IFSC] [nvarchar](120) NULL,
	[address] [nvarchar](120) NULL,
	[phone1] [nvarchar](50) NULL);
GO
