INSERT INTO users (login, name, email, birthday)
VALUES ('newLogin', 
		'newName', 
		'newEmail@mail.ru', 
		CURRENT_DATE - interval '30 year')
ON CONFLICT (login, name, email, birthday) DO NOTHING;

INSERT INTO user_friends (user_id, friend_id, friendship_status_id)
VALUES ((SELECT u.user_id
			FROM users AS u
			WHERE u.login = 'newLogin' AND
				  u.name = 'newName' AND
				  u.email = 'newEmail@mail.ru' AND
				  u.birthday = CURRENT_DATE - interval '30 year'), 
		(SELECT f.user_id
			FROM users AS f
			WHERE f.user_id = friendId), 
		(SELECT fs.friendship_status_id
			FROM friendship_status  AS fs
			WHERE status = 'Подтверждённая'))
ON CONFLICT (user_id, friend_id, friendship_status_id) DO NOTHING;