UPDATE users
SET login = 'newLogin',
    name = 'newName',
    email = 'newEmail@mail.ru',
    birthday = CURRENT_DATE - interval '30 year'
WHERE user_id = (SELECT u.user_id
					FROM users AS u
					WHERE u.user_id = userId);

/*
INSERT INTO user_friends (user_id, friend_id, friendship_status_id)
VALUES (userId, 
		(SELECT f.user_id
			FROM users AS f
			WHERE f.user_id = friendId), 
		(SELECT fs.friendship_status_id
			FROM friendship_status  AS fs
			WHERE status = 'status'))
ON CONFLICT (user_id) DO UPDATE SET friend_id = EXCLUDED.friend_id, friendship_status_id = EXCLUDED.friendship_status_id;
*/