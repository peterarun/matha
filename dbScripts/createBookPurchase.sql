USE [MathaNew]
GO

/****** Object:  Table [dbo].[Purchase]    Script Date: 13-01-2018 14:29:40 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[BookPurchase](
	[Id] [int] PRIMARY KEY,
	[DespatchedTo] [nvarchar](20) NULL,
	[DocumentsThrough] [nvarchar](20) NULL,
	[DespatchPer] [nvarchar](20) NULL,	
	[GrNo] [nvarchar](20) NULL,
	[Packages] [int] NULL,
	[Discount] [decimal](12, 2) NULL,
	[DiscountType] [bit] NULL,
	[SubTotal] [decimal](12, 2) NULL,
	[TxnId] [int] NOT NULL,
	[PurchaseDate] [datetime] NULL
 CONSTRAINT [IX_Book_Purchase] UNIQUE NONCLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY],
) ON [PRIMARY]
GO


