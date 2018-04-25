USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

INSERT INTO [dbo].[Addresses](
	[Name],
	[address1],
	[address2],
	[address3],
	[phone1],
	[phone2],
	[Email],
	[PIN])
	VALUES
	('Sales',	
	'PAREECKAL BUILDINGS',
	'VAZHAKULAM P.O',
	'MUVATTUPUZHA',
	'0485 2260225',
	'8281461689, 9495157528',
	'mathaabooks@gmail.com',
	'686 670');
GO

INSERT INTO [dbo].[Addresses](
	[Name],
	[address1],
	[address2],
	[address3],
	[phone1],
	[phone2],
	[Email],
	[PIN])
	VALUES
	('Purchase',
	'No.88, 8th Street, A.K.Swamy Nagar',
	'Kilpauk',
	'Chennai',
	'09444026149',
	NULL,
	NULL,
	'600010');
GO

