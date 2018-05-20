USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [dbo].[Supplier]
ADD
    PublisherId INT,
    Email nvarchar(120),
    PIN nvarchar(12),
    logoName nvarchar(120),
    Outstanding decimal(12,2);
GO

update [dbo].[Supplier]
set PublisherId = 48
where SuppId = 14;
GO

update [dbo].[Supplier]
set PublisherId = 55
where SuppId = 15;
GO

update [dbo].[Supplier]
set PublisherId = 56
where SuppId = 16;
GO

update [dbo].[Supplier]
set PublisherId = 57
where SuppId = 17;
GO

update [dbo].[Supplier]
set PublisherId = 46
where SuppId = 18;
GO

update [dbo].[Supplier]
set PublisherId = SuppId + 30
where PublisherId is null;
GO

INSERT INTO [dbo].[Supplier](
	[PublisherId],
	[SuppId],
	[SName],
	[Address1],
	[Email])
VALUES
	('60',
	'13',
	'Matha Agencies',
	'Pareekkal Buildings, Vazhakulam',
	'0485 2260225');
GO

INSERT INTO [dbo].[Supplier](
	[PublisherId],
	[SuppId],
	[SName],
	[Address1],
	[Email])
VALUES
	('45',
	'12',
	'PUBLISHER',
	'Pareekkal Buildings, Vazhakulam',
	'0485 2260225');
GO
