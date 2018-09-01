USE [Matha]
GO

/****** Object:  Table [dbo].[BankAccounts]    Script Date: 24-Aug-18 6:58:36 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[BankAccounts](
	[Name] [nvarchar](120) NOT NULL,
	[AccNum] [nvarchar](120) NULL,
	[IFSC] [nvarchar](120) NULL,
	[address] [nvarchar](120) NULL,
	[phone1] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


