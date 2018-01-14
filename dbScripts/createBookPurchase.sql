USE [MathaNew]
GO

/****** Object:  Table [dbo].[Purchase]    Script Date: 13-01-2018 14:29:40 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[BookPurchase](
	[Id] [int] IDENTITY PRIMARY KEY,
	[Publisher] [nvarchar](10) NOT NULL,
	[PurchaseDate] [datetime] NULL,
	[DeliveryDate] [datetime] NULL,
	[GrNo] [nvarchar](20) NULL,
	[InvoiceNo] [nvarchar](30) NULL,
	[SubTotal] [decimal](12, 2) NULL,
	[Taxes] [decimal](12, 2) NULL,
	[Discount] [decimal](12, 2) NULL,
	[DiscountType] [nchar](10) NULL,
	[PaidAmount] [decimal](12, 2) NULL,
	[Notes] [nvarchar](120) NULL
 CONSTRAINT [IX_Book_Purchase] UNIQUE NONCLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY],
) ON [PRIMARY]
GO


