USE [Matha]
GO

/****** Object:  Table [dbo].[Supplier]    Script Date: 25-Aug-18 12:13:19 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

INSERT INTO [dbo].[BankAccounts]
           ([Name]
           ,[AccNum]
           ,[IFSC]
           ,[address]
           ,[phone1])
	SELECT [Name]
		  ,[AccNum]
		  ,[IFSC]
		  ,[address]
		  ,[phone1]
	  FROM [Matha_Old].[dbo].[BankAccounts];

GO
