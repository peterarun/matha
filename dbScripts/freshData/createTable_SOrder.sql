USE [Matha]
GO

/****** Object:  Table [dbo].[SOrder]    Script Date: 25-Aug-18 11:38:13 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SOrder](
	[SerialId] [nvarchar](15) NULL,
	[SerialNo] [nvarchar](30) NULL,
	[TDate] [datetime] NULL,
	[CustId] [nvarchar](10) NULL,
	[DlyDate] [datetime] NULL,
	[Fy] [nvarchar](4) NULL,
	[Prefix] [nvarchar](30) NULL,
	[desLocation] [nvarchar](50) NULL,
	CONSTRAINT [IX_SOrder] UNIQUE NONCLUSTERED 
	(
		[SerialId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY],
	CONSTRAINT [IX_SOrder_1] UNIQUE NONCLUSTERED 
	(
		[SerialNo] ASC,
		[Fy] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY],
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SOrder]  WITH CHECK ADD  CONSTRAINT [FK_SOrder_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[SOrder] CHECK CONSTRAINT [FK_SOrder_Customer]
GO

