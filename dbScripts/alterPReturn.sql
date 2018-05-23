USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [dbo].[PReturn] ADD [TxnId] INT;
GO

ALTER TABLE [dbo].[PReturn] ADD [PublisherId] NVARCHAR(10) NULL;
GO

ALTER TABLE [dbo].[PReturn] ALTER COLUMN [SerialId] NVARCHAR(15) NOT NULL;
GO

ALTER TABLE [dbo].[PReturn] ADD CONSTRAINT PReturn_PK PRIMARY KEY([SerialId]);
GO

update pur set PublisherId = sup.PublisherId
  from [PReturn]  pur
  join Supplier sup on pur.SuppId = sup.SuppId
GO
