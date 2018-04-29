USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE Purchase ADD [DespatchedTo] [nvarchar](20) NULL;
GO

ALTER TABLE Purchase ADD [DocumentsThrough] [nvarchar](20) NULL;
GO

ALTER TABLE Purchase ADD [DespatchPer] [nvarchar](20) NULL;
GO

ALTER TABLE Purchase ADD [Packages] [INT] NULL;
GO

ALTER TABLE Purchase ADD [DiscountType] [BIT] NULL;
GO

ALTER TABLE Purchase ADD [TxnId] [INT] NULL;
GO
