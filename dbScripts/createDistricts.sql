USE [Matha]
GO

/****** Object:  Table [dbo].[Districts]    Script Date: 06-02-2018 07:58:01 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Districts](
	[name] [varchar](50) NOT NULL,
	[state] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Districts] PRIMARY KEY CLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

INSERT INTO [dbo].[Districts]
           ([name]
           ,[state])
     VALUES
        ('Ernakulam','Kerala'),
        ('Idukki','Kerala'),
        ('Thiruvananthapuram','Kerala'),
        ('Kollam','Kerala'),
        ('Alappuzha','Kerala'),
        ('Pathanamthitta','Kerala'),
        ('Kottayam','Kerala'),
        ('Thrissur','Kerala'),
        ('Palakkad','Kerala'),
        ('Malappuram','Kerala'),
        ('Kozhikode','Kerala'),
        ('Wayanad','Kerala'),
        ('Kannur','Kerala'),
        ('Kasaragod','Kerala');
GO


update bookOut
  set bookOut.district = (
  case
  when Add4 in ('Thiruvananthapuram','Kollam','Alappuzha','Pathanamthitta','Kottayam','Idukki','Ernakulam','Thrissur','Palakkad','Malappuram','Kozhikode','Wayanad','Kannur','Kasaragod')
  then Add4
  when Address3 in ('Thiruvananthapuram','Kollam','Alappuzha','Pathanamthitta','Kottayam','Idukki','Ernakulam','Thrissur','Palakkad','Malappuram','Kozhikode','Wayanad','Kannur','Kasaragod')
  then Address3
  when Add4 in ('TRIVANDRUM', 'THIRUVANANTHAPURAM-01') then 'Thiruvananthapuram'
  when Address3 in ('TRIVANDRUM', 'THIRUVANANTHAPURAM-01') then 'Thiruvananthapuram'
  when Address3 in ('KOTHAMANGALAM', 'MADAKKATHANAM P.O.', 'ALUVA', 'MUVATTUPUZHA') then 'Ernakulam'
  when Address2 in ('ALUVA P.O.,', 'MUVATTUPUZHA', 'PULLUVAZHY', 'OONNUKAL P.O.', 'OONNUKAL') then 'Ernakulam'
  when Address3 in ('KOTTAYAM DISTRICT', 'MAMMOODU P.O.,') then 'Kottayam'
  when Address3 in ('ALAPUZHA') then 'Alappuzha'
  when Address3 in ('THODUPUZHA', 'KATTAPPANA', 'MARAYOOR') then 'Idukki'
  when Address3 in ('CALICUT') then 'Kozhikode'
  when Address3 in ('TRICHUR', 'ORUMANAYOOR') then 'Thrissur'
  when Address3 in ('WAYANADU', 'WAYAND') then 'Wayanad'
  when Address3 in ('NEELESWAR', 'KANJAGAD (VIA)', 'KASARGOD') then 'Kasaragod'
  end )
  from [Matha].[dbo].[Customer] bookOut;

GO
