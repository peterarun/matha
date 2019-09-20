USE [Matha]
GO

/****** Object:  Table [dbo].[PReturnDet]    Script Date: 25-Aug-18 10:51:43 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PReturnDet](
	[PReturnDetId] [int] IDENTITY(1,1) NOT NULL,
	[SerialId] [nvarchar](15) NULL,
	[BookId] [int] NULL,
	[BookName] [nvarchar](120) NULL,
	[BookPrice] [decimal](8, 2) NULL,
	[Qty] [decimal](12, 2) NULL,
	[SlNo] [int] NULL,
	CONSTRAINT [PReturnDet_PK] PRIMARY KEY CLUSTERED 
	(
		[PReturnDetId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[PReturnDet]  WITH CHECK ADD  CONSTRAINT [FK_PReturnDet_PReturn] FOREIGN KEY([SerialId])
REFERENCES [dbo].[PReturn] ([SerialId])
GO

ALTER TABLE [dbo].[PReturnDet] CHECK CONSTRAINT [FK_PReturnDet_PReturn]
GO


ALTER TABLE [dbo].[PReturnDet]  WITH CHECK ADD  CONSTRAINT [FK_PReturnDet_Book] FOREIGN KEY([BookId])
REFERENCES [dbo].[Book] ([BookId])
GO

ALTER TABLE [dbo].[PReturnDet] CHECK CONSTRAINT [FK_PReturnDet_Book]
GO

