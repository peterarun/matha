USE [MathaDist]
GO

/****** Object:  Table [dbo].[Purchase]    Script Date: 25-Aug-18 11:05:08 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Purchase](
	[SerialId] [nvarchar](15) NULL,
	[TDate] [datetime] NULL,
	[GRNo] [nvarchar](20) NULL,
	[InvNo] [nvarchar](30) NULL,
	[SubTotal] [decimal](12, 2) NULL,
	[Discount] [decimal](12, 2) NULL,
	[NetAmt] [decimal](12, 2) NULL,
	[Fy] [nvarchar](4) NULL,
	[Notes] [nvarchar](120) NULL,
	[DespatchedTo] [nvarchar](20) NULL,
	[DocumentsThrough] [nvarchar](20) NULL,
	[DespatchPer] [nvarchar](20) NULL,
	[Packages] [int] NULL,
	[DiscountType] [bit] NULL,
	[TxnId] [int] NULL,
	[PublisherId] [nvarchar](10) NULL,
	[Status] [int] NULL,
	CONSTRAINT [IX_Purchase] UNIQUE NONCLUSTERED 
	(
		[SerialId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY],
	CONSTRAINT [IX_Purchase_1] UNIQUE NONCLUSTERED 
	(
		[InvNo] ASC,
		[Fy] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO


