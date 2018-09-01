USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

UPDATE [dbo].[SalesDet]
SET BkNo = 'IDS1C1'
WHERE BkNo = 'ID1';
GO

UPDATE [dbo].[SalesDet]
SET BkNo = 'IDS2C1'
WHERE BkNo = 'ID2';
GO

ALTER TABLE [dbo].[SalesDet] ADD SalesDetId INT IDENTITY(1,1);
GO

ALTER TABLE [dbo].[SalesDet] ADD OrderItemId INT;
GO

ALTER TABLE [dbo].[SalesDet] ALTER COLUMN [BkNo] nvarchar(30);
GO

--ALTER TABLE [dbo].[SalesDet]  WITH CHECK ADD  CONSTRAINT [FK_SalesDet_Book] FOREIGN KEY([BkNo])
--REFERENCES [dbo].[Book] ([BookNo])
--GO

--ALTER TABLE [dbo].[SalesDet] CHECK CONSTRAINT [FK_SalesDet_Book]
--GO


--declare @i int  = 1
--
--update SalesDet
--set SalesDetId  = @i , @i = @i + 1
--where SalesDetId is null;

--GO

ALTER TABLE [dbo].[SalesDet] ALTER COLUMN [SalesDetId] INT NOT NULL;
GO

ALTER TABLE [dbo].[SalesDet] ADD CONSTRAINT SalesDet_PK PRIMARY KEY([SalesDetId]);
GO

update sd
set sd.OrderItemId = sod.OrderDetId, sd.OrderId = sod.SerialId
  FROM [dbo].[SOrder] so
  join [dbo].[Sales] s on s.CustId = so.CustId
  join [dbo].[SOrderDet] sod on so.SerialId = sod.SerialId
  join [dbo].[SalesDet] sd on sod.BkNo = sd.BkNo and s.SerialId = sd.SerialId
  where so.TDate > '2017-10-01'
  and s.TDate > '2018-02-01';
GO
