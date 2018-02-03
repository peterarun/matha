USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

update bookOut 
  set bookOut.Short = bookOut.Short + '-2'
  from [MathaNew].[dbo].[Book] bookOut
  where SerialId > ANY ( select bookIn.SerialId
  from [MathaNew].[dbo].[Book] bookIn
  where bookOut.Short = bookIn.Short
  and bookOut.BName = bookIn.BName
  );
  
GO
