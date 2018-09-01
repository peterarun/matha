USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


insert into [Matha].[dbo].[Book] ( [SerialId]
      ,[BookNo]
      ,[Short]
      ,[BName]
      ,[CatId]
      ,[PubId]
      ,[Price]
      ,[Inventory])
	  values('BK325', 'MTEEL', 'MTEEL', 'MATHA TALENT EXCELLENT EXAMINATION LKG',51, 60, 120, 100);
GO

insert into [Matha].[dbo].[Book] ( [SerialId]
      ,[BookNo]
      ,[Short]
      ,[BName]
      ,[CatId]
      ,[PubId]
      ,[Price]
      ,[Inventory])
	  values('BK326', 'MTEEU', 'MTEEU', 'MATHA TALENT EXCELLENT EXAMINATION UKG',51, 60, 120, 100);
GO

insert into [Matha].[dbo].[Book] ( [SerialId]
      ,[BookNo]
      ,[Short]
      ,[BName]
      ,[CatId]
      ,[PubId]
      ,[Price]
      ,[Inventory])
	  values('BK213', 'MTEE', 'MTEE', 'MATHA TALENT EXCELLENT EXAMINATION',51, 60, 120, 100);
GO

ALTER DATABASE [Matha_Old] MODIFY FILE (NAME = 'EShopEx_Data', FILENAME = 'F:\Matha\Data\Matha_Old.mdf');
GO

ALTER DATABASE [Matha_Old] MODIFY FILE (NAME = 'EShopEx_Log', FILENAME = 'F:\Matha\Data\MathaOld_log.ldf');
GO

ALTER DATABASE [Matha_Old] MODIFY FILE (NAME = EShopEx_Data, NEWNAME = Matha_Old);
GO

ALTER DATABASE [Matha_Old] MODIFY FILE (NAME = EShopEx_Log, NEWNAME = MathaOld_log);
GO
