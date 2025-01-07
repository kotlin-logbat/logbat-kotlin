-- PROJECTS 테이블 생성
CREATE TABLE PROJECTS (
    id BIGINT PRIMARY KEY,                   -- 기본 키
    name VARCHAR(100) UNIQUE,                -- 유니크 키
    deleted BIT,                             -- 삭제 여부
    created_at DATETIME,                      -- 생성 일시
    updated_at DATETIME                       -- 수정 일시
);

-- APPS 테이블 생성
CREATE TABLE APPS (
    id BIGINT PRIMARY KEY,                    -- 기본 키
    project_id BIGINT,                        -- PROJECTS 테이블의 외래 키
    name VARCHAR(100),                        -- 이름
    app_key BINARY(16),                        -- 앱 키
    app_type VARCHAR(50),                     -- 앱 타입(enum 처리 필요)
    deleted BIT,                              -- 삭제 여부
    created_at DATETIME,                      -- 생성 일시
    CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES PROJECTS(id) -- 외래 키 설정
);

-- LOGS 테이블 생성
CREATE TABLE LOGS (
    id BIGINT PRIMARY KEY,                    -- 기본 키
    app_id BIGINT,                            -- APPS 테이블의 외래 키
    level TINYINT,                            -- 로그 레벨
    data TEXT,                                -- 데이터
    timestamp DATETIME,                        -- 타임스탬프
    CONSTRAINT fk_app_id FOREIGN KEY (app_id) REFERENCES APPS(id) -- 외래 키 설정
);