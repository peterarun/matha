USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Addresses](
	[Name] [nvarchar](120) PRIMARY KEY,
	[address1] [nvarchar](120) NULL,
	[address2] [nvarchar](120) NULL,
	[address3] [nvarchar](120) NULL,
	[phone1] [nvarchar](50) NULL,
	[phone2] [nvarchar](50) NULL,
	[Email] [nvarchar](120) NULL,
	[PIN] [nvarchar](12) NULL);
GO
