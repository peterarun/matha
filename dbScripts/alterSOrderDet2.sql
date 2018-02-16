USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table SOrderDet 
add SalesId nvarchar(20) null,
    fullfilledCnt int,
    soldCnt int;
    
GO
