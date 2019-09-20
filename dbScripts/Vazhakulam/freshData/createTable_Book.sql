USE [Matha]
GO

/****** Object:  Table [dbo].[Book]    Script Date: 24-Aug-18 7:00:13 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Book]
(
	[BookId] [int] IDENTITY(1,1) NOT NULL,
	[BookNo] [nvarchar](60) NULL,
	[BName] [nvarchar](120) NULL,
	[PubId] [int] NULL,
	[Price] [decimal](12, 2) NULL,
	[Inventory] [int] NULL,
	CONSTRAINT [IX_Book] PRIMARY KEY CLUSTERED 
	(
		[BookId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Book]  WITH CHECK ADD  CONSTRAINT [FK_Book_Supplier] FOREIGN KEY([PubId])
REFERENCES [dbo].[Supplier] ([PublisherId])
GO

ALTER TABLE [dbo].[Book] CHECK CONSTRAINT [FK_Book_Supplier]
GO


