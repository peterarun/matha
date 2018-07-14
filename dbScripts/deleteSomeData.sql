USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


delete from [Matha].[dbo].[SalesDet]
    where SerialId in (select SerialId FROM [Matha].[dbo].[Sales] sl where sl.TxnId is null);
GO

delete FROM [Matha].[dbo].[Sales]
  where TxnId is null;
GO

delete from [Matha].[dbo].[PurDet]
  where SerialId in (select SerialId FROM [Matha].[dbo].[Purchase] sl where sl.TxnId is null);
GO

delete FROM [Matha].[dbo].[Purchase]
  where TxnId is null;
GO

delete FROM [Matha].[dbo].[SOrderDet]
  where OrderDetId not in
    (select sd.OrderItemId from [Matha].[dbo].[SalesDet] sd where sd.OrderItemId is not null)
  and OrderDetId not in
    (select pd.OrderItemId from [Matha].[dbo].[PurDet] pd where pd.OrderItemId is not null)
GO

