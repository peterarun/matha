USE [Matha]
GO

/****** Object:  Table [dbo].[Supplier]    Script Date: 25-Aug-18 12:13:19 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

INSERT INTO [Matha].[dbo].[Supplier]
           ([PublisherId]
           ,[SName]
           ,[Address1]
           ,[Address2]
           ,[Address3]
           ,[PhNo1]
           ,[PhNo2]
           ,[Remarks]
           ,[GSTIN]
           ,[Email]
           ,[PIN])
		   select [PublisherId]
           ,[SName]
           ,[Address1]
           ,[Address2]
           ,[Address3]
           ,[PhNo1]
           ,[PhNo2]
           ,[Remarks]
           ,[GSTIN]
           ,[Email]
           ,[PIN]
		   from [Matha_Old].[dbo].[Supplier];

GO


