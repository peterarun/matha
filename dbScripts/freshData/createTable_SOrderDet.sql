USE [Matha]
GO

/****** Object:  Table [dbo].[SOrderDet]    Script Date: 25-Aug-18 11:51:50 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SOrderDet](
	[OrderDetId] [int] IDENTITY(1,1) NOT NULL,
	[SerialId] [nvarchar](15) NULL,
	[BookId] [int] NULL,
	[BookName] [nvarchar](120) NULL,
	[BookPrice] [decimal](8, 2) NULL,
	[Qty] [decimal](12, 2) NULL,
	[SlNo] [int] NULL,
	CONSTRAINT [SOrderDet_UK] PRIMARY KEY CLUSTERED 
	(
		[OrderDetId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SOrderDet]  WITH CHECK ADD  CONSTRAINT [FK_SOrderDet_SOrder] FOREIGN KEY([SerialId])
REFERENCES [dbo].[SOrder] ([SerialId])
GO

ALTER TABLE [dbo].[SOrderDet] CHECK CONSTRAINT [FK_SOrderDet_SOrder]
GO

ALTER TABLE [dbo].[SOrderDet]  WITH CHECK ADD  CONSTRAINT [FK_SOrderDet_Book] FOREIGN KEY([BookId])
REFERENCES [dbo].[Book] ([BookId])
GO

ALTER TABLE [dbo].[SOrderDet] CHECK CONSTRAINT [FK_SOrderDet_Book]
GO

