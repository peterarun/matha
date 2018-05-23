USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [dbo].[Purchase] ADD [DespatchedTo] [nvarchar](20) NULL;
GO

ALTER TABLE [dbo].[Purchase] ADD [DocumentsThrough] [nvarchar](20) NULL;
GO

ALTER TABLE [dbo].[Purchase] ADD [DespatchPer] [nvarchar](20) NULL;
GO

ALTER TABLE [dbo].[Purchase] ADD [Packages] [INT] NULL;
GO

ALTER TABLE [dbo].[Purchase] ADD [DiscountType] [BIT] NULL;
GO

ALTER TABLE [dbo].[Purchase] ADD [TxnId] [INT] NULL;
GO

ALTER TABLE [dbo].[Purchase] ADD [PublisherId] NVARCHAR(10) NULL;
GO

CREATE NONCLUSTERED INDEX [IX_Purchase_InvNo] ON [dbo].[Purchase]
(
	[InvNo] ASC
) ON [PRIMARY];
GO

update pur set PublisherId = sup.PublisherId
  from [Purchase]  pur
  join Supplier sup on pur.SuppId = sup.SuppId
GO
