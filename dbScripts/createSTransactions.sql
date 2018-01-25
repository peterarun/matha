USE [MathaNew]
GO

/****** Object:  Table [dbo].[STrans]    Script Date: 24-01-2018 09:00:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[STransactions](
	[SerialId] [int] PRIMARY KEY IDENTITY(1,1),
	[TxnDate] [date] NOT NULL,
	[Amount] [decimal](12,2) NOT NULL,
	[SchoolId] [varchar](15) NULL,
	[TxnType] [nvarchar](10) NULL,
	[TxnId] [int] NULL,
	[Note] [nvarchar](100) NULL,
	[SalesId] [varchar](15) NULL,
	[PaymentId] [int] NULL,
	[ReturnId] [int] NULL
) ON [PRIMARY]
GO


