USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

update bookOut 
  set bookOut.Short = bookOut.Short + '-2'
  from [Matha].[dbo].[Book] bookOut
  where SerialId > ANY ( select bookIn.SerialId
  from [Matha].[dbo].[Book] bookIn
  where bookOut.Short = bookIn.Short
  and bookOut.BName = bookIn.BName
  );
  
GO
