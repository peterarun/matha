USE [MathaDist]
GO

INSERT INTO [dbo].[Customer]
           ([CustId]
           ,[CName]
           ,[Address1]
           ,[Address2]
           ,[Address3]
           ,[Add4]
           ,[Add5]
           ,[PhNo1]
           ,[PhNo2]
           ,[Remarks]
           ,[email]
           ,[principal]
           ,[district]
           ,[state])
     SELECT  [CustId]
            ,[CName]
            ,[Address1]
            ,[Address2]
            ,[Address3]
            ,[Add4]
            ,[Add5]
            ,[PhNo1]
            ,[PhNo2]
            ,[Remarks]
            ,[email]
            ,[principal]
            ,[district]
            ,[state] FROM [Matha].[dbo].[Customer];

GO
