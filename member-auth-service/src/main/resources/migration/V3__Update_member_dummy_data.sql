-- V3__Update_member_dummy_data.sql
DELETE FROM member WHERE member_id IN (1, 2);

INSERT INTO member (member_id,
                    name,
                    password,
                    email,
                    phone,
                    address,
                    last_password_updated,
                    role,
                    created_at,
                    updated_at)
VALUES
    (1,
     '13b309e18e9c4df757c999fabe21c18c75a591b6e748a57cb0e8cb664311bc9e',
     '$2a$10$qVTFdVX0XFrFD3ig9KH4VOPge0NBi4bhulYwsKwM0a6S3Zjal/5SW',
     'hong@gmail.com',
     '749a6eb05375f041c482757025c33e5710e8ad04718c4778ec4eafd66489f9e3',
     '849f64dcb249755b32cb20d778a38f395e7b4c9f155672ea88e632fd55461c408910fd81b74f5d94a65226d45a0c2f1f96f8122fe2e2223989f7206377816787',
     '2024-10-31 20:00:54.938056',
     'ROLE_UNVERIFIED',
     '2024-10-31 20:00:54.958015',
     '2024-10-31 20:00:54.958015'),
    (2,
     '00ee0a6c35899f584a96013a379c5192b124bc6438b9e8e34b3516a19a9e5931',
     '$2a$10$PlyQMhTuzJpdS5WbJJqbKeu13owAFmDSMFOocNOyPm4cHDjDCytrC',
     'hongchange@gmail.com',
     '03dde9f87475e7e82a00dd5ed08b730cd75c3ae302795f3e5d9a374d11376c52',
     '4965bdfdd7ebfd25a548817f04d7ce2c41c8a529c1a96e96ff565e7686b7896c5c96c545c60a399a01e9d0cde204e2e5e677a48fafca6e531b3ee6a439935276',
     '2024-10-31 20:01:12.394805',
     'ROLE_UNVERIFIED',
     '2024-10-31 20:01:12.397054',
     '2024-10-31 20:01:33.722203');