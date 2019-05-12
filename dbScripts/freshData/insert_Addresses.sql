USE [MathaDist]
GO

/****** Object:  Table [dbo].[Addresses]    Script Date: 25-Aug-18 12:13:19 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

USE [MathaDist]
GO

INSERT INTO [dbo].[Addresses]
           ([Name]
           ,[address1]
           ,[address2]
           ,[address3]
           ,[phone1]
           ,[phone2]
           ,[Email]
           ,[PIN])
     VALUES
            ('Purchase'
            ,'No.88, 8th Street, A.K.Swamy Nagar'
            ,'Kilpauk'
            ,'Chennai'
            ,'09444026149'
            ,NULL
            ,NULL
            ,'600010');

GO

INSERT INTO [dbo].[Addresses]
           ([Name]
           ,[address1]
           ,[address2]
           ,[address3]
           ,[phone1]
           ,[phone2]
           ,[Email]
           ,[PIN])
     VALUES
            ('Sales'
            ,'No.88, 8th Street, A.K.Swamy Nagar'
            ,'Kilpauk'
            ,'Chennai'
            ,'9444026149'
            ,'8281461689'
            ,'mathaabooks@gmail.com'
            ,'600010');

GO
