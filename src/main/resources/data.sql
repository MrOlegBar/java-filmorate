MERGE INTO ratings_MPA (rating_MPA)
KEY (rating_MPA)
VALUES ('G'),
	('PG'),
	('PG-13'),
	('R'),
	('NC-17');

MERGE INTO genres (genre)
KEY (genre)
VALUES ('Комедия'),
        ('Драма'),
        ('Мультфильм'),
        ('Триллер'),
        ('Документальный'),
        ('Боевик');

/*
MERGE INTO films (title, description, release_date, duration, rating_MPA_id, rate)
KEY (title, description, release_date, duration, rating_MPA_id, rate)
VALUES ('Побег из Шоушенка', 
		'Бухгалтер Энди Дюфрейн обвинён в убийстве собственной жены и её любовника. 
		Оказавшись в тюрьме Шоушенк, он сталкивается с жестокостью и беззаконием, 
		царящими по обе стороны решётки.',
		'1994-09-10', 
		142,
        4,
        5),
		('Крёстный отец', 
		'Криминальная сага, повествующая о нью-йоркской сицилийской мафиозной семье Корлеоне. 
		 Фильм охватывает период 1945-1955 годов.',
		'1972-03-14', 
		175,
		 4,
		 4),
		('Темный рыцарь', 
		'Бэтмен поднимает ставки в войне с криминалом. 
		 С помощью лейтенанта Джима Гордона и прокурора Харви Дента он намерен очистить улицы Готэма от преступности.',
		'2008-07-14', 
		152,
		 3,
		 3),
		('Крёстный отец 2', 
		'В центре драмы представители нового поколения гангстерского клана — дона Корлеоне и его сына, 
		 для которых не существует моральных преград на пути достижения поставленых целей.',
		'1974-12-12', 
		202,
		 4,
		 2),
		('12 рaзгневанных мужчин', 
		'Юношу обвиняют в убийстве собственного отца, ему грозит электрический стул.
		 Двенадцать присяжных собираются чтобы вынести вердикт: виновен или нет.',
		'1957-04-10',
		96,
		 3,
		 1),
		('Список Шиндлера', 
		'Лента рассказывает реальную историю загадочного Оскара Шиндлера, 
		 члена нацистской партии, преуспевающего фабриканта, 
		 спасшего во время Второй мировой войны более тысячи ста евреев.',
		'1993-11-30',
		195,
		 4,
		 1),
		('Властелин колец: Возвращение короля', 
		'Повелитель сил тьмы Саурон направляет свою бесчисленную армию под стены Минас-Тирита, 
		 крепости Последней Надежды.',
		'2003-12-01',
		201,
		 3,
		 1),
		('Криминальное чтиво', 
		'Двое бандитов Винсент Вега и Джулс Винфилд ведут философские беседы в перерывах между разборками и 
		 решением проблем с должниками криминального босса Марселласа Уоллеса.',
		'1994-05-21',
		154,
		 4,
		 1),
		('Властелин колец: Братство кольца', 
		'Сказания о Средиземье — это хроника Великой войны за Кольцо, войны, длившейся не одну тысячу лет. 
		 Тот, кто владел Кольцом, получал власть над всеми живыми тварями, но был обязан служить злу.',
		'2001-12-10',
		178,
		 3,
		 1),
		('Хороший, плохой, злой', 
		'В разгар гражданской войны таинственный стрелок скитается по просторам Дикого Запада.
		 У него нет ни дома, ни друзей, ни компаньонов, пока он не встречает 2 незнакомцев,
		 таких же безжалостных',
		'1966-12-23',
		161,
		 4,
		 1);

MERGE INTO film_genre (film_id, genre_id)
KEY (film_id, genre_id)
VALUES (1, 2),
		(2, 2),
		(3, 6),
		(4, 2),
		(5, 2),
		(6, 2),
		(6, 5),
		(7, 6),
		(8, 1),
		(8, 2),
		(9, 6),
		(10, 6);

MERGE INTO users (login, name, email, birthday)
KEY (login, name, email, birthday)
VALUES ('BadComedian', 'Евгений Баженов', 'BadComedian@gmail.com', '1991-05-24'),
		('ADolin', 'Антон Долин', 'DolinAnton@yandex.ru', '1976-01-23'),
		('Disney', 'Уолт Дисней', 'WaltDisneyProductions@gmail.com', '1901-12-05'),
		('StarTrek', 'Джин Родденберри', 'StarTrek@yahoo.com', '1901-12-05'),
		('Pokemon', 'Сатоси Тадзири', 'pokemon@yahoo.com', '1965-08-28');

MERGE INTO film_likes (film_id, user_id)
KEY (film_id, user_id)
VALUES (1, 1),
		(1, 2),
		(1, 3),
		(1, 4),
		(1, 5),
		(2, 1),
		(2, 2),
		(2, 3),
		(2, 4),
		(3, 1),
		(3, 2),
		(3, 3),
		(4, 4),
		(4, 5),
		(5, 5),
		(6, 1),
		(7, 2),
		(8, 3),
		(9, 4),
		(10, 5);

MERGE INTO friendship_status (status)
KEY (status)
VALUES ('Неподтверждённая'),
		('Подтверждённая');

MERGE INTO user_friends (user_id, friend_id, friendship_status_id)
KEY (user_id, friend_id, friendship_status_id)
VALUES (1, 2, 2),
		(1, 3, 2),
		(1, 4, 2),
		(1, 5, 2),
		(5, 1, 2),
		(5, 2, 2),
		(5, 3, 2),
		(5, 4, 2),
		(2, 3, 1),
		(3, 4, 1),
		(2, 1, 2),
		(3, 1, 2),
		(4, 1, 2),
		(1, 5, 2),
		(5, 1, 2),
		(2, 5, 2),
		(3, 5, 2),
		(4, 5, 2),
		(3, 2, 1),
		(4, 3, 1);*/