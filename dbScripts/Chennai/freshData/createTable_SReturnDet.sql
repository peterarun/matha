USE [MathaDist]
GO

/****** Object:  Table [dbo].[SReturnDet]    Script Date: 25-Aug-18 12:04:44 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SReturnDet](
	[SReturnDetId] [int] IDENTITY(1,1) NOT NULL,
	[SerialId] [nvarchar](10) NULL,
	[BookId] [int] NULL,
	[BookName] [nvarchar](120) NULL,
	[Qty] [decimal](12, 2) NULL,
	[Rate] [decimal](12, 2) NULL,
	[SlNo] [int] NULL,
	CONSTRAINT [SReturnDet_PK] PRIMARY KEY CLUSTERED 
	(
		[SReturnDetId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SReturnDet]  WITH CHECK ADD  CONSTRAINT [FK_SReturnDet_SReturn] FOREIGN KEY([SerialId])
REFERENCES [dbo].[SReturn] ([SerialId])
GO

ALTER TABLE [dbo].[SReturnDet] CHECK CONSTRAINT [FK_SReturnDet_SReturn]
GO

ALTER TABLE [dbo].[SReturnDet]  WITH CHECK ADD  CONSTRAINT [FK_SReturnDet_Book] FOREIGN KEY([BookId])
REFERENCES [dbo].[Book] ([BookId])
GO

ALTER TABLE [dbo].[SReturnDet] CHECK CONSTRAINT [FK_SReturnDet_Book]
GO
