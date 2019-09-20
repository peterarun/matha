USE [Matha]
GO

/****** Object:  Table [dbo].[SPayment]    Script Date: 25-Aug-18 11:56:55 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SPayment](
	[SerialId] [int] IDENTITY(1,1) NOT NULL,
	[CustId] [nvarchar](10) NULL,
	[ReceiptNum] [nvarchar](20) NOT NULL,
	[Fy] [int] NULL,
	[Mode] [nvarchar](10) NULL,
	[Dated] [datetime] NULL,
	[RefNum] [nvarchar](200) NULL,
	[TxnId] [int] NULL,
	[Status] [int] NULL,
	PRIMARY KEY CLUSTERED 
	(
		[SerialId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
	CONSTRAINT [IX_SPayment_1] UNIQUE NONCLUSTERED 
	(
		[ReceiptNum] ASC,
		[Fy] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SPayment]  WITH CHECK ADD  CONSTRAINT [FK_SPayment_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[SPayment] CHECK CONSTRAINT [FK_SPayment_Customer]
GO

