INSERT INTO user_friends (user_id, friend_id, friendship_status_id)
VALUES ((SELECT u.user_id
			FROM users AS u
			WHERE u.user_id = 2),
		(SELECT f.user_id
			FROM users AS f
			WHERE f.user_id = 4), 
		(CASE (SELECT fs.status AS friendship_status
			   FROM user_friends AS uf
			   LEFT JOIN friendship_status AS fs ON uf.friendship_status_id=fs.friendship_status_id
			   WHERE uf.user_id = 4 AND uf.friend_id = 2)
		 	WHEN 'Неподтверждённая' THEN 2
		 	WHEN 'Подтверждённая' THEN 2
		 	ELSE 1
		 END)),
		((SELECT u.user_id
			FROM users AS u
			WHERE u.user_id = 4),
		(SELECT f.user_id
			FROM users AS f
			WHERE f.user_id = 2),
		(CASE (SELECT fs.status AS friendship_status
			   FROM user_friends AS uf
			   LEFT JOIN friendship_status AS fs ON uf.friendship_status_id=fs.friendship_status_id
			   WHERE uf.user_id = 2 AND uf.friend_id = 4)
		 	WHEN 'Неподтверждённая' THEN 2
		 	WHEN 'Подтверждённая' THEN 2
		 	ELSE 1
		 END))
ON CONFLICT (user_id, friend_id) DO UPDATE SET friendship_status_id = EXCLUDED.friendship_status_id;