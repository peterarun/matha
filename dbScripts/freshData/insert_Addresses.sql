USE [Matha]
GO

/****** Object:  Table [dbo].[Addresses]    Script Date: 25-Aug-18 12:13:19 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

INSERT INTO [Matha].[dbo].[Addresses]
           ([Name]
           ,[address1]
           ,[address2]
           ,[address3]
           ,[phone1]
           ,[phone2]
           ,[Email]
           ,[PIN])
          SELECT [Name]
           ,[address1]
           ,[address2]
           ,[address3]
           ,[phone1]
           ,[phone2]
           ,[Email]
           ,[PIN]
		   from [Matha_Old].[dbo].[Addresses];

GO
