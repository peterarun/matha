USE [Matha]
GO

/****** Object:  Table [dbo].[STrans]    Script Date: 24-01-2018 09:00:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PPayment](
	[SerialId] [int] PRIMARY KEY IDENTITY(1,1),
	[Mode] [nvarchar](10) NULL,
	[TxnId] [int] NOT NULL
) ON [PRIMARY]
GO


