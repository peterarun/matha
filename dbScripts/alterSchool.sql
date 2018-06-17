USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table [dbo].[Customer]
add [email] nvarchar(120),
    [principal] nvarchar(120),
    [district] varchar(50),
    [state] varchar(50),
    [Outstanding] [decimal](12,2) NULL;
GO

update bookOut
  set bookOut.state= 'Kerala'
  from [dbo].[Customer] bookOut;

update bookOut
  set bookOut.CName = REPLACE(CName,'S H', 'SH')
  from [dbo].[Customer] bookOut
  where CName like 'S H%';
GO

update bookOut
  set bookOut.CName = REPLACE(CName,'S.H.', 'SH')
  from [Matha].[dbo].[Customer] bookOut
  where CName like 'S.H.%';
GO

update bookOut
  set bookOut.CName = REPLACE(CName,'S.H', 'SH')
  from [Matha].[dbo].[Customer] bookOut
  where CName like 'S.H%'
  and CustId != 671;
GO

update bookOut
  set bookOut.CName = REPLACE(CName,'ST ', 'ST. ')
  from [Matha].[dbo].[Customer] bookOut
  where CName like 'ST %';
GO
