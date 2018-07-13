USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


  delete from [Matha].[dbo].[SalesDet]
  where SerialId in (select SerialId FROM [Matha].[dbo].[Sales] sl
  where sl.TxnId is null);
  GO

  delete
  FROM [Matha].[dbo].[Sales]
  where TxnId is null;
  GO

  /****** Script for SelectTopNRows command from SSMS  ******/
  SELECT *
    FROM [Matha].[dbo].[SOrderDet]
    where OrderDetId not in
    (select OrderDetId from [Matha].[dbo].[SalesDet] sd)
    and OrderDetId not in
    (select OrderDetId from [Matha].[dbo].[PurDet] pd)


