INSERT INTO member (name,
                    password,
                    email,
                    phone,
                    address,
                    role,
                    created_at,
                    updated_at,
                    last_password_updated)
VALUES (
           -- 'duswlsdl' 암호화
           '79736acbba57e83da4a5dac406875d75cce6c417c0961da025adb83f29a5ca20',
           -- 'password123!' BCrypt 해시
           '$2a$10$L7lnE/VTlY6DUuCcXyiDv.kBsGcKFleCRQ3HQcmc.oVxganDgRpZm',
           -- 'duswlsdl0121@naver.com' 암호화
           'G+uTQ0B9CTiEoUJ3onpc3jRhgYf+2IrUxxtpRoSDpeQ=',
           -- '010-1234-5678' 암호화
           'cb236555f3662f2d34dfd920cb7eeb8b415f4b1391824f44e3f581619249c810',
           -- '서울시 강남구 테헤란로 123' 암호화
           'ebecdcffc2f790ace974f1bd471bc21bf2e9f52b897587f96036a5a4e866bd0aa280ea318dff086e1fc9e67c48b3d2dee22720d4806ee32816b58ec251fe6741',
           'ROLE_UNVERIFIED',
           CURRENT_TIMESTAMP(6),
           CURRENT_TIMESTAMP(6),
           CURRENT_TIMESTAMP(6));