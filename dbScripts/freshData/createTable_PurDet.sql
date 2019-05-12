USE [MathaDist]
GO

/****** Object:  Table [dbo].[PurDet]    Script Date: 25-Aug-18 11:16:57 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PurDet](
	[PurDetId] [int] IDENTITY(1,1) NOT NULL,
	[SerialId] [nvarchar](15) NULL,
	[BookId] [int] NULL,
	[BookName] [nvarchar](120) NULL,
	[Qty] [decimal](12, 2) NULL,
	[Rate] [decimal](12, 2) NULL,
	[SlNo] [int] NULL,	
	[OrderItemId] [int] NULL,
	CONSTRAINT [PurDetId_PK] PRIMARY KEY CLUSTERED 
	(
		[PurDetId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[PurDet]  WITH CHECK ADD  CONSTRAINT [FK_PurDet_Purchase] FOREIGN KEY([SerialId])
REFERENCES [dbo].[Purchase] ([SerialId])
GO

ALTER TABLE [dbo].[PurDet] CHECK CONSTRAINT [FK_PurDet_Purchase]
GO

ALTER TABLE [dbo].[PurDet]  WITH CHECK ADD  CONSTRAINT [FK_PurDet_Book] FOREIGN KEY([BookId])
REFERENCES [dbo].[Book] ([BookId])
GO

ALTER TABLE [dbo].[PurDet] CHECK CONSTRAINT [FK_PurDet_Book]
GO

ALTER TABLE [dbo].[PurDet]  WITH CHECK ADD  CONSTRAINT [FK_PurDet_SOrderDet] FOREIGN KEY([OrderItemId])
REFERENCES [dbo].[SOrderDet] ([OrderDetId])
GO

ALTER TABLE [dbo].[PurDet] CHECK CONSTRAINT [FK_PurDet_SOrderDet]
GO

